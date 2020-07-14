/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.crossmobile.build.utils.DependencyDigger;
import org.crossmobile.build.utils.MojoLogger;
import org.crossmobile.utils.func.Opt;
import org.crossmobile.utils.plugin.DependencyItem;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Supplier;

import static org.crossmobile.bridge.system.BaseUtils.throwExceptionAndReturn;
import static org.crossmobile.utils.func.ScopeUtils.with;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

public abstract class GenericMojo extends AbstractMojo {

    protected static final String CM_DEPLOY_ARTIFACTS = "CM_DEPLOY_ARTIFACTS";
    protected static final String CM_REGISTRY = "CM_REGISTRY";
    private static final String CM_PROJ_DEPS = "CM_PROJECT_DEPS";

    @Component(hint = "default")
    protected DependencyGraphBuilder dependencyGraphBuilder;

    @Parameter(defaultValue = "${project}", readonly = true, property = "")
    protected MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true)
    protected MavenSession mavenSession;

    @Component
    protected BuildPluginManager pluginManager;

    @Component
    protected RepositorySystem repoSystem;

    @Parameter(defaultValue = "false", readonly = true)
    protected boolean skip;

    @Parameter(defaultValue = "${plugin}", readonly = true)
    protected PluginDescriptor pluginDescriptor;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip)
            return;
        MojoLogger.register(getLog());
        exec();
    }

    public abstract void exec() throws MojoExecutionException, MojoFailureException;

    public File resolveArtifact(ArtifactInfo info) {
        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact(new DefaultArtifact(info.groupId, info.artifactId, info.packaging, info.version));
        request.setRepositories(mavenProject.getRemoteProjectRepositories());
        try {
            ArtifactResult result = repoSystem.resolveArtifact(mavenSession.getRepositorySession(), request);
            return result.getArtifact().getFile();
        } catch (Exception ex) {
            return throwExceptionAndReturn(ex);
        }
    }

    public boolean installArtifact(ArtifactInfo info) {
        try {
            executeMojo(
                    plugin(
                            groupId("org.apache.maven.plugins"),
                            artifactId("maven-install-plugin"),
                            version("2.5.2")
                    ),
                    goal("install-file"),
                    configuration(
                            element(name("file"), info.source == null ? "" : info.source.getAbsolutePath()),
                            element(name("groupId"), info.groupId),
                            element(name("artifactId"), info.artifactId),
                            element(name("version"), info.version),
                            element(name("packaging"), info.packaging),
                            element(name("classifier"), info.classifier),
                            element(name("pomFile"), info.pomFile == null ? "" : info.pomFile.getAbsolutePath())
                    ),
                    executionEnvironment(
                            mavenProject,
                            mavenSession,
                            pluginManager
                    )
            );
            return true;
        } catch (MojoExecutionException ex) {
            return throwExceptionAndReturn(ex);
        }
    }

    public boolean deployArtifact(ArtifactInfo info, String repositoryId, String url) {
        if (repositoryId == null || repositoryId.trim().isEmpty())
            throw new NullPointerException("repositoryId could not be null");
        if (url == null || url.trim().isEmpty())
            throw new NullPointerException("url could not be null");
        if (info == null || info.source == null)
            return throwExceptionAndReturn(new FileNotFoundException("Source file should be present"));
        if (!info.source.isFile())
            return throwExceptionAndReturn(new FileNotFoundException("File not found: " + info.source.getAbsolutePath()));

        try {
            executeMojo(
                    plugin(
                            groupId("org.apache.maven.plugins"),
                            artifactId("maven-deploy-plugin"),
                            version("2.8.2")
                    ),
                    goal("deploy-file"),
                    configuration(
                            element(name("file"), info.source.getAbsolutePath()),
                            element(name("groupId"), info.groupId),
                            element(name("artifactId"), info.artifactId),
                            element(name("version"), info.version),
                            element(name("packaging"), info.packaging),
                            element(name("classifier"), info.classifier),
                            element(name("pomFile"), info.pomFile == null ? "" : info.pomFile.getAbsolutePath()),
                            element(name("repositoryId"), repositoryId),
                            element((name("url")), url)
                    ),
                    executionEnvironment(
                            mavenProject,
                            mavenSession,
                            pluginManager
                    )
            );
            return true;
        } catch (MojoExecutionException ex) {
            return throwExceptionAndReturn(ex);
        }
    }

    protected <T> T getContextData(String tag, Supplier<T> supplier) {
        return Opt.of(getProject().getContextValue(tag)).map(o -> (T) o).
                mapMissing(() -> with(supplier.get(), r -> getProject().setContextValue(tag, r))).get();
    }

    public DependencyItem getRootDependency(boolean shouldResolveRoot) {
        return getContextData(CM_PROJ_DEPS + "_RESOLVED_" + Boolean.toString(shouldResolveRoot).toUpperCase(),
                () -> DependencyDigger.getDependencyTree(getProject(), getSession(), getDependencyGraph(), this::resolveArtifact, shouldResolveRoot));
    }

    public MavenProject getProject() {
        return mavenProject;
    }

    public File getBuildDir() {
        return new File(getProject().getBuild().getDirectory());
    }

    public DependencyGraphBuilder getDependencyGraph() {
        return dependencyGraphBuilder;
    }

    public MavenSession getSession() {
        return mavenSession;
    }

    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }
}
