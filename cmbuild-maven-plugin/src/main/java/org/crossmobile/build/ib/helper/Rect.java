// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class Rect extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("x", Values.Float);
        addSupportedAttribute("y", Values.Float);
        addSupportedAttribute("width", Values.Float);
        addSupportedAttribute("height", Values.Float);
    }

    @Override
    public String toCode() {
        return "new CGRect(" + attr("x") + ", " + attr("y") + ", " + attr("width") + ", " + attr("height") + ")";
    }

}
