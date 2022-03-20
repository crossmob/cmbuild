/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.crossmobile.utils.Commander;

@Mojo(name = "execaroma", defaultPhase = LifecyclePhase.INSTALL)
public class ExecAromaMojo extends ExecGenericMojo {

    private static final int DaemonTimeout = 3000;

    @Override
    public void exec() {
        if (!isRunnable())
            return;
        Commander cmd = new Commander(getProject().getProperties().getProperty("cm.launch.aroma.exec"));
        cmd.setCurrentDir(getProject().getFile().getParentFile());
        cmd.setOutListener(System.out::println);
        cmd.setErrListener(System.err::println);
        cmd.exec();
        cmd.waitFor();
        if (cmd.exitValue() != 0)
            throw new RuntimeException("Exit code: " + cmd.exitValue());
    }
}
