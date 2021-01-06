/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Values;

public class DataDetectorType extends RealElement {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("link", Values.Boolean);
        addSupportedAttribute("address", Values.Boolean);
    }

//    @Override
//    public String toCode() {
//        StringBuilder out = new StringBuilder(super.toCode());
//
//        return out.toString();
//    }
}
