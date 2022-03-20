/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class Date extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("timeIntervalSinceReferenceDate", Values.Double);
    }

    @Override
    public String toCode() {
        return "";
    }

}
