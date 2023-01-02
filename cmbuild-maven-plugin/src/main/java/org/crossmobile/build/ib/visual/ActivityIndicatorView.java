/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class ActivityIndicatorView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("style", new Value.Selections(new String[]{"whiteLarge", "white", "gray"}));
        addSupportedAttribute("animating", Values.Boolean);
        addSupportedAttribute("hidesWhenStopped", Values.Boolean);

        addSupportedChild("color", Elements.Color);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        appendAttribute(out, "setActivityIndicatorViewStyle", "style");
        appendAttribute(out, "hidesWhenStopped");
        if (attr("animating", "NO").equals("true"))
            append(out, "startAnimating");
        append(out, "setColor", item("color") == null ? null : item("color").toCode());
        return out.toString();
    }

}
