/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;
import org.crossmobile.utils.Log;

public abstract class ExecGenericMojo extends GenericMojo {

    @Parameter(defaultValue = "${settings}", readonly = true)
    protected Settings settings;

    public boolean isRunnable() {
        return settings.getActiveProfiles().contains("run");
    }

    protected void waitSomeTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    protected void exitWithError(String message) {
        if (message != null && !message.isEmpty())
            Log.error(message);
        System.exit(1);
    }

}
