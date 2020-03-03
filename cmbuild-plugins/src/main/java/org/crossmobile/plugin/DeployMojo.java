// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.build.GenericMojo;
import org.crossmobile.build.utils.MojoLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY, requiresDependencyResolution = ResolutionScope.COMPILE)
public class DeployMojo extends GenericMojo {

    static final Collection<ArtifactInfo> deployableArtifacts = new LinkedHashSet<>();
    @Parameter(property = "repositoryId")
    private String repositoryId;

    @Parameter(property = "url")
    private String url;

    @Override
    public void exec() {
        MojoLogger.register(getLog());

        if (repositoryId == null)
            repositoryId = getProject().getDistributionManagementArtifactRepository().getId();
        if (url == null)
            url = getProject().getDistributionManagementArtifactRepository().getUrl();

        Collection<ArtifactInfo> toDeploy = new ArrayList<>();
        synchronized (deployableArtifacts) {
            toDeploy.addAll(deployableArtifacts);
            deployableArtifacts.clear();
        }
        for (ArtifactInfo info : toDeploy)
            if (!deployArtifact(info, repositoryId, url))
                break;
    }
}
