/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.xcode;

import com.dd.plist.NSDictionary;

import static org.crossmobile.build.utils.PlistUtils.getPath;

public abstract class PBXObject {

    public final String id;
    public final String isa;

    public PBXObject(String id, NSDictionary data) {
        this.id = id;
        this.isa = getPath(data, "isa").toString();
    }

    public abstract NSDictionary getData();

    public String getId() {
        return id;
    }
}
