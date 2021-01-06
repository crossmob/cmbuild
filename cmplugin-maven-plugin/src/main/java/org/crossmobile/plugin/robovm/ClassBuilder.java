/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.WaterPark;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassBuilder {

    protected final Class<?> target;
    protected final NObject obj;
    protected final ClassBuilderFactory cbf;
    private CtClass cclass;

    ClassBuilder(NObject obj, ClassBuilderFactory cbf) {
        target = obj.getType();
        this.obj = obj;
        this.cbf = cbf;
    }

    private final Map<String, MethodBuilder> methodBuilders = new HashMap<>();

    public void finaliseClass() throws CannotCompileException, IOException, NotFoundException, ClassNotFoundException {
        for (NSelector selector : obj.getSelectors())
            if (!methodBuilders.keySet().contains(selector.getName()))
                methodBuilders.put(selector.getName(), new MethodBuilder(selector, obj, cbf, cclass));
        for (MethodBuilder methodBuilder : methodBuilders.values()) methodBuilder.buildNativeMapping();
        if (!cclass.isInterface())
            cclass.setModifiers(cclass.getModifiers() & ~Modifier.ABSTRACT);
    }

    public CtClass getCclass() {
        if (cclass == null)
            throw new RuntimeException("Class type creation for " + obj.getType() + "has not be created");
        return cclass;
    }

    public void setCclass(CtClass cclass) {
        this.cclass = cclass;
    }
}
