/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class Size extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("width", Values.Float);
        addSupportedAttribute("height", Values.Float);
    }

    @Override
    public String toCode() {
        return "new CGSize(" + attr("width", "0") + ", " + attr("height", "0") + ")";
    }

}
