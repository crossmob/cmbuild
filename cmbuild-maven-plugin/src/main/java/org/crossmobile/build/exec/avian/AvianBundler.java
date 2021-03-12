package org.crossmobile.build.exec.avian;

import org.crossmobile.utils.Commander;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;

import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.Deflater;

import static org.crossmobile.bridge.system.BaseUtils.throwException;

public class AvianBundler {

    public static void compileAvian(String mainClass, File coreLibraryDir, File appJar, File targetDir, File targetFile, String os, String arch) throws IOException {
        File commonDir = new File(coreLibraryDir, "common");
        File hostFilesDir = new File(coreLibraryDir, "common" + File.separator + "bin" + File.separator + "linux-x86_64");
        File targetPlatformFilesDir = new File(coreLibraryDir, os + "-" + arch);
        File avianFilesDir = new File(targetDir, "avian-files");

        File bootJar = new File(avianFilesDir, "boot.jar").getAbsoluteFile();
        File[] jarFiles = new File[]{
                appJar,
                new File(commonDir, "classpath.jar"),
        };

        FileUtils.delete(avianFilesDir);
        FileUtils.mkdirs(avianFilesDir);
        mergeJars(bootJar, avianFilesDir, jarFiles);

        // Create mainClass name resource
        FileUtils.write(new File(avianFilesDir, "mainclass"), mainClass + '\0');

        // Create .o resource files
        String[] binaries = {"mainclass", "boot.jar"};
        binaryToObject(new File(hostFilesDir, "binaryToObject"), avianFilesDir, binaries, os, arch);

        File libAvian = new File(targetPlatformFilesDir, "libavian.zip");
        if (!FileUtils.unzip(libAvian, avianFilesDir))
            throw new IOException("Unable to unzip file " + libAvian.getAbsolutePath());

        linkApplication(avianFilesDir, targetPlatformFilesDir, targetFile);
        // Strip application
        execCmd(new Commander("strip", "--strip-all", targetFile.getAbsolutePath()));
    }

    private static void mergeJars(File target, File oFiles, File... jarPaths) throws IOException {
        JarOutputStream output = new JarOutputStream(new FileOutputStream(target));
        output.setLevel(Deflater.NO_COMPRESSION);
        Collection<String> allFiles = new HashSet<>();
        for (File jarfile : jarPaths) {
            JarFile jarFile = new JarFile(jarfile);
            copyEntries(jarFile, output, oFiles, allFiles);
        }
        output.close();
    }

    private static void copyEntries(JarFile sourceJar, JarOutputStream output, File oFiles, Collection<String> allFiles) throws IOException {
        Enumeration<JarEntry> sourceEntries = sourceJar.entries();
        while (sourceEntries.hasMoreElements()) {
            JarEntry sourceEntry = sourceEntries.nextElement();
            String sourceName = sourceEntry.getName();
            if (!allFiles.contains(sourceName)) {
                if (sourceName.endsWith(".o")) {
                    InputStream is = sourceJar.getInputStream(sourceEntry);
                    int slash = sourceName.lastIndexOf('/');
                    String filename = sourceName.substring(slash + 1);
                    OutputStream out = new FileOutputStream(new File(oFiles, filename));
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = (is.read(buf))) >= 0)
                        if (len > 0)
                            out.write(buf, 0, len);
                    out.close();
                } else {
                    JarEntry targetEntry = new JarEntry(sourceName);
                    output.putNextEntry(targetEntry);
                    InputStream is = sourceJar.getInputStream(sourceEntry);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = (is.read(buf))) >= 0)
                        if (len > 0)
                            output.write(buf, 0, len);
                    output.flush();
                    output.closeEntry();
                }
                allFiles.add(sourceName);
            } else {
                if (!sourceEntry.isDirectory())
                    System.err.println("Duplicated entry found: " + sourceName);
            }
        }
    }

    private static void binaryToObject(File binaryToObject, File buildDir, String[] binaries, String os, String arch) {
        for (String binary : binaries) {
            String symbol = binary.replace('.', '_');
            Commander cmd = new Commander(binaryToObject.getAbsolutePath(), binary, binary + ".o", "_binary_" + symbol + "_start", "_binary_" + symbol + "_end", os, arch);
            cmd.setCurrentDir(buildDir);
            execCmd(cmd);
        }
    }

    private static void linkApplication(File oFilesDir, File libraryFilesDir, File targetFile) {
        Commander cmd = new Commander("g++", "-rdynamic");

        BiConsumer<String, File> action = (relativePath, file) -> {
            if (file.getName().endsWith(".a") || file.getName().endsWith(".o")) cmd.addArgument(file.getAbsolutePath());
        };
        FileUtils.forAllFiles(oFilesDir, action);
        FileUtils.forAllFiles(libraryFilesDir, action);

        cmd.addArguments("-ldl", "-lfontconfig", "-lpthread", "-lz");
        cmd.addArguments("-o", targetFile.getAbsolutePath());
        cmd.setCurrentDir(oFilesDir);
        execCmd(cmd);
    }

    private static FilenameFilter thatEndsWith(String ends) {
        return (f, name) -> name.endsWith(ends);
    }

    private static void execCmd(Commander cmd) {
        StringBuilder out = new StringBuilder();
        cmd.setOutListener(str -> out.append(str).append('\n'));
        cmd.setErrListener(str -> out.append(str).append('\n'));
        cmd.exec();
        cmd.waitFor();
        if (cmd.exitValue() != 0) {
            Log.error(cmd.toString());
            Log.error(out.toString());
            throwException(new IOException("Unable to link application."));
        } else {
            Log.debug(cmd.toString());
            Log.debug(out.toString());
        }
    }
}

