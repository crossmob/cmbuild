/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.utils.Commander;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.Deflater;

import static java.io.File.separator;
import static java.lang.String.format;
import static org.crossmobile.bridge.system.BaseUtils.throwException;
import static org.crossmobile.utils.ParamsCommon.MAIN_CLASS;

@Mojo(name = "linkavian", defaultPhase = LifecyclePhase.PACKAGE)
public class LinkAvianMojo extends ExecGenericMojo {
    private static final Collection<String> DUPLICATE_BLACKLIST = new HashSet<>(Collections.singletonList("META-INF/MANIFEST.MF"));

    @Parameter
    private File avianLocation;

    @Override
    public void exec() {
        if (avianLocation == null)
            avianLocation = new File(new File(System.getProperty("user.home")), format(".cache%scrossmobile%savian%s%s", separator, separator, separator, "0.1"));

        File target = new File(getProject().getBuild().getDirectory());

        String mainClass = getProject().getProperties().getProperty(MAIN_CLASS.tag().name);
        File baseJar = new File(getProject().getProperties().getProperty("cm.launch.avian"));

        try {
            File exec = compileAvian(mainClass, getProject().getArtifactId(), avianLocation, baseJar, target, "linux", "x86_64");
            getProject().getProperties().setProperty("cm.launch.avian.exec", exec.getAbsolutePath());
        } catch (IOException e) {
            BaseUtils.throwException(e);
        }
    }

    public static File compileAvian(String mainClass, String appName, File coreLibraryDir, File appJar, File targetDir, String os, String arch) throws IOException {
        File commonDir = new File(coreLibraryDir, "common");
        File hostFilesDir = new File(coreLibraryDir, "common" + File.separator + "bin" + File.separator + "linux-x86_64");
        File targetPlatformFilesDir = new File(coreLibraryDir, os + "-" + arch);
        File avianFilesDir = new File(targetDir, "avian-files");
        File targetFile = new File(targetDir, appName + "." + os + "-" + arch);

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
        for (String binary : new String[]{"mainclass", "boot.jar"})
            binaryToObject(new File(hostFilesDir, "binaryToObject"), avianFilesDir, binary, os, arch);

        File libAvian = new File(targetPlatformFilesDir, "libavian.zip");
        if (!FileUtils.unzip(libAvian, avianFilesDir))
            throw new IOException("Unable to unzip file " + libAvian.getAbsolutePath());

        linkApplication(avianFilesDir, targetPlatformFilesDir, targetFile);
        // Strip application
        execCmd(new Commander("strip", "--strip-all", targetFile.getAbsolutePath()));

        Log.info("Successfully created avian executable " + targetFile);
        return targetFile;
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
                if (!sourceEntry.isDirectory() && !DUPLICATE_BLACKLIST.contains(sourceName))
                    Log.warning("Duplicated entry found: " + sourceName);
            }
        }
    }

    private static void binaryToObject(File binaryToObject, File buildDir, String binary, String os, String arch) {
        String symbol = binary.replace('.', '_');
        Commander cmd = new Commander(binaryToObject.getAbsolutePath(), binary, binary + ".o", "_binary_" + symbol + "_start", "_binary_" + symbol + "_end", os, arch);
        cmd.setCurrentDir(buildDir);
        execCmd(cmd);
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
