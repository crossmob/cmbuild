/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class TextView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedChild("fontDescription", Elements.FontDescription);
        addSupportedChild("textInputTraits", Elements.TextInputTraits);
        addSupportedChild("textColor", Elements.Color);
        addSupportedAttribute("textAlignment", Values.Enum);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        appendTo(out, variable(), "setFont", item("fontDescription").toCode());
        appendValue(out, "textColor");
        appendEnumAttribute(out, "textAlignment", "NSTextAlignment");
        return out.toString();
    }

}
