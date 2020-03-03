// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.utils;

import org.crossmobile.utils.JavaCommander;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static org.crossmobile.build.utils.DependencyJarResolver.ERROR;
import static org.crossmobile.utils.CollectionUtils.asList;

public class JavaVersionDowngrader {

    public static void convertToJava7(File retrolambda, File classes, Collection<File> libjars, boolean portDefaultMethods) {
        Collection<String> classpath = new ArrayList<>(asList(libjars, File::getAbsolutePath));
        classpath.add(classes.getAbsolutePath());
        JavaCommander cmd = new JavaCommander(retrolambda.getAbsolutePath()).
                addJavaParam("-Dretrolambda.bytecodeVersion=51").
                addJavaParam("-Dretrolambda.inputDir=" + classes.getAbsolutePath()).
                addJavaParam("-Dretrolambda.classpath=" + TextUtils.iterableToString(classpath, File.pathSeparator)).
                addJavaParam("-javaagent:" + retrolambda.getAbsolutePath());
        if (portDefaultMethods)
            cmd.addJavaParam("-Dretrolambda.defaultMethods=true");
        cmd.setOutListener(Log::debug);
        cmd.setErrListener(ERROR);
        Log.info("Downgrade Java to 7");
        cmd.exec();
        cmd.waitFor();
        if (cmd.exitValue() != 0)
            throw new RuntimeException("Unable to execute retrolambda with command:\n" + cmd.toString());
    }
}
