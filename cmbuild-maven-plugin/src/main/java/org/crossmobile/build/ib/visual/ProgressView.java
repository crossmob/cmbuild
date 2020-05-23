/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Value;

public class ProgressView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("progressViewStyle", new Value.Selections(new String[]{"default", "bar"}));
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        appendAttribute(out, "progressViewStyle");
        return out.toString();
    }

    @Override
    protected String constructor() {
        return "new UIProgressView(" + attr("progressViewStyle", "0") + ")";
    }

}
