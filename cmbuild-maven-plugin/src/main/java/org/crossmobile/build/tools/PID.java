// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.utils.Log;

import java.lang.management.ManagementFactory;

public class PID {

    public void exec() {
        Log.debug("Runtime Name: " + ManagementFactory.getRuntimeMXBean().getName());
    }

}
