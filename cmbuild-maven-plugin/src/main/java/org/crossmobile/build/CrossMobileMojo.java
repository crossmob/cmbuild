/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;
import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.prefs.MvnVersions.RetroLambda;
import org.crossmobile.prefs.MvnVersions.XMLVM;
import org.crossmobile.utils.Dependency;
import org.crossmobile.utils.DependencyParam;
import org.crossmobile.utils.ParamSet;
import org.crossmobile.utils.PluginRegistry;
import org.crossmobile.utils.launcher.Flavour;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.crossmobile.build.utils.Config.GENERATED_CMSOURCES;
import static org.crossmobile.utils.ParamsCommon.*;
import static org.crossmobile.utils.PluginMetaData.CURRENT_PLUGIN_REGISTRY;

abstract class CrossMobileMojo extends GenericMojo {
    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${settings}", readonly = true)
    private Settings settings;

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void exec() throws MojoExecutionException {
        if (getProject().getArtifactId().equals("cmproject") || getProject().getArtifactId().equals("cmproject-debug"))
            return;

        DependencyItem dependencies = getRootDependency(false);
        File basedir = getProject().getBasedir();

        getContextData(CM_PLUGIN_REGISTRY, () -> {
            for (DependencyItem dep : dependencies.getCompileOnlyDependencies(true))
                PluginRegistry.importJarPlugin(dep.getFile(), CURRENT_PLUGIN_REGISTRY);
            return true;
        });

        //Add generated sources directory for compile
        getProject().addCompileSourceRoot(new File(getBuildDir(), GENERATED_CMSOURCES).getAbsolutePath());
        Flavour flavour = Flavour.getFlavour(settings.getActiveProfiles());

        ParamSet set = getParamSet(dependencies);   // also removed invalid artifacts - needs dependencies to be populated
        Properties props = loadProperties(set, getProject().getBasedir());
        CMBuildEnvironment.create(basedir, getBuildDir(),
                flavour,
                props,
                set,
                () -> resolveArtifact(new ArtifactInfo(XMLVM.GROUP, XMLVM.ARTIFACT, XMLVM.VERSION, "jar")),
                () -> resolveArtifact(new ArtifactInfo(RetroLambda.GROUP, RetroLambda.ARTIFACT, RetroLambda.VERSION, "jar")),
                getProject().getGroupId(),
                getProject().getArtifactId(),
                getProject().getVersion(),
                dependencies,
                settings.getActiveProfiles().contains("release"),
                settings.getActiveProfiles().contains("run"),
                getSession().getUserProperties().getProperty(DEBUG_PROFILE.tag().name));
        if (settings == null)
            throw new MojoExecutionException("Unable to read maven settings");
        initCoreWorker().run();
    }

    protected abstract Runnable initCoreWorker();

    private Properties loadProperties(ParamSet paramset, File basedir) {
        Properties prop = paramset.getDefaults();
        prop.putAll(getProject().getProperties());
        prop.put(DISPLAY_NAME.tag().name, getProject().getName());
        prop.put(ARTIFACT_ID.tag().name, getProject().getArtifactId());
        prop.put(GROUP_ID.tag().name, getProject().getGroupId());
        prop.put(BUNDLE_VERSION.tag().name, getProject().getVersion());
        try {
            File localProperties = new File(basedir, "local.properties");
            if (localProperties.isFile())
                prop.load(new FileInputStream(localProperties));
        } catch (Exception ignored) {
        }
        return prop;
    }

    /* Implies Dependencies as parameter */
    private ParamSet getParamSet(DependencyItem root) {
        Map<String, Collection<DependencyParam>> possibleParams = new HashMap<>();
        for (Dependency dep : PluginRegistry.getPlugins())
            possibleParams.put(dep.groupId + "." + dep.artifactId, dep.getConfigParams());
        ParamSet p = new ParamSet();
        for (DependencyItem item : root.getCompiletimeDependencies(true))
            for (DependencyParam param : possibleParams.getOrDefault(item.getGroupID() + "." + item.getArtifactID(), Collections.emptyList()))
                p.register(param.param);
        return p;
    }
}
