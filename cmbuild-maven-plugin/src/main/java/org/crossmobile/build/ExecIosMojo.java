/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;

@Mojo(name = "execios", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ExecIosMojo extends ExecGenericMojo {
    @Override
    public void exec() throws MojoExecutionException {
        if (!isRunnable())
            return;

        File baseDir = getProject().getBasedir();
        String app = getProject().getArtifactId();
        File xcworkspace = new File(baseDir, app + ".xcworkspace");
        File xcodeproj = new File(baseDir, app + ".xcodeproj");
        File project = new File(xcworkspace, "contents.xcworkspacedata").exists() ? xcworkspace : xcodeproj;
        try {
            Runtime.getRuntime().exec(new String[]{"open", project.getAbsolutePath()});
        } catch (IOException ex) {
            throw new MojoExecutionException("Unable to launch project " + project.getName(), ex);
        }
    }
}
