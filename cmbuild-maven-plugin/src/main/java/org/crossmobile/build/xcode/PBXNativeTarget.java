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

public class PBXNativeTarget extends PBXAny {

    private String name;
    private String productName;
    private String productReference;
    public final String productType;
    public final String buildConfigurationList;
    private Collection<String> buildPhases;
    private Collection<String> dependencies;

    public PBXNativeTarget(String id, String name, XCConfigurationList configurationList, boolean asExtension) {
        this(id, getTargetData(name, configurationList, asExtension));
    }

    public PBXNativeTarget(String id, NSDictionary data) {
        //TODO null pointer handling
        super(id, data);
        name = getPath(data, "name").toString();
        productName = getPath(data, "productName").toString();
        productReference = getPath(data, "productReference").toString();
        productType = getPath(data, "productType").toString();
        buildPhases = arrayToCollection((NSArray) getPath(data, "buildPhases"));
        dependencies = arrayToCollection((NSArray) getPath(data, "dependencies"));
        buildConfigurationList = getPath(data, "buildConfigurationList").toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }

    @Override
    public NSDictionary getData() {
        NSDictionary data = super.getData();
        data.put("name", name);
        data.put("productName", productName);
        data.put("buildPhases", buildPhases);
        data.put("dependencies", dependencies);
        data.put("productReference", productReference);
        return data;
    }

    public void setBuildPhases(Collection<String> buildPhases) {
        this.buildPhases = buildPhases;
    }

    public Collection<String> getBuildPhases() {
        return buildPhases;
    }

    public void addBuildPhase(String id) {
        buildPhases.add(id);
    }

    public void setDependencies(Collection<String> dependencies) {
        this.dependencies = dependencies;
    }

    private static NSDictionary getTargetData(String name, XCConfigurationList configurationList, boolean asExtension) {
        NSDictionary data = new NSDictionary();
        data.put("isa", "PBXNativeTarget");
        data.put("buildConfigurationList", configurationList.id);
        data.put("buildPhases", new NSArray());
        data.put("buildRules", new NSArray());
        data.put("dependencies", new NSArray());
        data.put("name", name);
        data.put("productName", name);
        data.put("productReference", "");
        data.put("productType", asExtension ? "com.apple.product-type.app-extension" : "com.apple.product-type.application");
        return data;
    }
}
