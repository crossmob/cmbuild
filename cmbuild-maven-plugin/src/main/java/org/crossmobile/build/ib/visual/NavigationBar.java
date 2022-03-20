/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class NavigationBar extends View {

    @Override
    protected void addSupported() {
        super.addSupported();

        addSupportedAttribute("barStyle", new Value.Selections(new String[]{"default", "black", "blackTranslucent"}));
        addSupportedAttribute("translucent", Values.Boolean);
        addSupportedAttribute("inset", null);

        addSupportedChild("barTintColor", Elements.Color);
        addSupportedChild("textColor", Elements.Color);
        addSupportedChild("textShadowColor", Elements.Color);
        addSupportedChild("titleTextAttributes", Elements.TextAttributes);
    }

    public String toCode(String variable) {
        StringBuilder out = new StringBuilder();
        appendTo(out, "navigationBar()", "setTranslucent", attr("translucent", "YES"));
        appendTo(out, "navigationBar()", "setBarStyle", attr("barStyle", "default"));
        return out.toString();
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
