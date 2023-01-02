/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ng;

import org.crossmobile.build.AnnotationConfig;
import org.crossmobile.build.ib.helper.XIBList;
import org.crossmobile.build.tools.*;
import org.crossmobile.build.tools.images.IconBuilder;
import org.crossmobile.build.tools.images.IconBuilder.IconType;
import org.crossmobile.build.tools.images.IosIconRegistry;
import org.crossmobile.build.xcode.XcodeTargetRegistry;
import org.crossmobile.build.xcode.resources.ObjCLibrary;
import org.crossmobile.build.xcode.resources.ResourceItem;
import org.crossmobile.utils.*;
import org.crossmobile.utils.launcher.Flavour;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static java.io.File.separator;
import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.build.AnnotationConfig.ANN_LOCATION;
import static org.crossmobile.build.ng.CMBuildEnvironment.environment;
import static org.crossmobile.build.tools.IBObjectsCreator.getXibFiles;
import static org.crossmobile.build.tools.InfoPListCreator.getPlist;
import static org.crossmobile.build.tools.ObjCPostProcess.updateObjCFiles;
import static org.crossmobile.build.tools.SynchronizeFiles.syncChangedObjCFiles;
import static org.crossmobile.build.tools.SynchronizeFiles.synchronizeChangedJavaFiles;
import static org.crossmobile.build.tools.XMLVMConverter.convertJavaToObjC;
import static org.crossmobile.build.utils.Config.*;
import static org.crossmobile.build.utils.DependencyJarResolver.gatherLibs;
import static org.crossmobile.build.utils.JavaPostProcess.convertToJava7;
import static org.crossmobile.build.utils.JavaPostProcess.extractLibClassesFromPlugins;
import static org.crossmobile.prefs.Config.MATERIALS_PATH;
import static org.crossmobile.utils.CollectionUtils.asList;
import static org.crossmobile.utils.ParamsCommon.*;

public class PostCompilePipeline implements Runnable {

    @Override
    public void run() {
        if (environment().getFlavour() == Flavour.IOS)
            postCompileIOS();
    }

