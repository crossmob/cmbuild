/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.plugin.actions.PluginAssembler;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.SystemDependent;

import java.io.File;

@Mojo(name = "install", defaultPhase = LifecyclePhase.INSTALL)
public class InstallMojo extends GenericPluginMojo {

    @Parameter(defaultValue = "gen/report.txt", readonly = true)
    private File report;

    @Override
    public void exec(Registry reg) {
        skipUwp |= !SystemDependent.canMakeUwp();
        if (skipSwing && skipAvian && skipIos && skipAndroid && skipUwp) {
            Log.info("Skipping all targets");
            return;
        }
        PluginAssembler.installFiles(reg, new File(getProject().getBuild().getDirectory()), getRootDependency(true), this::installAndKeepJar,
                getVendorSource(), getVendorBin(), getCachedDir(),
                !skipSwing, !skipAvian, !skipIos, !skipAndroid, !skipUwp, !skipRvm, !skipCore, report
        );
    }

    private boolean installAndKeepJar(ArtifactInfo info) {
        getDeployArtifacts().add(info);
        return installArtifact(info);
    }
}
