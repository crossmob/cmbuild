/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.plugin.objc.ReverseImportRegistry;
import org.crossmobile.plugin.utils.ClassCollection;

public class Registry {

    private final ObjectRegistry objectRegistry = new ObjectRegistry(this);
    private final ReverseImportRegistry importRegistry = new ReverseImportRegistry(this);
    private final PackageRegistry packageRegistry = new PackageRegistry(this);
    private final DeclaredPluginRegistry declaredPluginRegistry = new DeclaredPluginRegistry(this);
    private final TargetRegistry targetRegistry = new TargetRegistry(this);
    private final TypeRegistry typeRegistry = new TypeRegistry(this);
    private final ClassCollection classCollection = new ClassCollection();
    private final ReverseCode reverseCode = new ReverseCode(classCollection.getClassPool(), this);


    public ObjectRegistry objects() {
        return objectRegistry;
    }

    public PackageRegistry packages() {
        return packageRegistry;
    }

    public DeclaredPluginRegistry plugins() {
        return declaredPluginRegistry;
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

    public ReverseCode reverse() {
        return reverseCode;
    }

    public ClassCollection getClassCollection() {
        return classCollection;
    }
}