    private void postCompileUwp() {
        CMBuildEnvironment env = environment();
        File ann = new File(env.getBuilddir(), AnnotationConfig.ANN_LOCATION);
        String projectName = env.getProperties().getProperty(ARTIFACT_ID.tag().name);

        File xcodeLibs = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_LIB);
        File xcodeSource = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_APP);
        File xcodeInclude = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_INC);
        File plistDir = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_PLIST);

        File classesDir = new File(env.getBuilddir(), CLASSES);
        File cacheBase = new File(env.getBuilddir(), PROJECT_CACHES);
        File cachedClasses = new File(cacheBase, "classes");
        File diffClasses = new File(cacheBase, "diff");
        File cacheSuper = new File(cacheBase, "super");
        File cacheSource = new File(cacheBase, XCODE_EXT_APP);

        // Post process Java classes
        convertToJava7(env.getRetroLambda(), classesDir, gatherLibs(env.root(), false), false);
        extractLibClassesFromPlugins(classesDir, env.root());

        GenerateScreenSizeSettings.exec(xcodeSource, env.getProperties());

        // Construct ObjC files
        if (synchronizeChangedJavaFiles(classesDir, cachedClasses, diffClasses)) {
            convertJavaToObjC(diffClasses, cacheSource, env.getXMLVM(), Boolean.parseBoolean(env.getProperties().getProperty("cm.objc.safemembers", "true")));
            Collection<String> allObjCFiles = updateObjCFiles(cacheSource, cacheSuper, diffClasses, classesDir,
                    getReverseCodeCollection(env, cachedClasses),
                    XcodeTargetRegistry.gatherTargets(env, new File(env.getBuilddir(), ANN_LOCATION)),
                    env.getProperties().getProperty(OBJC_IGNORE_INCLUDES.tag().name));
            syncChangedObjCFiles(allObjCFiles, cacheSource, xcodeSource);
        }

        // Extract binary libraries and include files
        ObjCLibrary objCLibrary = ObjCLibrary.extract(env.root(), xcodeLibs, xcodeInclude, env.getRelativeBuildToBase());
        Log.debug("Xcode Lib List : " + objCLibrary.getLibraries());

        //        <!-- create Xcode project -->
        Map<String, File> xmfontsValue = FontExtractor.findFonts(env.getMaterialsDir());

        // Parse IB to check for errors & update localizations
        Collection<File> xibs = getXibFiles(env.getMaterialsDir(), null);
        if (xibs != null) {
            XIBList xibList = IBObjectsCreator.parse(env.getMaterialsDir(), xibs, ann);
            MaterialsManager.parseMaterials(xibList.getMeta(), env.getMaterialsDir(), null);
        }

        // Create Info.plist files
        new InfoPListCreator(env.getProperties(),
                getPlist(plistDir, projectName),
                xmfontsValue.keySet(),
                env.getProperties().getProperty(INJECTED_INFOPLIST.tag().name),
                env.getBasedir()).execute(env);
        for (XcodeTarget target : XcodeTargetRegistry.getExtraTargets())
            InfoPListCreator.createExtensionPlist(plistDir, env.getAppId(), target);

        XCodeProject xCodeProject = new XCodeProject(projectName, plistDir, env.getProperties().getProperty(CM_PROJECT.tag().name, ""), env.getBasedir());
        xCodeProject.addConfiguredResource(new ResourceItem("Application", env.getBuilddir() + separator + XCODE_BASE + separator + XCODE_EXT_APP + separator));
        xCodeProject.addConfiguredResource(new ResourceItem("Materials", MATERIALS_PATH));
        xCodeProject.setConfiguredLibrary(objCLibrary);
        xCodeProject.execute();
        Commander cmd = new Commander("vsimporter.exe");
        cmd.setCurrentDir(env.getBasedir());
        cmd.setOutListener(Log::info);
        cmd.setErrListener(Log::warning);
        cmd.exec();
        cmd.waitFor();

        // tweak project
        File project = new File(env.getArtifactId() + ".vsimporter" + separator
                + env.getArtifactId() + "-WinStore10" + separator
                + env.getArtifactId() + ".vcxproj");
        XMLWalker walker = XMLWalker.load(project);
        if (walker == null)
            throw new NullPointerException("Unable to locate project at location " + project.getAbsolutePath());
        walker.path("/Project").tag("root").node("ItemGroup").exec(ig -> {
            for (File lib : listFiles(xcodeLibs))
                if (lib.getName().endsWith(".dll"))
                    ig.add("None").setAttribute("Include", lib.getPath()).add("DeploymentContent").setText("true").parent().parent();
        }).toTag("root").nodes("ItemDefinitionGroup",
                idg -> idg.node("Link").add("AdditionalLibraryDirectories").setText(".." + separator + ".." + separator + "target" + separator + "xcode" + separator + "plugins" + separator + "lib"));
        walker.store(project, true);
    }


    private void postCompileIOS() {
        CMBuildEnvironment env = environment();
        File ann = new File(env.getBuilddir(), AnnotationConfig.ANN_LOCATION);
        String projectName = env.getProperties().getProperty(ARTIFACT_ID.tag().name);

        File xcodeLibs = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_LIB);
        File xcodeSource = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_APP);
        File xcodeResources = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_MAT);
        File xcodeInclude = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_INC);
        File plistDir = new File(env.getBuilddir(), XCODE_BASE + separator + XCODE_EXT_PLIST);

        File allClasses = new File(env.getBuilddir(), CLASSES);

        File cacheBase = new File(env.getBuilddir(), PROJECT_CACHES);
        File cachedClasses = new File(cacheBase, "classes");
        File diffClasses = new File(cacheBase, "diff");
        File cacheSuper = new File(cacheBase, "super");
        File cacheSource = new File(cacheBase, XCODE_EXT_APP);

        // Post process Java classes
        convertToJava7(env.getRetroLambda(), allClasses, gatherLibs(env.root(), false), false);
        extractLibClassesFromPlugins(allClasses, env.root());

        // Construct ObjC files
        if (synchronizeChangedJavaFiles(allClasses, cachedClasses, diffClasses)) {
            convertJavaToObjC(diffClasses, cacheSource, env.getXMLVM(), Boolean.parseBoolean(env.getProperties().getProperty("cm.objc.safemembers", "true")));
            Collection<String> allObjCFiles = updateObjCFiles(cacheSource, cacheSuper, diffClasses, allClasses,
                    getReverseCodeCollection(env, cachedClasses),
                    XcodeTargetRegistry.gatherTargets(env, new File(env.getBuilddir(), ANN_LOCATION)),
                    env.getProperties().getProperty(OBJC_IGNORE_INCLUDES.tag().name));
            syncChangedObjCFiles(allObjCFiles, cacheSource, xcodeSource);
        }

        // Extract binary libraries and include files
        ObjCLibrary objCLibrary = ObjCLibrary.extract(env.root(), xcodeLibs, xcodeInclude, env.getRelativeBuildToBase());
        Log.debug("Xcode Lib List : " + objCLibrary.getLibraries());

        //        <!-- create Xcode project -->
        Map<String, File> xmfontsValue = FontExtractor.findFonts(env.getMaterialsDir());

        // Parse IB to check for errors & update localizations
        Collection<File> xibs = getXibFiles(env.getMaterialsDir(), null);
        if (xibs != null) {
            XIBList xibList = IBObjectsCreator.parse(env.getMaterialsDir(), xibs, ann);
            MaterialsManager.parseMaterials(xibList.getMeta(), env.getMaterialsDir(), null);
        }

        // Create Info.plist files
        new InfoPListCreator(env.getProperties(),
                getPlist(plistDir, projectName),
                xmfontsValue.keySet(),
                env.getProperties().getProperty(INJECTED_INFOPLIST.tag().name),
                env.getBasedir()).execute(env);
        for (XcodeTarget target : XcodeTargetRegistry.getExtraTargets())
            InfoPListCreator.createExtensionPlist(plistDir, env.getAppId(), target);

        File appIconSet = new File(xcodeResources, "CrossImages.xcassets" + separator + "CrossIcon.appIconSet");
        IconBuilder.copyIcons(IconBuilder.getDefaultHound(env.getBasedir()), appIconSet, IconType.valueOf(env.getProperties().getProperty("cm.project").toUpperCase()));
        IosIconRegistry.exec(appIconSet);
        XCodeProject xCodeProject = new XCodeProject(projectName, plistDir, env.getProperties().getProperty(CM_PROJECT.tag().name, ""), env.getBasedir());
        xCodeProject.addConfiguredResource(new ResourceItem("Application", env.getRelativeBuildToBase() + XCODE_BASE + separator + XCODE_EXT_APP + separator));
        xCodeProject.addConfiguredResource(new ResourceItem("Materials", MATERIALS_PATH, xcodeResources.getPath() + separator));
        xCodeProject.setConfiguredLibrary(objCLibrary);
        xCodeProject.execute();

        if (!objCLibrary.getPods().isEmpty())
            PluginPod.create(env.getBasedir(), projectName, XcodeTargetRegistry.getAllTargets(), objCLibrary.getPods());
    }

    private ReverseCodeCollection getReverseCodeCollection(CMBuildEnvironment env, File diffClasses) {
        Collection<File> classPaths = new ArrayList<>();
        classPaths.add(diffClasses);
        classPaths.addAll(asList(env.root().getCompileOnlyDependencies(true), DependencyItem::getFile));
        return new ReverseCodeCollection(classPaths);
    }
}
