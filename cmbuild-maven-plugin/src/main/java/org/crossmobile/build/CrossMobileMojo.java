/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;
import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.build.utils.DependencyDigger;
import org.crossmobile.build.utils.MojoLogger;
import org.crossmobile.build.utils.Versions.RetroLambda;
import org.crossmobile.build.utils.Versions.XMLVM;
import org.crossmobile.utils.Dependency;
import org.crossmobile.utils.DependencyParam;
import org.crossmobile.utils.ParamSet;
import org.crossmobile.utils.func.Opt;
import org.crossmobile.utils.func.ScopeUtils;
import org.crossmobile.utils.launcher.Flavour;
import org.crossmobile.utils.lic.LicencedItems;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.util.*;

import static org.crossmobile.build.utils.Config.GENERATED_CMSOURCES;
import static org.crossmobile.utils.ParamsCommon.*;
import static org.crossmobile.utils.func.ScopeUtils.with;

public abstract class CrossMobileMojo extends GenericMojo {

    private static final String CM_PROJ_DEPS = "CM_PROJECT_DEPS";

    @Parameter(defaultValue = "${settings}", readonly = true)
    private Settings settings;

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void exec() throws MojoExecutionException {
        if (getProject().getArtifactId().equals("cmproject") || getProject().getArtifactId().equals("cmproject-debug"))
            return;
        MojoLogger.register(getLog());

        DependencyItem dependencies = Opt.of(getProject().getContextValue(CM_PROJ_DEPS)).map(d -> (DependencyItem) d)
                .mapMissing(() -> with(DependencyDigger.getDependencyTree(getProject(), getSession(), getDependencyGraph(), this::resolveArtifact, false),
                        d -> getProject().setContextValue(CM_PROJ_DEPS, d))).get();
        File basedir = getProject().getBasedir();

        //Add generated sources directory for compile
        getProject().addCompileSourceRoot(new File(getBuildDir(), GENERATED_CMSOURCES).getAbsolutePath());
        Flavour flavour = Flavour.getFlavour(settings.getActiveProfiles());
        String appId = getProject().getGroupId() + "." + getProject().getArtifactId();
        LicencedItems.checkLicense(dependencies, appId, flavour);

        ParamSet set = getParamSet(dependencies);   // also removed invalid artifacts - needs dependencies to be populated
        Properties props = loadProperties(set);
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

    private boolean isRelease(List<String> profiles) {
        for (String prof : profiles)
            if ("release".equals(prof.toLowerCase()))
                return true;
        return false;
    }

    private Properties loadProperties(ParamSet paramset) {
        Properties prop = paramset.getDefaults();
        prop.putAll(getProject().getProperties());
        prop.put(DISPLAY_NAME.tag().name, getProject().getName());
        prop.put(ARTIFACT_ID.tag().name, getProject().getArtifactId());
        prop.put(GROUP_ID.tag().name, getProject().getGroupId());
        prop.put(BUNDLE_VERSION.tag().name, getProject().getVersion());
        return prop;
    }

    /* Implies Dependencies as parameter */
    private ParamSet getParamSet(DependencyItem root) {
        Map<String, Collection<DependencyParam>> possibleparams = new HashMap<>();
        for (Dependency dep : Dependency.getSystemPlugins())
            possibleparams.put(dep.groupId + "." + dep.artifactId, dep.getConfigParams());
        ParamSet p = new ParamSet();
        for (DependencyItem item : root.getCompiletimeDependencies(true))
            for (DependencyParam param : possibleparams.getOrDefault(item.getGroupID() + "." + item.getArtifactID(), (List<DependencyParam>) Collections.EMPTY_LIST))
                p.register(param.param);
        return p;
    }
}
