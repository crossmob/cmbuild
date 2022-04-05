/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.apache.maven.BuildFailureException;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.plugin.reg.Plugin;
import org.crossmobile.plugin.reg.PluginDependency;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.reg.ReverseCode;
import org.crossmobile.utils.CollectionUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.PluginMetaData;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.io.Writer;
import java.util.List;
import java.util.function.Consumer;

import static java.io.File.separator;
import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.build.utils.Locations.NATIVE_PATH;
import static org.crossmobile.build.utils.PlistUtils.isInclude;
import static org.crossmobile.plugin.actions.CreateLib.libLocation;
import static org.crossmobile.plugin.actions.PluginAssembler.*;
import static org.crossmobile.plugin.utils.Statics.*;
import static org.crossmobile.prefs.Config.REVERSE_INF;
import static org.crossmobile.utils.FileUtils.Predicates.extensions;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.JarUtils.createJar;
import static org.crossmobile.utils.PluginMetaData.PLUGIN_LOC;
import static org.crossmobile.utils.TextUtils.iterableToString;

public class CreateArtifacts {
    public CreateArtifacts() {
    }

    public static void installPlugin(Consumer<ArtifactInfo> installer,
                                     String plugin, File target, DependencyItem item, File cache, File vendorSrc, File vendorBin, ReverseCode rc,
                                     boolean buildSwing, boolean buildIos, boolean buildUwp, boolean buildAndroid, boolean buildRvm, boolean buildCore,
                                     Writer report, Registry reg) {
        // Get plugin data
        Plugin pluginData = reg.plugins().getPluginData(plugin);

        // Define targets
        File compileTarget = compileBase.apply(target, plugin);
        File sourcesTarget = sourcesBase.apply(target, plugin);
        File iosTarget = iosBase.apply(target, plugin);
        File swingTarget = swingBase.apply(target, plugin);
        File uwpTarget = uwpBase.apply(target, plugin);
        File androidTarget = androidBase.apply(target, plugin);
        File rvmTarget = rvmBase.apply(target, plugin);

        PluginMetaData meta = new PluginMetaData(pluginData.getLibs(), pluginData.getPermissions(), pluginData.getPods(), pluginData.getInjections(), pluginData.getInitializer(), pluginData.getAndroidExtraDependencies());
        Log.debug("Meta data for " + plugin + ": " + meta.toString());
        copy(write(new File(compileTarget, PLUGIN_LOC), meta.getProperties("Plugin " + plugin)),
                new File(cache, plugin + File.separator + "plugin.txt"));

        if (buildIos || buildUwp) {
            Log.debug("Back references for " + plugin + ": " + iterableToString(rc.getListOfClasses(plugin), ";"));
            copy(write(new File(compileTarget, REVERSE_INF), rc.toString(plugin)),
                    new File(cache, plugin + File.separator + "reverse.txt"));
            Log.debug("Installing native files of plugin " + plugin);

            if (buildIos) {
                File lib = libLocation.apply(cache, plugin, CreateLib.iOSTarget);
                if (copy(lib, new File(iosTarget, NATIVE_PATH + separator + plugin + ".a")) == 0)
                    if (pluginData.hasOptionalLibraryBinary())
                        Log.info("Native library not found but ignored as noted: " + lib.getAbsolutePath());
                    else
                        Log.error("Unable to copy native library " + lib.getAbsolutePath());
                listFiles(new File(vendorBin, plugin)).stream().filter(extensions(".a"))
                        .forEach(f -> copy(f, new File(iosTarget, NATIVE_PATH + separator + f.getName())));
            }
            if (buildUwp) {
                File dll = libLocation.apply(cache, plugin, CreateLib.uwpTarget);
                if (copy(dll, new File(uwpTarget, NATIVE_PATH + separator + plugin + ".dll")) == 0)
                    if (pluginData.hasOptionalLibraryBinary())
                        Log.info("Native library not found but ignored as noted: " + dll.getAbsolutePath());
                    else
                        Log.error("Unable to copy native library " + dll.getAbsolutePath());
                listFiles(new File(vendorBin, plugin)).stream().filter(extensions(".dll"))
                        .forEach(f -> copy(f, new File(iosTarget, NATIVE_PATH + separator + f.getName())));
            }
            listFiles(new File(cache, plugin + separator + (buildIos ? "native" : "uwp" + separator + "uwpinclude"))).stream().filter(f -> isInclude(f.getName()))
                    .forEach(f -> copy(f, new File(buildIos ? iosTarget : uwpTarget, NATIVE_PATH + separator + f.getName())));
            listFiles(new File(vendorSrc, plugin + (buildIos ? "" : (separator + "uwpinclude")))).stream().filter(f -> isInclude(f.getName()))
                    .forEach(f -> copy(f, new File(buildIos ? iosTarget : uwpTarget, NATIVE_PATH + separator + f.getName())));
        }

        Log.info("Installing plugin " + plugin + " artifacts in local repository");
        File artBase = new File(target, ARTIFACTS);
        for (PluginDependency dep : reg.plugins().getPluginData(plugin).getDependencies())
            Log.info("Found dependency " + dep.info() + " with targets " + dep.target().listTargets());
        if (buildCore) {
            install(installer, createJar(report, new File(artBase, "cmplugin-" + plugin + '-' + item.getVersion() + ".jar"), compileTarget),
                    plugin, "", item.getGroupID(), item.getVersion(), reg);
            installer.accept(new ArtifactInfo(createJar(report, new File(artBase, "cmplugin-" + plugin + '-' + item.getVersion() + "-sources.jar"), sourcesTarget),
                    item.getGroupID(), "cmplugin-" + plugin, item.getVersion(), "jar", "sources", null));
        }
        if (buildIos)
            install(installer, createJar(report, new File(artBase, "cmplugin-ios-" + plugin + "-" + item.getVersion() + ".jar"), iosTarget),
                    plugin, "ios-", item.getGroupID(), item.getVersion(), reg);
        if (buildUwp)
            install(installer, createJar(report, new File(artBase, "cmplugin-uwp-" + plugin + "-" + item.getVersion() + ".jar"), uwpTarget),
                    plugin, "uwp-", item.getGroupID(), item.getVersion(), reg);
        if (buildSwing)
            install(installer, createJar(report, new File(artBase, "cmplugin-swing-" + plugin + "-" + item.getVersion() + ".jar"), swingTarget),
                    plugin, "swing-", item.getGroupID(), item.getVersion(), reg);
        if (buildAndroid)
            install(installer, createJar(report, new File(artBase, "cmplugin-android-" + plugin + "-" + item.getVersion() + ".jar"), androidTarget),
                    plugin, "android-", item.getGroupID(), item.getVersion(), reg);
        if (buildRvm)
            install(installer, createJar(report, new File(artBase, "cmplugin-rvm-" + plugin + "-" + item.getVersion() + ".jar"), rvmTarget),
                    plugin, "rvm-", item.getGroupID(), item.getVersion(), reg);
        Log.debug("Installing of plugin " + plugin + " terminated");
    }

