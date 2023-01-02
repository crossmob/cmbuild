/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.xcode.resources;


import org.crossmobile.build.xcode.PBXBuildFile;
import org.crossmobile.build.xcode.PBXFileReference;
import org.crossmobile.build.xcode.XcodeTargetRegistry;
import org.crossmobile.utils.XcodeTarget;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class AnyResource implements Comparable<AnyResource> {


    private final String fileref;
    private final String name;
    private final String path;


    public AnyResource(String path, String fileref) {
        this(path == null ? "" : new File(path).getName(), path, fileref);
    }

    public AnyResource(String name, String path, String fileref) {
        this.name = name;
        this.path = path;
        this.fileref = fileref;
    }

    @Override
    public int hashCode() {
        return 97 + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        return path.equals(((AnyResource) obj).name);
    }

    public abstract String family();

    protected abstract String filetype();

    protected abstract boolean isText();

    protected abstract boolean isInSDK();

    public boolean isBuildable() {
        return true;
    }

    public boolean isLocalized() {
        return false;
    }

    public String getFileref() {
        return fileref;
    }

    public String name() {
        return name;
    }

    public String path() {
        return path;
    }

    protected abstract Map<String, Object> dataMap();

    public abstract PBXFileReference fileReference();

    protected Map<String, Object> buildMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("isa", "PBXBuildFile");
        data.put("fileRef", fileref);
        return data;
    }

    public abstract PBXBuildFile buildFile(String ref);

    public String abstractName() {
        int dot = name.lastIndexOf(".");
        if (dot > 0)
            return name.substring(0, dot);
        return name;
    }

    @Override
    public int compareTo(AnyResource other) {
        return name.compareToIgnoreCase(other.name);
    }

    public final boolean belongsToTarget(String target) {
        return belongsToTarget(abstractName(), XcodeTargetRegistry.getTarget(target));
    }

    protected abstract boolean belongsToTarget(String resourceName, XcodeTarget target);
}
