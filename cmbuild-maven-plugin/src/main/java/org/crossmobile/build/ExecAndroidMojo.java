/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.settings.Settings;
import org.crossmobile.build.exec.android.AdbUtils;
import org.crossmobile.build.exec.android.AndroidTargetSelector;
import org.crossmobile.build.exec.android.GradleLauncher;
import org.crossmobile.utils.ExceptionUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.Opt;

import javax.swing.*;
import java.io.File;

import static org.crossmobile.utils.ParamsCommon.DEBUG_PROFILE;

@Mojo(name = "execandroid", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ExecAndroidMojo extends GenericMojo {
    private static final String APK_PREFIX = File.separator + "target" + File.separator + "app" + File.separator + "build" + File.separator + "outputs" + File.separator + "apk" + File.separator;

    @Parameter(defaultValue = "${settings}", readonly = true)
    private Settings settings;

    @Override
    public void exec() {
        File basedir = getProject().getBasedir();
        String bundleID = getProject().getGroupId() + "." + getProject().getArtifactId();
        boolean run = settings.getActiveProfiles().contains("run");
        boolean release = settings.getActiveProfiles().contains("release");
        String debugProfile = Opt.of(getSession().getUserProperties().getProperty(DEBUG_PROFILE.tag().name)).getOrElse(DEBUG_PROFILE.tag().deflt);

        ExceptionUtils.callNoException(() -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));

        Log.info("Rebuild APK");
        if (!GradleLauncher.runGradle(basedir, release))
            exitWithError("");
        String apkFile = Opt.of(getApkFile(basedir, release)).ifMissing(() -> exitWithError("Unable to locate APK")).get();
        if (!run)
            return;

        AdbUtils adb = AdbUtils.getInstance();
        adb.setBaseDir(basedir);
        adb.setDebugProfile(debugProfile);

        Log.info("Listing connected Android devices");
        adb.setListener(AndroidTargetSelector.init(dvc -> {
            adb.stop();
            adb.setDevice(dvc);
            if (!adb.foundDevice())
                exitWithError("No devices selected, exiting.");
            adb.installApk(apkFile);
            String oldPid = adb.getPid(bundleID);
            adb.launchApp(bundleID + "/" + bundleID + ".CMLauncher", false);

            String newPid = null;
            for (int i = 0; i < 10; i++) {
                newPid = adb.getPid(bundleID);
                if (newPid != null && !newPid.equals(oldPid))
                    break;
                else
                    newPid = null;
                waitSomeTime();
            }
            if (newPid == null)
                exitWithError("Unable to retrieve PID of activity " + bundleID);
            Log.info("PID " + newPid + " uses " + adb.joinDebugPort(newPid, !release) + " port for debugging");
            adb.pipeLog(newPid);
        }, adb.getEmulator()));
        while (true)
            waitSomeTime();
    }

    // Android SDK is peculiar. The name of the APK is based on the name of the current folder.
    private static String getApkFile(File basedir, boolean release) {
        String apkName = basedir.getName();
        String rel_deb = release ? "release" : "debug";
        File oldLocation = new File(basedir + APK_PREFIX + apkName + "-" + rel_deb + ".apk");
        File newLocation = new File(basedir + APK_PREFIX + rel_deb + File.separator + apkName + "-" + rel_deb + ".apk");
        return newLocation.isFile() ? newLocation.getAbsolutePath() : (oldLocation.isFile() ? oldLocation.getAbsolutePath() : null);
    }

    private static void waitSomeTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    private static void exitWithError(String message) {
        if (message != null && !message.isEmpty())
            Log.error(message);
        System.exit(1);
    }
}
