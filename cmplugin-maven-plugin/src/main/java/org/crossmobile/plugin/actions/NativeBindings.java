/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.plugin.reg.NativeMethodRegistry;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.structs.Target;
import org.crossmobile.plugin.structs.Target.ValidTarget;
import org.crossmobile.utils.Commander;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.io.File.separator;
import static java.util.Objects.requireNonNull;
import static org.crossmobile.bridge.system.BaseUtils.throwException;
import static org.crossmobile.plugin.actions.PluginAssembler.aromaBase;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.SystemDependent.Execs.JAVAH;

public class NativeBindings {

    private static final Pattern methodP = Pattern.compile("JNIEXPORT\\s+(.*?)\\s+JNICALL\\s+(.*?)\\s*\\(\\s*" +
            "JNIEnv\\s*\\*\\s*,\\s*(.*?)\\);");

    static BiFunction<File, Target, File> oDir = (targetDir, target) -> mkdirs(new File(targetDir, "jni" + separator + "build" + separator + target.getName()));
    static Function<File, File> javaIncludeDir = (targetDir) -> mkdirs(new File(targetDir, "jni" + separator + "include"));

    private static File guessJavah(File given) {
        if (given != null && given.isFile())
            return given;
        File javaHome = new File(System.getProperty("java.home"));
        if (!javaHome.isDirectory())
            throw new RuntimeException("Unable to find JAVA_HOME under '" + javaHome.getAbsolutePath() + "'");

        File javah = new File(javaHome, "bin" + separator + JAVAH.filename());
        if (javah.isFile())
            return javah;
        if (javaHome.getName().equals("jre")) {
            javah = new File(javaHome.getParent(), "bin" + separator + JAVAH.filename());
            if (javah.isFile())
                return javah;
        }
        throw new RuntimeException("Unable to locate javah using JAVA_HOME " + javaHome.getAbsolutePath());
    }

    public static void createNativeBinding(NativeMethodRegistry reg, File classpath, File srcOut, File targetDir, File javahLocation, Collection<Target> targets, File projectLocation) {
        if (!reg.hasNatives())
            return;
        javahLocation = guessJavah(javahLocation);

        File hLocation = mkdirs(new File(srcOut, "include"));
        File cLocation = mkdirs(new File(srcOut, "src"));

        // Run javah command
        Commander cmd = new Commander(javahLocation.getAbsolutePath(),
                "-d", hLocation.getAbsolutePath(),
                "-cp", classpath.getAbsolutePath());
        reg.stream().forEach(c -> cmd.addArguments(c.getName()));
        StringBuilder out = new StringBuilder();
        cmd.setOutListener(str -> out.append(str).append('\n'));
        cmd.setErrListener(str -> out.append(str).append('\n'));
        cmd.exec();
        cmd.waitFor();
        if (cmd.exitValue() != 0) {
            Log.error(cmd.toString());
            Log.error(out.toString());
            throwException(new IOException("Unable to produce C headers"));
        } else {
            Log.debug(cmd.toString());
            Log.debug(out.toString());
        }

        // Check/create C files
        Collection<File> cFiles = reg.stream().parallel()
                .map(c -> guessCFile(cLocation, getBaseName(c), hLocation))
                .collect(Collectors.toSet());

        // Compile JNI files
        if (!targets.isEmpty()) {
            File javaIncludeDir = NativeBindings.javaIncludeDir.apply(targetDir);
            if (!new File(javaIncludeDir, "jni.h").isFile()) {
                FileUtils.delete(javaIncludeDir);
                FileUtils.unzip(NativeBindings.class.getResourceAsStream("/org/crossmobile/plugin/jni.zip"), javaIncludeDir);
            }
            // Compile for all required targets
            for (Target target : targets)
                compileForArch(target, cFiles, hLocation, javaIncludeDir, targetDir, projectLocation);
        }
    }

