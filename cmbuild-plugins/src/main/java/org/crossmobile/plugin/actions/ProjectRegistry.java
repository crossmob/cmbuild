/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.build.utils.DependencyJarResolver;
import org.crossmobile.plugin.reg.PackageRegistry;
import org.crossmobile.plugin.reg.PluginRegistry;
import org.crossmobile.plugin.reg.TypeRegistry;
import org.crossmobile.plugin.utils.ClassCollection;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import static org.crossmobile.utils.CollectionUtils.asList;

public class ProjectRegistry {

    private Collection<File> libjars;
    private Collection<File> embedjars;
    private Collection<File> appjars;
    private Collection<File> allbljars;

    public void register(DependencyItem root, String[] embedlibs, ClassCollection cc) {
        if (libjars != null)
            return;

        libjars = new TreeSet<>();
        embedjars = new TreeSet<>();
        appjars = new TreeSet<>();
        allbljars = new TreeSet<>();

        DependencyJarResolver.gatherProgramAndEmbeddedLibs(root, true, embedlibs, libjars, embedjars, allbljars);
        appjars.add(root.getFile());
        appjars.addAll(embedjars);
        allbljars.addAll(libjars);

        cc.addClassPaths(asList(getAppjars(), File::getAbsolutePath));
        cc.register(false);
        for (Class cls : cc.getAllClasses())
            PluginRegistry.register(cls);
        for (Class cls : cc.getAllNativeClasses())
            TypeRegistry.register(cls);

        // Resolve all classes: required for type checking in plugins. Otherwise plugins will not be able to find base types
        Collection<Package> packages = new HashSet<>();
        Collection<Class<?>> classes = new HashSet<>();
        ClassCollection.gatherClasses(asList(libjars, File::getAbsolutePath), packages::add, classes::add, true);
        packages.forEach(PackageRegistry::registerDependencies);
        classes.forEach(PluginRegistry::registerDependencies);
        classes.forEach(TypeRegistry::registerDependencies);
    }

    public Collection<File> getAppjars() {
        return appjars;
    }

    public Collection<File> getEmbedjars() {
        return embedjars;
    }

    public Collection<File> getLibjars() {
        return libjars;
    }

    public Collection<File> getLibAndBlacklistedJars() {
        return allbljars;
    }
}
