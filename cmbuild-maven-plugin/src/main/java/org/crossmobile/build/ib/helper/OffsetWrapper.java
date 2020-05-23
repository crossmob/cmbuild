/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class OffsetWrapper extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("horizontal", Values.Integer);
        addSupportedAttribute("vertical", Values.Integer);
    }

    @Override
    public String toCode() {
        return "";
    }

}
