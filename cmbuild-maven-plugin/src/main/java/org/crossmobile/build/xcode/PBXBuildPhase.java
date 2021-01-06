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

import static org.crossmobile.build.utils.PlistUtils.arrayToCollection;
import static org.crossmobile.build.utils.PlistUtils.getPath;

public class PBXBuildPhase extends PBXAny {

    private Collection<String> files;

    public PBXBuildPhase(String id, NSDictionary data) {
        super(id, data);
        files = arrayToCollection((NSArray) getPath(data, "files"));
    }

    @Override
    public String toString() {
        return id + " PBXBuildPhase{"
                + getData()
                + ", files=" + files
                + '}';
    }

    public void setFiles(Collection<String> files) {
        this.files = files;
    }

    @Override
    public NSDictionary getData() {
        data.put("files", NSObject.wrap(files.toArray()));
        return data;
    }
}
