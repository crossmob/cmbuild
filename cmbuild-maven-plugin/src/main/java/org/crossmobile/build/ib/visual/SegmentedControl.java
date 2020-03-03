// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class SegmentedControl extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("segmentControlStyle", new Value.Selections(new String[]{"plain", "bordered", "bar"}));
        addSupportedAttribute("momentary", Values.Boolean);
        addSupportedAttribute("selectedSegmentIndex", Values.Integer);

        addSupportedChild(Elements.Segments);

    }

    @Override
    protected String constructor() {
        try {
            return "new " + getClassName() + "(" + parts(Elements.Segments).iterator().next().toCode() + ")";
        } catch (Exception e) {
            return super.constructor();
        }
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        return out.toString();
    }

}
