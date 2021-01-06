/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.utils.ClasspathUtils;
import org.crossmobile.utils.Commander;
import org.crossmobile.utils.ReverseCodeCollection;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import static org.crossmobile.build.utils.Config.CLASSES;
import static org.crossmobile.utils.ClasspathUtils.CLASS_USAGE_SIGNATURE;

@Mojo(name = "findclasses", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class FindClassesMojo extends GenericMojo {

    @Override
    public void exec() {
        File classesDir = new File(getBuildDir(), CLASSES);
        for (String className : findAllImports(Collections.singleton(classesDir)))
            System.out.println(CLASS_USAGE_SIGNATURE + className);
    }

    public static Collection<String> findAllImports(Collection<File> classpath) {
        Collection<String> classes = new TreeSet<>();
        ReverseCodeCollection dbn = new ReverseCodeCollection(classpath);
        for (String classname : ClasspathUtils.getClasspathClasses(classpath, true)) {
            try {
                CtClass cls = dbn.getClassPool().get(classname);
                classes.addAll(cls.getRefClasses());
            } catch (NotFoundException e) {
                BaseUtils.throwException(e);
            }
        }
        return classes;
    }
}
