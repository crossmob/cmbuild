/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.objc.ReverseImportRegistry;
import org.crossmobile.plugin.objc.ObjectEmitter;
import org.crossmobile.plugin.reg.*;
import org.crossmobile.plugin.utils.Streamer;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.JarUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.reqgraph.Requirement;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.jar.JarFile;

import static java.io.File.separator;
import static java.util.stream.Collectors.toCollection;
import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.build.utils.PlistUtils.isCompilable;
import static org.crossmobile.build.utils.PlistUtils.isInclude;
import static org.crossmobile.plugin.reg.PluginRegistry.*;
import static org.crossmobile.plugin.utils.Streamer.asBody;
import static org.crossmobile.plugin.utils.Streamer.asHeader;
import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.TextUtils.plural;
import static org.crossmobile.utils.TimeUtils.time;

public abstract class CreateLib {

    protected static final String LIB_ANCHOR = "CMPLUGIN";
    protected static final Function<Boolean, String> PLATFORM = asIOS -> asIOS ? "native" : "uwp";
    private static final Function<String, String> IOSLibName = l -> "lib" + l + ".a";
    private static final Function<String, String> UWPLibName = l -> l + ".dll";

    public CreateLib(Function<ArtifactInfo, File> resolver, File target, File cache, File vendor, File IDELocation, ReverseImportRegistry handleRegistry, boolean asIOS, boolean build) throws IOException {
        // Create native files in the scratch folder
        Function<String, File> prodResolv = plugin -> new File(target, PLATFORM.apply(asIOS) + separator + plugin);
        final Function<String, String> LIBNAME = asIOS ? IOSLibName : UWPLibName;

        time("Creating " + PLATFORM.apply(asIOS) + " files", () -> {
            for (String plugin : plugins())
                delete(prodResolv.apply(plugin));
            runEmitters(prodResolv);
        });

        time("Creating inverse block handlers", () -> handleRegistry.saveTo(prodResolv));

        time("Synchronizing plugins", () -> {
            Requirement<Plugin> root = new Requirement<>(new Plugin("root", false));
            for (Plugin p : pluginsData())
                root.setRequires(p.getRootRequirement());

            Map<String, Collection<File>> pluginIncludes = new HashMap<>();

            //Sibling plugin Create iteration
            root.listRequirements().stream().filter(r -> r != root).map(Requirement::getData).forEach(pData -> {
                String plugin = pData.getName();
                File prod = prodResolv.apply(plugin);
                if (!prod.exists())
                    //noinspection ResultOfMethodCallIgnored
                    prod.mkdirs();
                File cached = new File(cache, plugin + separator + PLATFORM.apply(asIOS));
                File lib = new File(cache, "lib" + separator + LIBNAME.apply(plugin));
                File vendorFiles = new File(vendor, plugin);
                int howMany = sync(prod, cached, null, true, f -> !f.getName().equals("patches"));
                if (howMany == 0)
                    Log.debug("All source files are in sync for plugin " + plugin);
                else
                    Log.info("Updated " + howMany + " file" + plural(howMany) + " for plugin " + plugin);

                // Need to define these anyways, so that dependencies will work
                Collection<File> compiled = getCompiled(cached, vendorFiles, prod);

                //Adding own include dirs to handle later
                Collection<File> includeDir = new LinkedHashSet<>();
                includeDir.add(cached);
                includeDir.add(vendorFiles);

                Collection<File> headerSearchPaths = new LinkedHashSet<>();
                //Adding external plugins include directories (external = not sibling)
                headerSearchPaths.add(external(pData, resolver, asIOS, target));


                pluginIncludes.put(plugin, Arrays.asList(cached, vendorFiles));  // store include paths for possible future usage

                if ((!lib.exists() || getLastModified(cached) > lib.lastModified() || getLastModified(vendorFiles) > lib.lastModified())
                        && listFiles(prod).stream().anyMatch(f -> f.getName().endsWith(".h"))) {
                    Log.debug("Compiling native plugin " + plugin);
                    Collection<String> requirements = new HashSet<>();
                    pData.getRootRequirement().listRequirements().stream()
                            .filter(p -> p.getData() != pData)
                            .map(p -> p.getData().getName())
                            .forEach(requirements::add);
                    requirements.forEach(p -> {
                        // Add plugin dependencies include files
                        Collection<File> depfiles = pluginIncludes.get(p);
                        if (depfiles == null)
                            Log.error("Unable to retrieve required dependency files of plugin " + p + " required by " + plugin);
                        else
                            headerSearchPaths.addAll(depfiles);
                    });
                    File proj = createProj(target, plugin);
                    updateProj(proj, pData, includeDir, headerSearchPaths, compiled, requirements);    // Update project with project files and includes
                    if (build)
                        compile(proj, asIOS ? lib : IDELocation, pData, LIBNAME.apply(plugin));
                } else
                    Log.debug("Plugin " + plugin + " is in sync with native cache files");
            });
        });
    }

