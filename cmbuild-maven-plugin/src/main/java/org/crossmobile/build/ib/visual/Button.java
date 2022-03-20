/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.State;

public class Button extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("buttonType", Values.Enum);
        addSupportedAttribute("lineBreakMode", new Value.Selections(new String[]{"clip", "characterWrap", "wordWrap", "headTruncation", "middleTruncation", "tailTruncation"}));
        addSupportedAttribute("reversesTitleShadowWhenHighlighted", Values.Boolean);
        addSupportedAttribute("showsTouchWhenHighlighted", Values.Boolean);
        addSupportedChild("fontDescription", Elements.FontDescription);
        addSupportedChild("titleColor", Elements.Color);
        addSupportedChild("titleShadowColor", Elements.Color);
        addSupportedChild("titleShadowOffset", Elements.Size);
        addSupportedAttribute("reversesTitleShadowWhenHighlighted", Values.Boolean);
        addSupportedAttribute("showsTouchWhenHighlighted", Values.Boolean);
        addSupportedAttribute("adjustsImageWhenHighlighted", Values.Boolean);
        addSupportedAttribute("adjustsImageWhenDisabled", Values.Boolean);
        addSupportedChild("imageEdgeInsets", Elements.Inset);

        addSupportedChild("normal", Elements.State);
        addSupportedChild("disabled", Elements.State);
        addSupportedChild("selected", Elements.State);
        addSupportedChild("highlighted", Elements.State);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        String variable = variable();
        appendState(out, "normal", variable);
        appendState(out, "disabled", variable);
        appendState(out, "selected", variable);
        appendState(out, "highlighted", variable);
        if (item("fontDescription") != null)
            appendTo(out, variable(), "titleLabel().setFont", item("fontDescription").toCode());
        return out.toString();
    }

    @Override
    protected String constructor() {
        String buttonType = attr("buttonType", "custom");
        if ("RoundedRect".equals(buttonType))
            buttonType = "System";
        return "UIButton.buttonWithType(UIButtonType." + buttonType + ")";
    }

    private void appendState(StringBuilder out, String state, String varname) {
        State s = (State) item(state);
        if (s != null)
            out.append(s.toCode(varname));
    }
}
