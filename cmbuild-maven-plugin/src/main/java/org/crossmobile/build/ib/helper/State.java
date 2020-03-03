// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class State extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("title", Values.LocalizedString);
        addSupportedAttribute("image", Values.String);
        addSupportedAttribute("backgroundImage", Values.String);

        addSupportedChild("titleColor", Elements.Color);
        addSupportedChild("titleShadowColor", Elements.Color);
    }

    @Override
    public String toCode() {
        return "";
    }

    public String toCode(String varname) {
        StringBuilder out = new StringBuilder();
        String state = attrName("key");
        state = "UIControlState." + capitalize(state);
        stateAttr(out, varname, state, "title");
        stateImage(out, varname, state, "image");
        stateImage(out, varname, state, "backgroundImage");
        stateChild(out, varname, state, "titleShadowColor");
        stateChild(out, varname, state, "titleColor");
        return out.toString();
    }

    private void stateAttr(StringBuilder out, String varname, String state, String attr) {
        setState(out, varname, state, attr, attr(attr));
    }

    private void stateImage(StringBuilder out, String varname, String state, String attr) {
        String img = attr(attr);
        if (img != null)
            setState(out, varname, state, attr, "UIImage.imageNamed(" + img + ")");
    }

    private void stateChild(StringBuilder out, String varname, String state, String attr) {
        setState(out, varname, state, attr, value(attr));
    }

    private void setState(StringBuilder out, String varname, String state, String attr, String value) {
        if (value != null)
            out.append(I4).append(varname).append(".set").append(capitalize(attr))
                    .append("(").append(value).append(", ").append(state).append(");").append(NEWLINE);
    }
}
