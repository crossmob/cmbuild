/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.plugin.objc.ReverseImportRegistry;

public class Registry {

    private final ObjectRegistry objectRegistry = new ObjectRegistry(this);
    private final ReverseImportRegistry importRegistry = new ReverseImportRegistry(this);
    private final PackageRegistry packageRegistry = new PackageRegistry(this);
    private final PluginRegistry pluginRegistry = new PluginRegistry(this);
    private final TargetRegistry targetRegistry = new TargetRegistry(this);
    private final TypeRegistry typeRegistry = new TypeRegistry(this);

    public ObjectRegistry objects() {
        return objectRegistry;
    }

    public PackageRegistry packages() {
        return packageRegistry;
    }

    public PluginRegistry plugins() {
        return pluginRegistry;
    }

    public TargetRegistry targets() {
        return targetRegistry;
    }

    public TypeRegistry types() {
        return typeRegistry;
    }

    public ReverseImportRegistry imports() {
        return importRegistry;
    }
}
