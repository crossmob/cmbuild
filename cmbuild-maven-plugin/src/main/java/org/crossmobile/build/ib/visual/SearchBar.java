/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class SearchBar extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("text", Values.LocalizedString);
        addSupportedAttribute("prompt", Values.LocalizedString);
        addSupportedAttribute("placeholder", Values.LocalizedString);
        addSupportedChild("textInputTraits", Elements.TextInputTraits);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        appendAttribute(out,"text");
        appendAttribute(out,"prompt");
        appendAttribute(out,"placeholder");
        return out.toString();
    }

}