    private static void compileForArch(Target target, Collection<File> cFiles, File hLocation, File javaIncludeDir, File targetDir, File projectLocation) {
        ValidTarget vt = target.asValidTarget();
        File oDir = NativeBindings.oDir.apply(targetDir, target);

        // Compile JNI files to target architecture */
        File osIncludeDir = new File(javaIncludeDir, target.getOs());

        List<String> incDirs = target.getIncludes();
        incDirs.add(hLocation.getAbsolutePath());
        incDirs.add(javaIncludeDir.getAbsolutePath());
        incDirs.add(osIncludeDir.getAbsolutePath());

        List<String> includes = new ArrayList<>();
        List<String> volumes = new ArrayList<>();
        int idx = 1;
        for (String incl : incDirs) {
            volumes.add("-v");
            volumes.add(getAbs(projectLocation, incl) + ":/include/dir" + idx);
            includes.add("-I/include/dir" + idx);
            idx++;
        }

        cFiles.stream().parallel().forEach(cFile -> {
            // Run CC
            File oFile = new File(oDir, removeExtension(cFile.getName()) + ".o");

            Commander cmd = new Commander("docker", "run", "--rm");
            cmd.addArguments(volumes);
            cmd.addArguments("-v", getAbs(cFile.getParentFile()) + ":/src",
                    "-v", getAbs(oFile.getParentFile()) + ":/out",
                    "aroma/dep-builder-" + vt.getDockerName(),
                    (vt.usesCrossCompilerPrefix() ? vt.getTriple() + "-" : "") + "gcc",
                    "-L" + "/usr/lib/" + vt.getTriple(),
                    "-D_JNI_IMPLEMENTATION_",
                    "-fPIC",
                    "-I/src");
            cmd.addArguments(includes);
            if (!containsOptimizationFlags(target.getCcflags()))
                cmd.addArgument("-Os");
            cmd.addArguments(target.getCcflags());
            cmd.addArguments("-c", "/src/" + cFile.getName(), "-o", "/out/" + oFile.getName());
            StringBuilder out = new StringBuilder();
            cmd.setOutListener(str -> out.append(str).append('\n'));
            cmd.setErrListener(str -> out.append(str).append('\n'));
            cmd.setCurrentDir(projectLocation);
            cmd.exec();
            cmd.waitFor();
            if (cmd.exitValue() != 0) {
                Log.error(cmd.toString());
                Log.error(out.toString());
                throwException(new IOException("Unable to compile file " + cFile.getAbsolutePath()));
            } else {
                Log.debug(cmd.toString());
                Log.debug(out.toString());
            }
        });
    }

    private static String getBaseName(Class<?> cls) {
        return cls.getName().replace('.', '_');
    }

    private static boolean containsOptimizationFlags(List<String> ccflags) {
        if (ccflags != null)
            for (String flag : ccflags)
                if (flag.startsWith("-O"))
                    return true;
        return false;
    }

    private static File guessCFile(File baseDir, String baseName, File includeDir) {
        File srcFile;
        srcFile = new File(baseDir, baseName + ".cc");
        if (srcFile.isFile())
            return srcFile;
        srcFile = new File(baseDir, baseName + ".c++");
        if (srcFile.isFile())
            return srcFile;
        srcFile = new File(baseDir, baseName + ".cxx");
        if (srcFile.isFile())
            return srcFile;
        srcFile = new File(baseDir, baseName + ".cp");
        if (srcFile.isFile())
            return srcFile;
        srcFile = new File(baseDir, baseName + ".c");
        if (srcFile.isFile())
            return srcFile;
        srcFile = new File(baseDir, baseName + ".cpp");
        if (srcFile.isFile())
            return srcFile;
        // No file found -- construct one
        File hFile = new File(includeDir, baseName + ".h");
        if (!hFile.isFile())
            throwException(new IOException("Unable to locate JNI file " + hFile.getAbsolutePath()));
        return constructCFile(srcFile, hFile);
    }

    private static File constructCFile(File cFile, File hFile) {
        String cdata = requireNonNull(FileUtils.read(hFile)).replace('\n', ' ').replace('\r', ' ');
        Matcher matcher = methodP.matcher(cdata);
        StringBuilder out = new StringBuilder();

        out.append("// JNI file ").append(cFile.getName()).append("\n");
        out.append("\n#include\"").append(hFile.getName()).append("\"\n");
        while (matcher.find()) {
            out.append("\n\nJNIEXPORT ").append(matcher.group(1).trim()).append(" JNICALL ").append(matcher.group(2).trim())
                    .append("\n  (JNIEnv *env");
            int index = 0;
            for (String part : matcher.group(3).trim().split(",")) {
                part = part.trim();
                if (index == 0) {
                    if (part.equals("jobject"))
                        out.append(", jobject thiz");
                    else if (part.equals("jclass"))
                        out.append(", jclass clazz");
                    else
                        throwException(new IOException("Unrecognized token " + part + " in JNI definition " + matcher.group(0)));
                } else
                    out.append(", ").append(part).append(" var").append(index);
                index++;
            }
            out.append(") {\n\n}\n");
        }
        FileUtils.write(cFile, out.toString());
        return cFile;
    }

    public static void createArchive(File targetDir, Registry reg, Collection<Target> targets) {
        for (Target target : targets) {
            File inDir = oDir.apply(targetDir, target);
            reg.natives().stream().forEach(c -> {
                File inFile = new File(inDir, getBaseName(c) + ".o");
                File outFile = new File(aromaBase.apply(targetDir, reg.plugins().getPlugin(c.getName())),
                        "native" + separator + target.getName() + separator + inFile.getName());
                copy(inFile, outFile);
            });
        }
    }
}
