// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.reg;

import org.crossmobile.Version;
import org.crossmobile.bridge.ann.CMLibDepends;
import org.crossmobile.bridge.ann.CMLibTarget;
import org.crossmobile.utils.Log;

public class PluginDependency implements Comparable<PluginDependency> {

    private final CMLibDepends depends;
    private final String hostClassName;
    private CMLibTarget target;

    public PluginDependency(CMLibDepends depends, Class host, boolean requireTarget) {
        this.depends = depends;
        this.hostClassName = host.getName();
        if (target() == CMLibTarget.UNKNOWN && requireTarget)
            Log.error("Plugin Dependency " + toString() + " for Class " + hostClassName + ", has invalid target. A Valid target should be specified (i.e. \"target=API\")");
    }

    public CMLibTarget target() {
        if (target == null)
            target = depends.target() != CMLibTarget.UNKNOWN ? depends.target() : TargetRegistry.getTarget(hostClassName);
        return target;
    }

    public boolean isCMPlugin() {
        return depends.isCMPlugin();
    }

    public String pluginName() {
        return depends.pluginName();
    }

    public String type() {
        return depends.type();
    }

    public String groupId() {
        return depends.groupId();
    }

    public String version() {
        return depends.version().equals("") ? Version.VERSION : depends.version();
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
        return "Dependency{" + depends + "}";
    }
}
