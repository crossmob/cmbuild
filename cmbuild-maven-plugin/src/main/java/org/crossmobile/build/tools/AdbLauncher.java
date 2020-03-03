// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.utils.Commander;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdbLauncher {

    private static final boolean isWindows = System.getProperty("os.name", "").toLowerCase().contains("windows");
    private final File adb;

    public AdbLauncher(String sdk) {
        this.adb = sdk == null ? null : new File(sdk + File.separator + "platform-tools", isWindows ? "adb.exe" : "adb");
    }

    public Commander exec(String... args) {
        if (adb != null && !adb.isFile())
            return null;
        List<String> commands = new ArrayList<>();
        commands.add(adb.getAbsolutePath());
        if (args != null && args.length > 0)
            commands.addAll(Arrays.asList(args));
        Commander cmd = new Commander(commands).exec();
        return cmd;
    }
}
