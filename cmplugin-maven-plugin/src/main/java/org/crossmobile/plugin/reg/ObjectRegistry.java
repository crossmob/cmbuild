/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.bridge.system.LazyProperty;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.utils.ReflectionUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ObjectRegistry {

    public static final String NSObjectClassName = "crossmobile.ios.foundation.NSObject";
    public static final String CFTypeClassName = "crossmobile.ios.foundation.CFType";
    public static final String UIAppearanceClassName = "crossmobile.ios.uikit.UIAppearance";
    private final Registry reg;

    private final LazyProperty<Class<?>> NSObjectClass = new LazyProperty<>(() -> ReflectionUtils.getClassForName(NSObjectClassName));
    private final LazyProperty<Class<?>> CFTypeClass = new LazyProperty<>(() -> ReflectionUtils.getClassForName(CFTypeClassName));
    private final LazyProperty<Class<?>> UIAppearanceClass = new LazyProperty<>(() -> ReflectionUtils.getClassForName(UIAppearanceClassName));

    private final Map<Class<?>, NObject> objects = new TreeMap<>(Comparator.comparing(Class::getName));

    public void register(NObject nobj) {
        objects.put(nobj.getType(), nobj);
    }

    public NObject retrieve(Class<?> objClass) {
        return objClass == null ? null : objects.get(objClass);
    }

    public Iterable<NObject> retrieveAll() {
        return objects.values();
    }

    public Class<?> getNSObject() {
        return NSObjectClass.get();
    }

    public Class<?> getCFType() {
        return CFTypeClass.get();
    }

    ObjectRegistry(Registry reg) {
        this.reg = reg;
    }

    public boolean isUIAppearanceClass(Class<?> classToCheck) {
        return UIAppearanceClass.get().isAssignableFrom(classToCheck);
    }

    public boolean contains(String className) {
        for (Class<?> aClass : objects.keySet())
            if (aClass.getName().equals(className))
                return true;
        return false;
    }
}
