/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Values;

public class Placeholder extends RealElement {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("placeholderIdentifier", Values.String);
        addSupportedAttribute("userLabel", Values.String);
        addSupportedAttribute("sceneMemberID", Values.String);
    }

    boolean isOwner() {
        String identifier = attrName("placeholderIdentifier");
        return identifier != null && (identifier.toLowerCase().equals("ibfilesowner") || identifier.toLowerCase().equals("ibfirstresponder"));
    }

    @Override
    public String toCode() {
        return printConnections();
    }

}
