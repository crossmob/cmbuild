/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.xcode;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;

import java.util.Collection;

import static org.crossmobile.build.utils.PlistUtils.*;

public class PBXGroup extends PBXObject {

    private final Collection<String> children;
    private final String name;
    private final String sourceTree;

    public PBXGroup(String id, NSDictionary data) {
        super(id, data);
        this.name = getStringMaybe(getPath(data, "name"));
        this.sourceTree = getPath(data, "sourceTree").toString();
        children = arrayToCollection((NSArray) getPath(data, "children"));
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void addChild(String child) {
        children.add(child);
    }

    @Override
    public NSDictionary getData() {
        NSDictionary data = new NSDictionary();
        data.put("isa", isa);
        data.put("children", NSObject.wrap(children.toArray()));
        data.put("name", name);
        data.put("sourceTree", sourceTree);
        return data;
    }
}
