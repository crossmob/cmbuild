/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class TabBar extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedChild(Elements.Items);
        addSupportedAttribute("barStyle", new Value.Selections(new String[]{"default", "black"}));
        addSupportedAttribute("translucent", Values.Boolean);
        addSupportedAttribute("barTintColor", Values.Integer);
        addSupportedAttribute("itemPositioning", new Value.Selections(new String[]{"automatic", "fill", "centered"}));
        addSupportedAttribute("itemWidth", Values.Integer);
        addSupportedAttribute("itemSpacing", Values.Integer);

        addSupportedChild("selectedImageTintColor", Elements.Color);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
