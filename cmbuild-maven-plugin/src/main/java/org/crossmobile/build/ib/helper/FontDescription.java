/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class FontDescription extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("type", Values.String);
        addSupportedAttribute("name", Values.String);
        addSupportedAttribute("family", Values.String);
        addSupportedAttribute("pointSize", Values.Float);
        addSupportedAttribute("weight", Values.String);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder("UIFont.");
        if (attr("type", "custom").toLowerCase().endsWith("system\""))
            out.append(attrName("type")).append("FontOfSize(").append(attr("pointSize", "14")).append(")");
        else
            out.append("fontWithName(").append(attr("name")).append(", ").append(attr("pointSize", "14")).append(")");
        return out.toString();
    }

}