    private static void install(Consumer<ArtifactInfo> installer, File fileToInstall, String plugin, String type, String groupid, String version, Registry reg) {
        if (fileToInstall == null)
            throw new NullPointerException("Unable to install null file");
        String packaging = getExtension(fileToInstall.getName());
        String artifactid = "cmplugin-" + type + plugin;

        StringBuilder deplist = new StringBuilder();
        for (PluginDependency dep : reg.plugins().getPluginData(plugin).getDependencies())
            if (checkTarget(dep, type))
                addDependency(dep, groupid, type, version, packaging, deplist);

        String dependencies = deplist.length() > 0 ? "    <dependencies>\n" + deplist.toString() + "    </dependencies>\n" : "";

        String pomData = POM_XML.
                replace(POM_GROUPID, groupid).
                replace(POM_ARTIFACTID, artifactid).
                replace(POM_VERSION, version).
                replace(POM_PACKAGING, packaging).
                replace(POM_DEPENDENCIES, dependencies);
        File pomFile = new File(fileToInstall.getParent(), artifactid + "-" + version + ".pom");
        write(pomFile, pomData);
        installer.accept(new ArtifactInfo(fileToInstall, groupid, artifactid, version, "", pomFile));
    }

    private static void addDependency(PluginDependency dep, String groupid, String type, String version, String packaging, StringBuilder deplist) {
        addDependency(
                dep.groupId().trim().isEmpty() ? groupid : dep.groupId().trim(),
                (dep.isCMPlugin() ? "cmplugin-" + type : "") + dep.pluginName().trim(),
                dep.version().trim().isEmpty() ? version :
                        (dep.isCMPlugin() && !dep.version().trim().equals(version)) ? "[" + dep.version().trim() + ",)" : dep.version().trim(),
                dep.type().trim().isEmpty() ? packaging : dep.type().trim(),
                deplist);
    }

    private static void addDependency(String groupid, String artifactid, String version, String type, StringBuilder deplist) {
        deplist.append("        <dependency>\n").
                append("            <groupId>").append(groupid).append("</groupId>\n").
                append("            <artifactId>").append(artifactid).append("</artifactId>\n").
                append("            <version>").append(version).append("</version>\n").
                append("            <type>").append(type).append("</type>\n").
                append("        </dependency>\n");
    }

    private static boolean checkTarget(PluginDependency dep, String type) {
        switch (type) {
            case "ios-":
                return dep.target().iosjava;
            case "android-":
                if (!dep.isCMPlugin())
                    return false;
                return dep.target().android;
            case "swing-":
                return dep.target().swing;
            case "uwp-":
                return dep.target().uwpjava;
            default:
                return false;
        }
    }

    public static void installJavadoc(Consumer<ArtifactInfo> installer, DependencyItem root, Registry reg) {
        Plugin pluginData = reg.plugins().getPluginData(root.getArtifactID());
        String baseName = pluginData == null ? "" : pluginData.getName();
        if (baseName == null) {
            List<String> plugins = CollectionUtils.asList(reg.plugins().plugins());
            if (plugins.size() > 1)
                BaseUtils.throwException(new BuildFailureException("Unable to locate main plugin data, more then one plugin found and none of them does not have the same name as the original module"));
            else if (plugins.isEmpty())
                BaseUtils.throwException(new BuildFailureException("No plugins defined"));
            baseName = plugins.get(0);
        }
        String filename = baseName + "-" + root.getVersion() + "-javadoc.jar";
        File javadocFile = new File(root.getFile().getParent(), filename);
        if (javadocFile.isFile())
            installer.accept(new ArtifactInfo(javadocFile, root.getGroupID(), "cmplugin-" + baseName, root.getVersion(), "javadoc", null));
    }
}
