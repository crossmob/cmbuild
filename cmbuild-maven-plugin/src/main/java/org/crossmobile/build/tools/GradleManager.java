// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.utils.*;

import java.io.File;
import java.util.regex.Pattern;

import static org.crossmobile.build.utils.Config.GRADLE;
import static org.crossmobile.build.utils.Config.GRADLE_PROPERTIES;
import static org.crossmobile.utils.SystemDependent.is64Bit;
import static org.crossmobile.utils.TemplateUtils.copyTemplateIfMissing;

public class GradleManager {

    public static final String GRADLE_PLUGIN = "cmbuild-gradle-plugin";

    public static void createAndUpdate(CMBuildEnvironment env) {
        createAndUpdateBuild(env);
        createAndUpdateProperties(env.getBasedir());
    }

    @SuppressWarnings("UseSpecificCatch")
    private static void createAndUpdateBuild(CMBuildEnvironment env) {
        try {
            File gradle = new File(env.getBasedir(), GRADLE);
            if (gradle.isFile()) {
                String content = FileUtils.read(gradle);
                if (content != null && (content.contains("crossmobile-gradle-plugin:1") || !content.contains("google()")))
                    FileUtils.move(gradle, new File(gradle.getParentFile(), "build.v1.old.gradle"), null);
            }

            ParamList list = new ParamList();
            StringBuilder deps = new StringBuilder();
            StringBuilder groot = new StringBuilder();
            for (PluginMetaData info : env.root().getPluginMetaData()) {
                deps.append(info.getAndroidInjections().getGradleBuildDep());
                groot.append(info.getAndroidInjections().getGradleExt());
            }
            list.put(ParamsCommon.ANDROID_GRADLE_DEPS.tag(), deps.toString());
            list.put(ParamsCommon.ANDROID_GRADLE_ROOT.tag(), groot.toString());
            copyTemplateIfMissing(GRADLE, gradle, "Creating missing build.gradle file", list);
        } catch (Exception ex) {
            BaseUtils.throwException(ex);
        }
    }

    private static void createAndUpdateProperties(File basedir) {
        try {
            copyTemplateIfMissing(GRADLE_PROPERTIES + (is64Bit() ? "" : ".32"),
                    new File(basedir, GRADLE_PROPERTIES), "Creating missing gradle.properties file", null);
        } catch (ProjectException ex) {
            BaseUtils.throwException(ex);
        }
    }
}
