/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.xcode;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;

import static org.crossmobile.build.utils.PlistUtils.getPath;

public class PBXBuildFile extends PBXObject {

    private final String fileRef;
    private final NSObject settings;

    public PBXBuildFile(String id, NSDictionary data) {
        super(id, data);
        this.fileRef = getPath(data, "fileRef").toString();
        this.settings = getPath(data, "settings");
//        this.weak = containsElement((NSArray) getPath(data, "settings", "ATTRIBUTES"), "Weak");
    }

    @Override
    public String toString() {
        return id + " PBXBuildFile " + fileRef;
    }

    @Override
    public NSDictionary getData() {
        NSDictionary data = new NSDictionary();
        data.put("isa", isa);
        data.put("fileRef", fileRef);
        data.put("settings", settings);
        return data;
    }

}
