/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Values;

public class ViewControllerLayoutGuide extends RealElement {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("type", Values.String);
    }

    @Override
    public String getClassName() {
        return "UILayoutSupport";
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder().append(I4).append(variable()).append(" = ")
                .append(attrName("type")).append("LayoutGuide();").append(NEWLINE);
        return out.toString();
    }
}
