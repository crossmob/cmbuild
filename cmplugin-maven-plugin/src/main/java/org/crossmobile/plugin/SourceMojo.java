/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import javassist.ClassPool;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.crossmobile.plugin.actions.AppearanceInjections;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.ClassCollection;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ReflectionUtils;
import org.robovm.objc.block.VoidBlock1;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.io.File.separator;
import static java.util.Arrays.asList;
import static org.crossmobile.prefs.Config.CMPLUGIN_MAVEN_PLUGIN_SIGNATURE;
import static org.crossmobile.utils.FileUtils.getLastModified;
import static org.crossmobile.utils.TimeUtils.time;

@Mojo(name = "source", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class SourceMojo extends GenericPluginMojo {

    private final static Comparator<Class<?>> classComparator = (o1, o2) -> {
        if (o1 == o2)
            return 0;
        if (o1.isAssignableFrom(o2))
            return -1;
        if (o2.isAssignableFrom(o1))
            return 1;
        return o1.getName().compareTo(o2.getName());
    };

    @Override
    public void exec(Registry reg) {
        File status = new File(getProject().getBuild().getDirectory(), "maven-status" + separator + CMPLUGIN_MAVEN_PLUGIN_SIGNATURE + separator + "last-modification");
        File sourceDir = new File(getProject().getBuild().getSourceDirectory());
        if (getLastModified(sourceDir) < getLastModified(status)) {
            Log.info("No classes to modify");
            return;
        }
        time("Post-process classes", () -> {
            File classes = new File(getProject().getBuild().getDirectory(), "classes");
            ReflectionUtils.resetClassLoader();
            ClassPool cp = ClassPool.getDefault();
            Collection<Class<?>> appearanceClasses = new TreeSet<>(classComparator);

            List<String> classPath = asList(classes.getAbsolutePath(), VoidBlock1.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            ClassCollection.addClassPaths(cp, classPath);
            ClassCollection.gatherClasses(classPath, null, e -> {
                if (!e.isInterface() && reg.objects().isUIAppearanceClass(e))
                    appearanceClasses.add(e);
            }, true);
            AppearanceInjections injections = new AppearanceInjections(cp, classes.getAbsolutePath());
            injections.cleanup(classes);
            for (Class<?> cls : appearanceClasses)
                injections.makeAppearance(cls);
            FileUtils.write(status, "");
        });

//            // This should be done source-level
//            time("Create bean classes", () -> {
//                CreateBeanAPI bean = new CreateBeanAPI(cc.getClassPool());
//                for (Class<?> cls : cc.getCompileTimeClasses())
//                    bean.beanClass(cls, runtime);
//            });

    }
}