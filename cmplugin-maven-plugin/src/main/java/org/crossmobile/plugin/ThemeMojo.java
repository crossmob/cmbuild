/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.crossmobile.build.GenericMojo;
import org.crossmobile.plugin.actions.XMLPluginWriter;
import org.crossmobile.plugin.reg.PluginRegistryFile;

@Mojo(name = "theme", defaultPhase = LifecyclePhase.COMPILE)
public class ThemeMojo extends GenericMojo {

    @Parameter(readonly = true)
    private String name;

    @Parameter(readonly = true)
    private String description;

    @Override
    public void exec() throws MojoExecutionException {
        if (name == null || name.trim().isEmpty())
            throw new MojoExecutionException("Name of theme not set");
        if (description == null || description.trim().isEmpty())
            throw new MojoExecutionException("Description of theme not set");
        XMLPluginWriter.storeForTheme(PluginRegistryFile.forTheme(mavenProject), mavenProject.getGroupId(),
                mavenProject.getArtifactId(), mavenProject.getVersion(), name, description);
    }
}
