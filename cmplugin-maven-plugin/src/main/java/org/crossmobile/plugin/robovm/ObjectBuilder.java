/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.NotFoundException;
import org.crossmobile.bridge.ann.CMClass;
import org.crossmobile.bridge.ann.CMReference;
import org.crossmobile.bridge.ann.CMStruct;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.PluginRegistry;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.Log;

import java.io.IOException;

import static org.crossmobile.plugin.bro.JavaTransformer.LIBRARY_ANN;
import static org.crossmobile.plugin.bro.JavaTransformer.NATIVECLASS_ANN;
import static org.crossmobile.utils.JavassistAnnParam.toParam;
import static org.crossmobile.utils.JavassistUtils.addAnnotation;

class ObjectBuilder extends ClassBuilder {

    ObjectBuilder(NObject obj, ClassBuilderFactory cbf) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        super(obj, cbf);
        createClass();
        cbf.addPointer(getCclass(), cbf.getFileDest().getAbsolutePath());
        addAnnotations();
    }

    private void addAnnotations() {
        if (target.getAnnotation(CMClass.class) != null)
            addAnnotation(getCclass(), NATIVECLASS_ANN);

        if (target.getAnnotation(CMClass.class) != null ||
                target.getAnnotation(CMReference.class) != null ||
                target.getAnnotation(CMStruct.class) != null)
            addLibraryAnnotation(getCclass(), target);
    }

    void createClass() throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        Class<?> superclass = target.getSuperclass();
        setCclass(superclass != null ?
                cbf.waterpark().classPool().makeClass(target.getName(), cbf.waterpark().contains(superclass.getName()) ?
                        cbf.waterpark().get(superclass.getName()) :
                        cbf.getClass(cbf.registry().objects().retrieve(superclass)).getCclass())
                : cbf.waterpark().classPool().makeClass(target.getName()));
    }

    /**
     * Adds the library annotation to Classes
     */
    private void addLibraryAnnotation(CtClass cclass, Class<?> target) {
        String lib = cbf.registry().plugins().getLibs(target.getName());
        if (lib.isEmpty())
            Log.error("Cant find library for class : " + target.getName());
        else lib = lib.substring(0, lib.indexOf('.'));
        addAnnotation(cclass, LIBRARY_ANN, toParam("value", lib));
    }

    @Override
    public void finaliseClass() throws CannotCompileException, IOException, NotFoundException, ClassNotFoundException {
        addEmptyConstructorIfNeeded(getCclass());
        super.finaliseClass();
    }

    private void addEmptyConstructorIfNeeded(CtClass cclass) throws CannotCompileException {
        if (cclass.getConstructors().length == 0)
            cclass.addConstructor(CtNewConstructor.defaultConstructor(cclass));
    }
}
