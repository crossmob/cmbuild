/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package org.crossmobile.build.exec.android;

import org.crossmobile.utils.Commander;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.SystemDependent;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.crossmobile.utils.SystemDependent.Execs.EMULATOR;

@SuppressWarnings("WeakerAccess")
public class AdbUtils {
    private final ConnectedAndroidDispatcher dispatcher;
    private final String sdk;
    private final String adb;
    private String device;
    private File baseDir;
    private String debugProfile;

    private static AdbUtils instance;

    public static void launch(String sdk) {
        instance = new AdbUtils(sdk);
    }

    public static AdbUtils getInstance() {
        return instance;
    }

    private AdbUtils(String sdk) {
        File sdkDir = sdk == null || sdk.trim().isEmpty() ? null : new File(sdk);
        if (sdkDir == null || !sdkDir.isDirectory()) {
            Log.error("SDK location not found, please use the configuration wizard to select the desired Android SDK");
            if (sdkDir != null)
                Log.error("SDK path provided: " + sdkDir.getAbsolutePath());
            System.exit(1);
        }
        File adbFile = new File(sdkDir, "platform-tools" + File.separator + SystemDependent.Execs.ADB.filename());
        if (!adbFile.isFile())
            Log.error("Incomplete Android SDK; the build might fail");
        this.sdk = sdkDir.getAbsolutePath();
        this.adb = adbFile.getAbsolutePath();
        dispatcher = new ConnectedAndroidDispatcher(adb);
    }

    public void setListener(ConnectedAndroidDispatcher.AListener listener) {
        dispatcher.setListener(listener);
    }

    public void stop() {
        dispatcher.stop();
    }

    public String getPid(String bundleID) {
        String pid = getPidNew(bundleID);
        return pid == null ? getPidOld(bundleID) : pid;
    }

    private String getPidNew(String bundleID) {
        AtomicReference<String> pid = new AtomicReference<>();
        exec(line -> {
            line = line.trim();
            int space = line.indexOf(' ');
            if (space >= 0 && space < line.length() + 1 && bundleID.equals(line.substring(space).trim())) {
                try {
                    pid.set(line.substring(0, space).trim());
                } catch (NumberFormatException ignored) {
                }
            }
        }, adb, "-s", device, "shell", "ps", "-o", "PID,NAME");
        return pid.get();
    }

    private String getPidOld(String bundleID) {
        AtomicReference<String> pid = new AtomicReference<>();
        AtomicInteger pidColumn = new AtomicInteger(-1);
        exec(line -> {
            line = line.trim();
            List<String> parts = Arrays.asList(line.split("\\s+"));
            if (pidColumn.get() < 0) {
                if (line.contains("PID")) {
                    for (int idx = 0; idx < parts.size(); idx++) {
                        String part = parts.get(idx);
                        if ("PID".equals(part)) {
                            pidColumn.set(idx);
                            break;
                        }
                    }
                }
            } else if (parts.size() > pidColumn.get() && parts.contains(bundleID)) {
                pid.set(parts.get(pidColumn.get()));
            }
        }, adb, "-s", device, "shell", "ps");
        return pid.get();
    }


    public String getPath() {
        return adb;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean foundDevice() {
        return device != null && !device.isEmpty();
    }

    public void installApk(String fileLocation) {
        Log.info("Installing " + new File(fileLocation).getName());
        exec(adb, "-s", device, "install", "-r", fileLocation);
    }

    public void launchApp(String launchId, boolean waitForDebugger) {
        if (waitForDebugger)
            exec(adb, "-s", device, "shell", "am", "start", "-D", "-n", launchId);
        else
            exec(adb, "-s", device, "shell", "am", "start", "-n", launchId);
    }

    public String joinDebugPort(String pid, boolean debug) {
        if (debug) {
            for (int port = 39622; port < 65000; port++) {
                try {
                    new ServerSocket(port).close();
                    exec(adb, "forward", "tcp:" + port, "jdwp:" + pid);
                    return String.valueOf(port);
                } catch (IOException ex) { // Loop until a valid port is found
                }
            }
            Log.error("Unable to open debug port");
        }
        return "unknown";
    }

    public void pipeLog(String pid) {
        if (isLogNew())
            pipeLogNew(pid);
        else
            pipeLogOld(pid);
    }

    private boolean isLogNew() {
        AtomicBoolean supportsNew = new AtomicBoolean(false);
        Consumer<String> parser = line -> {
            if (line.contains("--pid"))
                supportsNew.set(true);
        };
        exec(false, false, parser, parser, null, adb, "-s", device, "logcat", "--INVALID_COMMAND");
        return supportsNew.get();
    }

    private String[] createLogcatArgs(String pid) {
        List<String> commands = new ArrayList<>();
        commands.add(adb);
        commands.add("-s");
        commands.add(device);
        commands.add("logcat");
        if (pid != null)
            commands.add("--pid=" + pid);
        if (!debugProfile.equals("full"))
            commands.add("-s");
        switch (debugProfile) {
            case "outerr":
                commands.add("System.out:I");
            case "err":
                commands.add("System.err:W");
                commands.add("AndroidRuntime:E");
            case "nslog":
                commands.add("CrossMob:*");
        }
        return commands.toArray(new String[0]);
    }

    private void pipeLogOld(String pid) {
        Log.info("");
        exec(false, line -> {
            if (line.contains(pid)) {
                Log.info(line);
                if (line.contains("CrossMob") && line.contains("Activity destroyed"))
                    System.exit(line.contains("error") ? -1 : 0);
            }
        }, createLogcatArgs(null));
    }

    private void pipeLogNew(String pid) {
        Log.info("");
        exec(true, line -> {
            Log.info(line);
            if (line.contains("CrossMob") && line.contains("Activity destroyed"))
                System.exit(line.contains("error") ? -1 : 0);
        }, createLogcatArgs(pid));
    }

    private void exec(String... cmds) {
        exec(true, true, null, null, cmds);
    }

    private void exec(Consumer<String> out, String... cmds) {
        exec(false, false, out, null, cmds);
    }

    private void exec(boolean terminateOnError, Consumer<String> out, String... cmds) {
        exec(true, terminateOnError, out, null, cmds);
    }

    @SuppressWarnings("Convert2MethodRef")
    private void exec(boolean displayCommand, boolean quitOnError, Consumer<String> out, Consumer<String> err, String... cmds) {
        Commander cmd = new Commander(cmds);
        cmd.setCurrentDir(baseDir);
        cmd.setOutListener(out == null ? Log::info : out);
        cmd.setErrListener(err == null ? l -> Log.error(l) : err);
        cmd.setDebug(displayCommand);
        cmd.exec();
        cmd.waitFor();
        if (quitOnError && cmd.exitValue() != 0)
            System.exit(cmd.exitValue());
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }

    public void setDebugProfile(String debugProfile) {
        this.debugProfile = debugProfile;
    }

    public File getEmulator() {
        File emulator = new File(sdk, "emulator" + File.separator + EMULATOR.filename());
        if (!emulator.isFile())
            emulator = new File(sdk, "tools" + File.separator + EMULATOR.filename());
        if (!emulator.isFile())
            emulator = new File(sdk, "cmdline-tools" + File.separator + EMULATOR.filename());
        return emulator.isFile() ? emulator : null;
    }
}
