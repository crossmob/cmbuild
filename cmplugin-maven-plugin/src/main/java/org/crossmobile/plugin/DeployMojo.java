/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.plugin.reg.Registry;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY, requiresDependencyResolution = ResolutionScope.COMPILE)
public class DeployMojo extends GenericPluginMojo {

    @Parameter(property = "repositoryId")
    private String repositoryId;

    @Parameter(property = "url")
    private String url;

    @Override
    public void exec(Registry reg) {
        if (repositoryId == null)
            repositoryId = getProject().getDistributionManagementArtifactRepository().getId();
        if (url == null)
            url = getProject().getDistributionManagementArtifactRepository().getUrl();
        for (ArtifactInfo info : getDeployArtifacts())
            if (!deployArtifact(info, repositoryId, url))
                break;
    }
}
