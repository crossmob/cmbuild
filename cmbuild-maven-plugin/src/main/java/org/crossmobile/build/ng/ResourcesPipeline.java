/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ng;

import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.build.AnnotationConfig;
import org.crossmobile.build.exec.android.AdbUtils;
import org.crossmobile.build.ib.helper.XIBList;
import org.crossmobile.build.ib.i18n.IBParserMeta;
import org.crossmobile.build.tools.*;
import org.crossmobile.build.tools.images.IconBuilder;
import org.crossmobile.build.tools.images.IconBuilder.IconType;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ParamList;
import org.crossmobile.utils.images.ImageHound;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Collection;

import static org.crossmobile.backend.desktop.DesktopLocations.FONT_LIST;
import static org.crossmobile.bridge.system.RuntimeCommons.CROSSMOBILE_PROPERTIES;
import static org.crossmobile.build.ng.CMBuildEnvironment.environment;
import static org.crossmobile.build.utils.Config.*;
import static org.crossmobile.utils.CollectionUtils.asList;
import static org.crossmobile.utils.FileUtils.delete;
import static org.crossmobile.utils.FileUtils.write;
import static org.crossmobile.utils.ParamsCommon.*;
import static org.crossmobile.utils.TemplateUtils.copyTemplateIfMissing;

public class ResourcesPipeline implements Runnable {

    @Override
    public void run() {
        Log.debug("Runtime Name: " + ManagementFactory.getRuntimeMXBean().getName());
        switch (environment().getFlavour()) {
            case IOS:
                resourcesIOS();
                break;
            case ANDROID:
                resourcesAndroid();
                break;
            case SWING:
                resourcesSwing();
                break;
            case AROMA:
                resourcesAroma();
                break;
        }
    }

    private void resourcesUWP() {
        resourcesIOS();
    }

    private void resourcesAroma() {
        resourcesSwing();
    }

    private void resourcesSwing() {
        CMBuildEnvironment env = environment();
        File generated = new File(env.getBuilddir(), GENERATED_CMSOURCES);
        File app = new File(env.getBuilddir(), APP);
        File ann = new File(env.getBuilddir(), AnnotationConfig.ANN_LOCATION);
        File propertiesOut = new File(app, CROSSMOBILE_PROPERTIES);
        File info = new File(app, "Info.plist");
        File cacheBase = new File(env.getBuilddir(), PROJECT_CACHES);

        File ibobjects = new File(generated, IBOBJECTS_FILE);
        Collection<File> xibs = IBObjectsCreator.getXibFiles(env.getMaterialsDir(), ibobjects);
        IBParserMeta meta = null;   // We need this reference for parseMaterials later on
        if (xibs != null) {
            XIBList xibList = IBObjectsCreator.parse(env.getMaterialsDir(), xibs, ann);
            IBObjectsCreator.createJavaSource(xibList, ibobjects, new File(cacheBase, IBOBJECTS_FILE));
            meta = xibList.getMeta();
        }
        MaterialsManager.parseMaterials(meta, env.getMaterialsDir(), new File(env.getBuilddir(), APP));

        IconBuilder.copyIcons(IconBuilder.getDefaultHound(env.getBasedir()), new File(env.getBuilddir(), SYS), IconType.DESKTOP);
        new PropertiesCreator(env.getProperties(),
                env.getProperties().getProperty(MAIN_CLASS.tag().name), propertiesOut, env.getBasedir()).execute(env);
        new InfoPListCreator(env.getProperties(), info, null, env.getProperties().getProperty(INJECTED_INFOPLIST.tag().name),
                env.getBasedir()).execute(env);
        new PluginsLauncher(env.root().getPluginMetaData(), generated, cacheBase).execute();
        write(new File(env.getBuilddir(), "classes" + File.separator + FONT_LIST), String.join("\n", FontExtractor.findFonts(env.getMaterialsDir()).keySet()));
    }

    private void resourcesAndroid() {
        CMBuildEnvironment env = environment();
        if (env.isRun())
            AdbUtils.launch(env.getProperties().getProperty("sdk.dir")); // Early launching of ADB devices if run is requested

        File andrRes = new File(env.getBuilddir(), ANDROID_RES);
        File andrAsset = new File(env.getBuilddir(), ANDROID_ASSET);
        File generated = new File(env.getBuilddir(), GENERATED_CMSOURCES);
        File ann = new File(env.getBuilddir(), AnnotationConfig.ANN_LOCATION);
        File cacheBase = new File(env.getBuilddir(), PROJECT_CACHES);
        UpdateAndroidDependencies.execute(new File(env.getBuilddir(), CROSSMOBILE_GRADLE),
                env.getProperties().getProperty(GROUP_ID.tag().name) + "." + env.getProperties().getProperty(ARTIFACT_ID.tag().name),
                env.root());
        AndroidProjectCreator.execute(env);

        File ibobjects = new File(generated, IBOBJECTS_FILE);
        Collection<File> xibs = IBObjectsCreator.getXibFiles(env.getMaterialsDir(), ibobjects);
        IBParserMeta meta = null;   // We need this reference for parseMaterials later on
        if (xibs != null) {
            XIBList xibList = IBObjectsCreator.parse(env.getMaterialsDir(), xibs, ann);
            IBObjectsCreator.createJavaSource(xibList, ibobjects, new File(cacheBase, IBOBJECTS_FILE));
            meta = xibList.getMeta();
        }
        MaterialsManager.parseMaterials(meta, env.getMaterialsDir(), andrAsset);

        MaterialsManager.copyAndroidSys(asList(env.root().getRuntimeDependencies(true), DependencyItem::getFile), andrAsset, andrRes);

        ImageHound images = IconBuilder.getDefaultHound(env.getBasedir());
        IconBuilder.copyIcons(images, andrRes, IconType.BASE_ANDROID);
        IconBuilder.copyIcons(images, andrRes, IconType.ADAPTIVE_ANDROID);
        IconBuilder.copyMask(images, env.getBasedir(), andrRes);

        new PropertiesCreator(env.getProperties(), env.getProperties().getProperty(MAIN_CLASS.tag().name),
                new File(env.getBuilddir(), ANDROID_PROP), env.getBasedir()).execute(env);
        new InfoPListCreator(env.getProperties(),
                new File(env.getBuilddir(), ANDROID_PLIST), null, env.getProperties().getProperty(INJECTED_INFOPLIST.tag().name),
                env.getBasedir()).execute(env);
        new PluginsLauncher(env.root().getPluginMetaData(), generated, cacheBase).execute();
        //noinspection ResultOfMethodCallIgnored
        ann.mkdirs();

        write(new File(env.getBuilddir(), ANDROID_FONTLIST), FontExtractor.getFontDataAsResource(FontExtractor.findFonts(env.getMaterialsDir())));
        GradleManager.createAndUpdate(env);
        LocalPropertiesManager.createIfNotExist(env.getBasedir());
    }

    private void resourcesIOS() {
        try {
            delete(new File(environment().getBuilddir(), "classes")); // Needed by retrolambda to work properly
            ParamList paramList = new ParamList().
                    put(IPHONEOS_DEPLOYMENT_TARGET.tag(), environment().getProperties().getProperty(IPHONEOS_DEPLOYMENT_TARGET.tag().name));
            copyTemplateIfMissing("project.pbxproj",
                    new File(environment().getBasedir(), environment().getProperties().getProperty(ARTIFACT_ID.tag().name) + ".xcodeproj" + File.separator + "project.pbxproj"),
                    "Creating missing Xcode project file", paramList);
        } catch (Throwable ex) {
            BaseUtils.throwException(ex);
        }
    }
}
