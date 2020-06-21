/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

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

    private Class<?> NSObjectClass;
    private Class<?> UIAppearanceClass;
    private Class<?> CFTypeClass;

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
        return NSObjectClass = getClassNamed(NSObjectClass, NSObjectClassName);
    }

    public Class<?> getCFType() {
        return CFTypeClass = getClassNamed(CFTypeClass, CFTypeClassName);
    }

    ObjectRegistry(Registry reg) {
        this.reg = reg;
    }

    public boolean isUIAppearanceClass(Class<?> classToCheck) {
        return (UIAppearanceClass = getClassNamed(UIAppearanceClass, UIAppearanceClassName)).isAssignableFrom(classToCheck);
    }

    public boolean contains(String className) {
        for (Class<?> aClass : objects.keySet())
            if (aClass.getName().equals(className))
                return true;
        return false;
    }

    private Class<?> getClassNamed(Class<?> cached, String className) {
        if (cached == null && (cached = ReflectionUtils.getClassForName(className)) == null)
            throw new RuntimeException("Unable to locate class " + className + " in classpath");
        return cached;
    }
}
