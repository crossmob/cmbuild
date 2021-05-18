/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.crossmobile.plugin.actions.PluginAssembler;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.SystemDependent;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;

import static org.crossmobile.utils.FileUtils.toURL;

@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE)
public class PackageMojo extends GenericPluginMojo {

    @Override
    public void exec(Registry reg) {
        skipUwp |= !SystemDependent.canMakeUwp();
        if (skipSwing && skipAroma && skipIos && skipAndroid && skipUwp) {
            Log.info("Skipping all targets");
            return;
        }
        // Append classpaths - maybe should be done otherwise?
        DependencyItem root = getRootDependency(true);
        getPluginDescriptor().getClassRealm().addURL(FileUtils.toURL(new File(getProject().getBuild().getOutputDirectory())));
        for (DependencyItem dep : root.getCompiletimeDependencies(true))
            getPluginDescriptor().getClassRealm().addURL(toURL(dep.getFile()));

        PluginAssembler.packageFiles(reg, new File(getProject().getBuild().getDirectory()), new File(getProject().getBuild().getSourceDirectory()),
                !skipSwing, !skipAroma, !skipIos, !skipAndroid, !skipUwp, !skipRvm
        );
    }
}
