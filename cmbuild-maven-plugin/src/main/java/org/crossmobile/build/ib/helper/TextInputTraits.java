// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Values;

public class TextInputTraits extends RealElement {

    @Override
    protected void addSupported() {
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("textContentType", Values.String);
        addSupportedAttribute("keyboardType", Values.Enum);
        addSupportedAttribute("autocapitalizationType", Values.Enum);
        addSupportedAttribute("secureTextEntry", Values.Boolean);
        addSupportedAttribute("autocorrectionType", Values.Enum);
        addSupportedAttribute("spellCheckingType", Values.Enum);
    }

    @Override
    public String toCode() {
        return "";
    }

    public void toCode(StringBuilder out, String variable) {
        appendAttributeTo(out, variable, "textContentType");
        appendAttributeTo(out, variable, "secureTextEntry");
        appendEnumAttributeTo(out, variable, "autocorrectionType" , "UITextAutocorrectionType");
        appendEnumAttributeTo(out, variable, "spellCheckingType", "UITextSpellCheckingType");
        appendEnumAttributeTo(out, variable, "keyboardType", "UIKeyboardType");
        appendEnumAttributeTo(out, variable, "autocapitalizationType", "UITextAutocapitalizationType");
    }
}
