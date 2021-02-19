/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.bridge.ann.CMLibTarget.BaseTarget;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.plugin.Packages;
import org.crossmobile.plugin.reg.PluginRegistryFile;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ReflectionUtils;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.io.File.separator;
import static org.crossmobile.plugin.actions.CreateBeanAPI.OBJ_STYLE;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.JarUtils.explodeClasspath;
import static org.crossmobile.utils.TextUtils.plural;
import static org.crossmobile.utils.TimeUtils.time;
import static org.crossmobile.utils.func.ScopeUtils.with;

public class PluginAssembler {
    public static final String BUNDLES = "bundles";
    public static final String ARTIFACTS = "artifacts";

    public static final BiFunction<File, String, File> compileBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "compile");
    public static final BiFunction<File, String, File> sourcesBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "sources");
    public static final BiFunction<File, String, File> swingBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "swing");
    public static final BiFunction<File, String, File> avianBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "avian");
    public static final BiFunction<File, String, File> androidBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "android");
    public static final BiFunction<File, String, File> iosBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "ios");
    public static final BiFunction<File, String, File> uwpBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "uwp");
    public static final BiFunction<File, String, File> rvmBase = (target, plugin) -> new File(target, BUNDLES + separator + plugin + separator + "rvm");

    private static final byte SOURCE_TYPE = OBJ_STYLE;

    public static void assembleFiles(Registry reg, File target, DependencyItem root,
                                     String[] embedlibs, File srcDir, File vendorSrc, File vendorBin,
                                     Function<ArtifactInfo, File> resolver,
                                     File cachedir, Packages[] packs,
                                     boolean buildSwing, boolean buildAvian, boolean buildIos, boolean buildAndroid, boolean buildUwp, boolean buildRvm,
                                     File VStudioLocation, PluginRegistryFile pluginRegistry) {
        File runtime = new File(target, "runtime");

        long sourcesModified = Math.max(Math.max(getLastModified(vendorBin), getLastModified(vendorSrc)), getLastModified(srcDir));
        long artifactsProduced = getLastModified(new File(target, ARTIFACTS));
        if (sourcesModified < artifactsProduced) {
            Log.info("No files to process");
            return;
        }

        File bundles = new File(target, BUNDLES);
        delete(bundles);

        ReflectionUtils.resetClassLoader();
        ProjectRegistry registry = new ProjectRegistry();
        time("API processing", () -> {
            time("Initialize classes", () -> {
                reg.packages().register("META-INF", "none", "SOURCEONLY");
                if (packs != null && packs.length > 0)
                    for (Packages pack : packs)
                        if (pack != null)
                            reg.packages().register(pack.getName(), pack.getPlugin(), pack.getTarget());
                registry.register(root, embedlibs, reg.getClassCollection(), reg);
                if (!reg.plugins().pluginExists(root.getArtifactID()))
                    Log.error("Required plugin " + root.getArtifactID() + " not defined˝");
            });
            time("Gather iOS native API", () -> with(new Parser(reg),
                    p -> (buildIos ? reg.getClassCollection().getIOsNativeClasses() : reg.getClassCollection().getUWPNativeClasses()).forEach(p::parse)));
            time("Create native bindings", ()->{

                System.exit(1);
            });
        });

        if (buildIos || buildSwing || buildAvian || buildUwp || buildAndroid)
            time("Extract embedded jars", () -> {
                for (File f : registry.getAppjars().toArray(new File[0]))
                    explodeClasspath(f, runtime);
            });

        if (buildIos || buildUwp) {
//            time(() -> new JavaTransformer(cc.getClassPool(), runtime_rvm));
            time("Create reverse code", () -> reg.reverse().produce());
            // do not use if (buildIos), since we always need to create the source files. The optimization is embedded in the function itself
            time("Create iOS libraries", () -> new CreateDylib(resolver, target, cachedir, vendorSrc, null, reg, buildIos));
            if (buildUwp)
                time("Create UWP libraries", () -> new CreateDll(resolver, target, cachedir, vendorSrc, VStudioLocation, reg, buildUwp));
        }

        time("Initialize and create stub compile-time files", () -> {
            for (String plugin : reg.plugins().plugins()) {
                mkdirs(compileBase.apply(target, plugin));
                mkdirs(sourcesBase.apply(target, plugin));
                if (buildSwing)
                    mkdirs(swingBase.apply(target, plugin));
                if (buildAvian)
                    mkdirs(avianBase.apply(target, plugin));
                if (buildAndroid)
                    mkdirs(androidBase.apply(target, plugin));
                if (buildIos)
                    mkdirs(iosBase.apply(target, plugin));
                if (buildUwp)
                    mkdirs(uwpBase.apply(target, plugin));
                if (buildRvm)
                    mkdirs(rvmBase.apply(target, plugin));
            }
            XMLPluginWriter.storeForPlugin(pluginRegistry, root, reg);

            CreateSkeleton skel = new CreateSkeleton(reg.getClassCollection().getClassPool());
            int hm = 0;
            for (Class<?> cls : reg.getClassCollection().getCompileTimeClasses())
                hm += skel.stripClass(cls, plugin -> compileBase.apply(target, plugin), reg, SOURCE_TYPE) ? 1 : 0;
            // Still might need to add extra resource files
            CreateBundles.bundleFilesAndReport(runtime, plugin -> compileBase.apply(target, plugin), CreateBundles.getNoClassResolver(reg), BaseTarget.COMPILE);
            Log.debug(hm + " class" + plural(hm, "es") + " stripped");
        });
    }

    public static void packageFiles(Registry reg, File target, File srcDir,
                                    boolean buildSwing, boolean buildAvian, boolean buildIos, boolean buildAndroid, boolean buildUwp, boolean buildRvm) {
        File runtime = new File(target, "runtime");
        File runtime_rvm = new File(target, "runtime_rvm");

        time("Create distributions of artifacts", () -> {
            if (buildSwing)
                CreateBundles.bundleFiles(runtime, plugin -> swingBase.apply(target, plugin), CreateBundles.classResolver(reg), BaseTarget.SWING);
            if (buildAvian)
                CreateBundles.bundleFiles(runtime, plugin -> avianBase.apply(target, plugin), CreateBundles.classResolver(reg), BaseTarget.AVIAN);
            if (buildRvm)
                CreateBundles.bundleFiles(runtime_rvm, plugin -> rvmBase.apply(target, plugin), CreateBundles.classResolver(reg), BaseTarget.IOS);
            if (buildUwp)
                CreateBundles.bundleFiles(runtime, plugin -> uwpBase.apply(target, plugin), CreateBundles.classResolver(reg), BaseTarget.UWP);
            if (buildAndroid)
                CreateBundles.bundleFiles(runtime, plugin -> androidBase.apply(target, plugin), CreateBundles.classResolver(reg), BaseTarget.ANDROID);
            CreateBundles.bundleFiles(srcDir, plugin -> sourcesBase.apply(target, plugin), CreateBundles.sourceResolver(reg), BaseTarget.ALL);
            /*
             * iOS target does not support gathering classes and other resource files, so files are not copied.
             * If we want in the future to add resource files (which is not supported yet), we should
             *   (a) annotate the packages (not the files!) accordingly
             *   (b) copy all files BUT .classes. Right now the algorithm accepts all files, *including* classes.
             * Follows commented the old code that gathers everything (including .class):
             * if (buildIos) CreateBundles.bundleFiles(runtime, plugin -> iosBase.apply(target, plugin), CreateBundles.classResolver(reg), BaseTarget.IOS);
             */
        });
    }

    public static void installFiles(Registry reg, File target, DependencyItem root,
                                    Consumer<ArtifactInfo> installer,
                                    File vendorSrc, File vendorBin, File cachedir,
                                    boolean buildSwing, boolean buildAvian, boolean buildIos, boolean buildAndroid, boolean buildUwp, boolean buildRvm, boolean buildCore,
                                    File report
    ) {
        time("Install artifacts", () -> {
            StringWriter writer = report == null ? null : new StringWriter();
            for (String plugin : reg.plugins().plugins())
                CreateArtifacts.installPlugin(installer, plugin, target, root, cachedir, vendorSrc, vendorBin, reg.reverse(),
                        buildSwing, buildAvian, buildIos, buildUwp, buildAndroid, buildRvm, buildCore, writer, reg);
            CreateArtifacts.installJavadoc(installer, root, reg);
            report.getParentFile().mkdirs();
            if (writer != null)
                try (OutputStreamWriter filewriter = new OutputStreamWriter(new FileOutputStream(report), StandardCharsets.UTF_8)) {
                    filewriter.write(writer.toString().replaceAll(root.getVersion(), "<VERSION>"));
                    filewriter.flush();
                } catch (Exception e) {
                    Log.error("Unable to store reports.txt", e);
                }
        });
    }
}
