/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;

public class Constraints extends Element {

    @Override
    protected void addSupported() {
        addSupportedChild(Elements.Constraint);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();
        for (Constraint item : parts(Elements.Constraint))
            out.append(item.toCode());
        return out.toString();
    }

}