    protected static void emitPlatformFiles(Function<String, File> prodResolv, boolean asIOS) throws IOException {
        objectEmitter(prodResolv, false);
    }

    protected static void objectEmitter(Function<String, File> prodResolv, boolean headersOnly) throws IOException {
        for (NObject obj : ObjectRegistry.retrieveAll()) {
            ObjectEmitter out = new ObjectEmitter(obj);
            String name = toObjC(obj.getType());
            String plugin = getPlugin(obj.getType().getName());
            if (plugin != null) {
                File pluginRoot = prodResolv.apply(plugin + (headersOnly ? separator + "uwpinclude" : ""));
                out.emitAndTerminate(asHeader(pluginRoot, name), headersOnly ? null : asBody(pluginRoot, name), headersOnly, PluginRegistry.getPluginData(plugin).getImports());
            }
        }
//        for (String plugin : swift.keySet()) {
//            Streamer streamer = swift.get(plugin);
//            String data = streamer.toString();
//            if (data.isEmpty())
//                continue;
//            File swiftFile = new File(prodResolv.apply(plugin), "swift_bindings.swift");
//            String swiftContent = "import Foundation\n@objc\nclass " +
//                    plugin + "_swift :NSObject {\n" +
//                    data +
//                    "}\n";
//            FileUtils.write(swiftFile, swiftContent);
//        }
    }

    private File external(Plugin pData, Function<ArtifactInfo, File> resolver, boolean asIOS, File target) {
        File out = new File(target, "lib" + separator + PLATFORM.apply(asIOS));
        File f;
        for (PluginDependency dep : pData.getDependencies())
            if (!isSibling(dep) && ((dep.target().iosnative && asIOS) || (dep.target().uwpnative && !asIOS))) {
                String groupid = dep.isCMPlugin() ? "org.crossmobile" : dep.groupId();
                String artifactid = "cmplugin-" + (asIOS ? "ios-" : "uwp-") + dep.pluginName();
                ArtifactInfo ai = new ArtifactInfo(groupid, artifactid, dep.version(), dep.type().isEmpty() ? "jar" : dep.type());
                try {
                    if ((f = resolver.apply(ai)) != null)
                        JarUtils.copyJarResources(new JarFile(f), PLATFORM.apply(asIOS), out, entry -> isInclude(entry.name));
                } catch (Exception e) {
                    Log.warning("🐔\uD83D\uDD04🥚 Could not find artifact " + ai.toString());
//                    BaseUtils.throwException(e);
                }
            }
        return out;
    }

    private boolean isSibling(PluginDependency dep) {
        for (String plugin : plugins())
            if (dep.pluginName().equals(plugin))
                return true;
        return false;
    }

    protected Collection<File> getCompiled(File cached, File vendorFiles, File src) {
        Collection<File> compiled = listFiles(cached).stream().filter(f -> isCompilable(f.getName())).collect(toCollection(LinkedHashSet::new));
        listFiles(vendorFiles).stream().filter(f -> isCompilable(f.getName())).forEach(compiled::add);
        return compiled;
    }

    protected abstract void runEmitters(Function<String, File> prodResolv) throws IOException;

    protected abstract void compile(File projRoot, File lib, Plugin plugin, String libname);

    protected abstract void updateProj(File proj, Plugin plugin, Collection<File> includeDir, Collection<File> headerSearchPaths, Collection<File> compiled, Collection<String> deps);

    protected abstract File createProj(File target, String plugin);


}
