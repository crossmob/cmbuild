/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.Connections;

public class Control extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("contentHorizontalAlignment", new Value.Selections(new String[]{"center", "left", "right", "fill"}));
        addSupportedAttribute("contentVerticalAlignment", new Value.Selections(new String[]{"center", "top", "bottom", "fill"}));
        addSupportedAttribute("enabled", Values.Boolean);
        addSupportedAttribute("highlighted", Values.Boolean);
        addSupportedAttribute("selected", Values.Boolean);
    }

    @Override
    public String toCode() {
        String variable = variable();
        StringBuilder out = new StringBuilder(super.toCode());
        appendAttribute(out, "enabled");
        appendAttribute(out, "highlighted");
        appendAttribute(out, "selected");
        appendAttribute(out, "contentHorizontalAlignment");
        appendAttribute(out, "contentVerticalAlignment");
        for (Connections c : parts(Elements.Connections)) {
            out.append(c.getActions(variable));
            out.append(c.getSegues(variable));
        }

        return out.toString();
    }

}
