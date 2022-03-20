/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.Version;
import org.crossmobile.bridge.ann.CMLibDepends;
import org.crossmobile.bridge.ann.CMLibTarget;
import org.crossmobile.plugin.utils.Texters;
import org.crossmobile.utils.Log;

import static org.crossmobile.plugin.utils.Texters.annName;
import static org.crossmobile.plugin.utils.Texters.typesafeClassName;

public class PluginDependency implements Comparable<PluginDependency> {

    private final String pluginName;
    private final String groupId;
    private final String version;
    private final String type;
    private final boolean cmPlugin;
    private final CMLibTarget givenTarget;

    private final String hostClassName;
    private final Registry reg;

    PluginDependency(CMLibDepends depends, Class<?> host, Registry reg, boolean requireTarget) {
        this.pluginName = depends.pluginName();
        this.groupId = depends.groupId();
        this.type = depends.type();
        this.givenTarget = depends.target();
        this.cmPlugin = groupId.isEmpty();
        /*
         * When groupId is empty, this is considered to be a CrossMobile plugin. This means that a multi-target
         * artifact will be attached. Otherwise a generic maven artifact will be attached.
         *
         * When defining a CrossMobile plugin, the version could be omitted. Then it will be considered that it matches
         * the version of the current CrossMobile library.
         */
        if (cmPlugin && depends.version().isEmpty())   // if it is a crossmobile plugin AND no specific version is provided, use default version
            version = Version.VERSION;
        else
            version = depends.version();

        String displayName = annName(CMLibDepends.class) + " in class " + typesafeClassName(host) + " (name=" + pluginName + " groupId=" + groupId + ")";
        if (pluginName.isEmpty())
            Log.error("No name provided for " + displayName);
        if (!cmPlugin && version.isEmpty())
            Log.error("Version not defined for " + displayName + ", when considered as an external non-CrossMobile plugin");
        if (cmPlugin && pluginName.startsWith("cm") && !version.equals(Version.VERSION)) // Plugins starting with cm are reserved for crossmobile core itself
            Log.error("Core CrossMobile plugin from " + displayName + " has version " + version + "; expected " + Version.VERSION);
        if (cmPlugin && !type.isEmpty())
            Log.error("No type supported for CrossMobile plugins");

        this.hostClassName = host.getName();
        this.reg = reg;
        if (target() == CMLibTarget.UNKNOWN && requireTarget)
            Log.error("Plugin Dependency " + toString() + " for Class " + hostClassName + ", has invalid target. A Valid target should be specified (i.e. \"target=API\")");
    }

    public CMLibTarget target() {
        return givenTarget == CMLibTarget.UNKNOWN ? reg.targets().getTarget(hostClassName) : givenTarget;
    }

    public boolean isCMPlugin() {
        return cmPlugin;
    }

    public String pluginName() {
        return pluginName;
    }

    public String type() {
        return type;
    }

    public String groupId() {
        return groupId;
    }

    public String version() {
        return version;
    }

    public String info() {
        return (isCMPlugin() ? pluginName() : groupId() + "." + pluginName()) + "(" + version() + ")";
    }

    @Override
    public int compareTo(PluginDependency other) {
        if (other == null)
            return -1;
        int s = this.groupId().compareTo(other.groupId());
        if (s != 0)
            return s;
        s = this.pluginName().compareTo(other.pluginName());
        if (s != 0)
            return s;
        return this.type().compareTo(other.type());
    }

    @Override
    public String toString() {
        return "PluginDependency{" +
                "name=" + pluginName +
                ", groupId=" + groupId +
                ", version=" + version +
                ", type=" + type +
                '}';
    }
}
