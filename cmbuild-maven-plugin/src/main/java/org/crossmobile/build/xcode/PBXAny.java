// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.xcode;

import com.dd.plist.NSDictionary;

public class PBXAny extends PBXObject {

    protected final NSDictionary data;

    public PBXAny(String id, NSDictionary data) {
        super(id, data);
        this.data = data;
    }

    @Override
    public NSDictionary getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
