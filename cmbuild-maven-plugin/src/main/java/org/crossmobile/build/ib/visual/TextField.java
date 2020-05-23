/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.TextInputTraits;

public class TextField extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedChild("fontDescription", Elements.FontDescription);
        addSupportedChild("textInputTraits", Elements.TextInputTraits);
        addSupportedAttribute("borderStyle", new Value.Selections(new String[]{"default", "line", "bezel", "roundedRect"}));
        addSupportedAttribute("clearButtonMode", new Value.Selections(new String[]{"default", "whileEditing", "unlessEditing", "always"}));
        addSupportedAttribute("clearsOnBeginEditing", Values.Boolean);
        addSupportedAttribute("minimumFontSize", Values.Integer);
        addSupportedAttribute("adjustsFontSizeToFit", Values.Boolean);
        addSupportedAttribute("autocapitalizationType", new Value.Selections(new String[]{"none", "words", "sentences", "allCharacters"}));
        addSupportedAttribute("autocorrectionType", new Value.Selections(new String[]{"default", "no", "yes"}));
        addSupportedAttribute("keyboardType", new Value.Selections(new String[]{"default", "alphabet", "numbersAndPunctuation", "URL", "numberPad", "phonePad", "namePhonePad", "emailAddress", "decimalPad", "twitter", "webSearch"}));
        addSupportedAttribute("keyboardAppearance", new Value.Selections(new String[]{"default", "alert", "light"}));
        addSupportedAttribute("returnKeyType", new Value.Selections(new String[]{"default", "go", "google", "next", "route", "search", "send", "yahoo", "done", "emergencyCall"}));
        addSupportedAttribute("enablesReturnKeyAutomatically", Values.Boolean);
        addSupportedAttribute("secureTextEntry", Values.Boolean);
        addSupportedAttribute("placeholder", Values.LocalizedString);
        addSupportedAttribute("textAlignment", Values.String);

        addSupportedChild("textColor", Elements.Color);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        appendAttribute(out, "placeholder");
        appendAttribute(out, "borderStyle");
        appendTo(out, variable(), "setFont", item("fontDescription").toCode());

        ((TextInputTraits)item("textInputTraits")).toCode(out, variable());

        String textAlignment = attrName("textAlignment");
        if (textAlignment != null)
            out.append(I4).append(variable()).append(".setTextAlignment(NSTextAlignment.").append(capitalize(textAlignment)).append(");").append(NEWLINE);
        return out.toString();
    }

}
