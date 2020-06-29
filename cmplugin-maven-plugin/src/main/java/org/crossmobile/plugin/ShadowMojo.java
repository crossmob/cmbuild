/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.build.GenericMojo;
import org.crossmobile.build.utils.DependencyDigger;
import org.crossmobile.build.utils.MojoLogger;
import org.crossmobile.plugin.actions.PluginAssembler;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.SystemDependent;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import static org.crossmobile.plugin.DeployMojo.deployableArtifacts;
import static org.crossmobile.utils.FileUtils.toURL;

@Mojo(name = "shadow", defaultPhase = LifecyclePhase.INSTALL)
public class ShadowMojo extends GenericMojo {

    @Parameter(property = "artifacts")
    private String artifacts;


    @Override
    public void exec() {
        MojoLogger.register(getLog());
        System.out.println(artifacts);
    }
}
