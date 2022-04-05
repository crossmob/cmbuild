/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.build.GenericMojo;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public abstract class GenericPluginMojo extends GenericMojo {

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skipSwing;

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skipIos;

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skipAndroid;

    @Parameter(defaultValue = "true", readonly = true)
    protected boolean skipUwp = true;

    @Parameter(defaultValue = "true", readonly = true)
    protected boolean skipRvm = true;

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skipCore;

    protected abstract void exec(Registry reg) throws MojoExecutionException, MojoFailureException;

    public void exec() throws MojoExecutionException, MojoFailureException {
        DependencyItem root = getRootDependency(true);
        if (!"jar".equals(root.getType()))
            Log.info("Skipping plugin creation for " + root.getArtifactID() + ", only JAR files supported, found " + root.getFile().getAbsolutePath());
        else
            exec(getContextData(CM_REGISTRY, Registry::new));
    }

    protected File getVendorSource() {
        return new File(new File(getProject().getBuild().getSourceDirectory()).getParent(), "objc");
    }

    protected File getVendorBin() {
        return new File(getProject().getBasedir(), "lib/main/vendor");
    }

    public File getCachedDir() {
        return new File(getProject().getBasedir(), "gen");
    }

    public Collection<ArtifactInfo> getDeployArtifacts() {
        return getContextData(CM_DEPLOY_ARTIFACTS, ArrayList::new);
    }
}
