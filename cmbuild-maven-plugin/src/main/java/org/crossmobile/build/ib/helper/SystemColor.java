/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class SystemColor extends Element {
    @Override
    protected void addSupported() {
        addSupportedChild(Elements.Color);
        addSupportedAttribute("name", Values.String);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();
        out.append(I3).append("public static UIColor getColor").append(capitalize(attrName("name"))).append("(){").append(NEWLINE);
        for (Color color : this.parts(Elements.Color))
            out.append(I4).append("return ").append(color.toCode()).append(";").append(NEWLINE);
        out.append(I3).append("}").append(NEWLINE);
        return out.toString();
    }
}
