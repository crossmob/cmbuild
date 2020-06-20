/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.bridge.ann.CMLib;
import org.crossmobile.bridge.ann.CMLibTarget;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ReflectionUtils;
import org.crossmobile.utils.func.Opt;
import org.crossmobile.utils.func.UnsafePredicate;

import java.util.HashMap;
import java.util.Map;

import static org.crossmobile.bridge.ann.CMLibTarget.UNKNOWN;

public class PackageRegistry {

    private static final Map<String, PackageDefaults> registry = new HashMap<>();
    private static final Map<String, PackageDefaults> virtualRegistry = new HashMap<>();

    public static void register(Package pkg) {
        register(pkg, true);
    }

    public static void registerDependencies(Package pkg) {
        register(pkg, false);
    }

    private static void register(Package pkg, boolean requireAllInfo) {
        String pkgName = pkg.getName();
        PackageDefaults def = registry.get(pkgName);
        if (def == null) {
            String pluginName = "";
            CMLibTarget target = UNKNOWN;
            while (pkg != null) {
                pkg = ReflectionUtils.findPackage(pkg, CMLib.class);
                if (pkg != null) {
                    CMLib ann = pkg.getAnnotation(CMLib.class);
                    if (ann != null) {
                        if (pluginName.isEmpty())
                            pluginName = ann.name();
                        if (target == UNKNOWN)
                            target = ann.target();
                    }
                    if (!pluginName.isEmpty() && target != UNKNOWN)
                        pkg = null;
                    else
                        pkg = ReflectionUtils.getParentPackage(pkg);
                }
            }

            if (pluginName.isEmpty() && target == UNKNOWN) {
                if (requireAllInfo && getDefaults(pkgName, virtualRegistry) == null)
                    Log.error("Unable to locate default plugin and target for package " + pkgName);
            } else {
                if (target == UNKNOWN)
                    Log.error("Unable to locate default target for package " + pkgName);
                else if (pluginName.isEmpty())
                    Log.error("Unable to locate default plugin for package " + pkgName);
            }
            registry.put(pkgName, new PackageDefaults(pluginName, target));
        }
    }

    public static void register(String pkgName, String pluginName, String target) {
        if (pkgName == null || pkgName.trim().isEmpty()) {
            Log.error("Package name is a required field");
            return;
        }
        if (pluginName == null || pluginName.trim().isEmpty()) {
            Log.error("Plugin is a required field");
            return;
        }
        if (target == null || target.trim().isEmpty()) {
            Log.error("Target is a required field");
            return;
        }
        CMLibTarget libtargt;
        try {
            libtargt = CMLibTarget.valueOf(target.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            Log.error("Unknown target: " + target);
            return;
        }
        virtualRegistry.put(pkgName, new PackageDefaults(pluginName, libtargt));
    }

    public static String getPlugin(String pkg) {
        return getAny(pkg, p -> !p.plugin.trim().isEmpty()).map(d -> d.plugin).getOrElse("");
    }

    public static CMLibTarget getTarget(String pkg) {
        return getAny(pkg, p -> p.target != UNKNOWN).map(d -> d.target).getOrElse(UNKNOWN);
    }

    private static Opt<PackageDefaults> getAny(String pkg, UnsafePredicate<PackageDefaults> predicate) {
        return Opt.of(getDefaults(pkg, registry)).filter(predicate).mapMissing(() -> getDefaults(pkg, virtualRegistry));
    }

    private static PackageDefaults getDefaults(String pkg, Map<String, PackageDefaults> aRegistry) {
        while (pkg != null) {
            PackageDefaults defs = aRegistry.get(pkg);
            if (defs != null)
                return defs;
            if (pkg.isEmpty())
                pkg = null;
            else {
                int dot = pkg.lastIndexOf('.');
                if (dot >= 0)
                    pkg = pkg.substring(0, dot);
                else
                    pkg = "";
            }
        }
        return null;
    }
}
