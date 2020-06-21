/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.bridge.ann.CMLib;
import org.crossmobile.bridge.ann.CMLibTarget;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.NamingUtils;
import org.crossmobile.utils.ReflectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class TargetRegistry {

    private static final Map<String, CMLibTarget> targets = new LinkedHashMap<>();

    public static CMLibTarget register(Class<?> cls) {
        String name = cls.getName();
        CMLibTarget target;
        CMLib library = cls.getAnnotation(CMLib.class);
        if (library != null && library.target() != CMLibTarget.UNKNOWN)
            target = library.target();
        else {
            cls = ReflectionUtils.getTopClass(cls);
            library = cls.getAnnotation(CMLib.class);
            if (library != null && library.target() != CMLibTarget.UNKNOWN)
                target = library.target();
            else
                target = PackageRegistry.getTarget(cls.getPackage().getName());
        }
        if (target == CMLibTarget.UNKNOWN)
            Log.error("Unable to locate target of class " + name);
        targets.put(name, target);
        return target;
    }

    public static CMLibTarget getTarget(String cls) {
        return getTarget(cls, false);
    }

    public static CMLibTarget getTarget(String cls, boolean silently) {
        CMLibTarget target = targets.get(cls);
        if (target != null)
            return target;
        target = PackageRegistry.getTarget(NamingUtils.getPackageName(cls));
        if (target != null)
            return target;
        if (!silently)
            Log.error("Unable to locate registered target of class " + cls);
        return null;
    }
}
