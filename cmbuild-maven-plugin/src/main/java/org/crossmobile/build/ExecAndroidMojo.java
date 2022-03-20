/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.crossmobile.build.exec.android.AdbUtils;
import org.crossmobile.build.exec.android.AndroidTargetSelector;
import org.crossmobile.build.exec.android.GradleLauncher;
import org.crossmobile.utils.ExceptionUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.func.Opt;

import javax.swing.*;
import java.io.File;

import static org.crossmobile.utils.ParamsCommon.DEBUG_PROFILE;

@Mojo(name = "execandroid", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ExecAndroidMojo extends ExecGenericMojo {
    private static final String APK_PREFIX1 = File.separator + "target" + File.separator + "app" + File.separator + "build" + File.separator + "outputs" + File.separator + "apk" + File.separator;
    private static final String AAB_PREFIX = File.separator + "target" + File.separator + "app" + File.separator + "build" + File.separator + "outputs" + File.separator + "bundle" + File.separator;

    @Override
    public void exec() {
        File basedir = getProject().getBasedir();
        String bundleID = getProject().getGroupId() + "." + getProject().getArtifactId();
        boolean release = settings.getActiveProfiles().contains("release");
        boolean asApk = !settings.getActiveProfiles().contains("androidappbundle");
        String debugProfile = Opt.of(getSession().getUserProperties().getProperty(DEBUG_PROFILE.tag().name)).getOrElse(DEBUG_PROFILE.tag().deflt);
        String type = asApk ? "APK" : "Android App Bundle";

        ExceptionUtils.callNoException(() -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));

        Log.info("Rebuilding " + type);
        if (!GradleLauncher.runGradle(basedir, release, asApk))
            exitWithError("");
        String apkFile = Opt.of(getApkFile(basedir, release, asApk)).ifMissing(() -> exitWithError("Unable to locate " + type)).get();
        if (!isRunnable())
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
    private static String getApkFile(File basedir, boolean release, boolean apk) {
        String baseName = basedir.getName();
        String rel_deb = release ? "release" : "debug";
        String ext = apk ? "apk" : "aab";
        String prefix = apk ? APK_PREFIX1 : AAB_PREFIX;
        File oldLocation = new File(basedir + prefix + baseName + "-" + rel_deb + "." + ext);
        File newLocation = new File(basedir + prefix + rel_deb + File.separator + baseName + "-" + rel_deb + "." + ext);
        return newLocation.isFile() ? newLocation.getAbsolutePath() : (oldLocation.isFile() ? oldLocation.getAbsolutePath() : null);
    }
}
