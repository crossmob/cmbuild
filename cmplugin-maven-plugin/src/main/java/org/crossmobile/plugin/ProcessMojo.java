/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.crossmobile.plugin.actions.PluginAssembler;
import org.crossmobile.plugin.reg.PluginRegistryFile;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.SystemDependent;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;

import static org.crossmobile.utils.FileUtils.toURL;

@Mojo(name = "process", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ProcessMojo extends GenericPluginMojo {

    @Parameter(readonly = true)
    private String[] embedlibs = new String[]{};

    @Parameter(readonly = true)
    private Packages[] packages;

    @Parameter(defaultValue = "C:\\Program Files (x86)\\Microsoft Visual Studio\\2017\\Community\\VC\\Auxiliary\\Build", readonly = true)
    private File VStudioLocation;

    @Override
    public void exec(Registry reg) {
        skipUwp |= !SystemDependent.canMakeUwp();
        if (skipSwing && skipAvian && skipIos && skipAndroid && skipUwp) {
            Log.info("Skipping all targets");
            return;
        }
        // Append classpaths - maybe should be done otherwise?
        DependencyItem root = getRootDependency(true);
        getPluginDescriptor().getClassRealm().addURL(FileUtils.toURL(new File(getProject().getBuild().getOutputDirectory())));
        for (DependencyItem dep : root.getCompiletimeDependencies(true))
            getPluginDescriptor().getClassRealm().addURL(toURL(dep.getFile()));

        PluginAssembler.assembleFiles(reg, new File(getProject().getBuild().getDirectory()), getRootDependency(true),
                embedlibs, new File(getProject().getBuild().getSourceDirectory()), getVendorSource(), getVendorBin(),
                this::resolveArtifact, getCachedDir(), packages,
                !skipSwing, !skipAvian, !skipIos, !skipAndroid, !skipUwp, !skipRvm,
                VStudioLocation, PluginRegistryFile.forPlugin(mavenProject)
        );
    }
}
