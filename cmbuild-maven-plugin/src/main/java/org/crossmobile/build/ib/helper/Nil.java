// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;

public class Nil extends Element {

    @Override
    protected void addSupported() {
    }

    @Override
    public String toCode() {
        return "";
    }

}
