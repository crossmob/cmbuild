/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class Inset extends Element {

    @Override
    protected void addSupported() {

        addSupportedAttribute("minX", Values.Float);
        addSupportedAttribute("minY", Values.Float);
        addSupportedAttribute("maxX", Values.Float);
        addSupportedAttribute("maxY", Values.Float);

    }

    @Override
    public String toCode() {
        return "";
    }

}
