/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.xcode;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;

import java.util.Collection;

import static org.crossmobile.build.utils.PlistUtils.arrayToCollection;
import static org.crossmobile.build.utils.PlistUtils.getPath;

public class PBXProject extends PBXAny {

    private Collection<String> knownRegions;
    private Collection<String> targets;
    private final String mainGroup;
    private String productRefGroup;

    public PBXProject(String id, NSDictionary data) {
        super(id, data);
        knownRegions = arrayToCollection((NSArray) getPath(data, "knownRegions"));
        targets = arrayToCollection((NSArray) getPath(data, "targets"));
        productRefGroup = getPath(data, "productRefGroup").toString();
        mainGroup = getPath(data, "mainGroup").toString();
    }

    public void setKnownRegions(Collection<String> knownRegions) {
        this.knownRegions = knownRegions;
    }

    public void setTargets(Collection<String> targets) {
        this.targets = targets;
    }

    public String getMainGroup() {
        return mainGroup;
    }

    public void setProductRefGroup(String productRefGroup) {
        this.productRefGroup = productRefGroup;
    }

    @Override
    public NSDictionary getData() {
        data.put("knownRegions", knownRegions.toArray());
        data.put("targets", targets.toArray());
        data.put("mainGroup", mainGroup);
        data.put("productRefGroup", productRefGroup);
        return data;
    }

}
