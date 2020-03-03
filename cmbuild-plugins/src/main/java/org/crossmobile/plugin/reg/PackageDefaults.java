// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.reg;

import org.crossmobile.bridge.ann.CMLibTarget;

public class PackageDefaults {

    public final String plugin;
    public final CMLibTarget target;

    public PackageDefaults(String plugin, CMLibTarget target) {
        this.plugin = plugin;
        this.target = target;
    }

}
