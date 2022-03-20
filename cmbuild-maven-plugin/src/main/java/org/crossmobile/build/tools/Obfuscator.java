/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.utils.JavaCommander;
import org.crossmobile.utils.Log;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import static org.crossmobile.build.utils.DependencyJarResolver.ERROR;

public class Obfuscator {

    private static final String[] JAVA_SYS = {"lib/rt.jar", "lib/jce.jar"};

    public static void obfuscate(File proguardLib, File proguardMap, File inputJar, File outputJar, Collection<File> proguardConfig) {
        if (!inputJar.getName().toLowerCase().endsWith(".jar"))
            throw new RuntimeException("Obfuscated file output should have a 'jar' extension");
        JavaCommander cmd = new JavaCommander(proguardLib.getAbsolutePath());
        //noinspection ResultOfMethodCallIgnored
        outputJar.getParentFile().mkdirs();
        //noinspection ResultOfMethodCallIgnored
        proguardMap.getParentFile().mkdirs();

        for (File conf : proguardConfig)
            cmd.addArgument("@").addArgument(conf.getAbsolutePath());

        for (String sysjar : JAVA_SYS)
            cmd.addArgument("-libraryjars").addArgument(new File(System.getProperty("java.home"), sysjar).getAbsolutePath());

        cmd.addArgument("-injars").addArgument(inputJar.getAbsolutePath());
        cmd.addArgument("-outjars").addArgument(outputJar.getAbsolutePath());
        cmd.addArgument("-printmapping").addArgument(proguardMap.getAbsolutePath());

        cmd.setOutListener(Log::info);
        cmd.setErrListener(ERROR);
        cmd.exec();
        cmd.waitFor();
        if (cmd.exitValue() != 0)
            throw new RuntimeException("Unable to execute proguard with command: " + cmd.toString());
    }
}
