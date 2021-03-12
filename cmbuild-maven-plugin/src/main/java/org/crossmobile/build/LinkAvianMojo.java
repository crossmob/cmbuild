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

import java.io.File;
import java.io.IOException;

import static java.io.File.separator;
import static java.lang.String.format;
import static org.crossmobile.build.exec.avian.AvianBundler.compileAvian;
import static org.crossmobile.utils.ParamsCommon.MAIN_CLASS;

@Mojo(name = "linkavian", defaultPhase = LifecyclePhase.PACKAGE)
public class LinkAvianMojo extends ExecGenericMojo {

    @Parameter
    private File avianLocation;

    @Override
    public void exec() {
//        if (!isRunnable())
//            return;
        if (avianLocation == null)
            avianLocation = new File(new File(System.getProperty("user.home")), format(".cache%scrossmobile%savian%s%s", separator, separator, separator, "0.1"));

        File target = new File(getProject().getBasedir(), "target");

        String mainClass = getProject().getProperties().getProperty(MAIN_CLASS.tag().name);
        File baseJar = new File(getProject().getProperties().getProperty("cm.launch.avian"));

        try {
            compileAvian(mainClass, avianLocation, baseJar, target, new File(target, "aroma"), "linux", "x86_64");
        } catch (IOException e) {
            BaseUtils.throwException(e);
        }
    }
}
