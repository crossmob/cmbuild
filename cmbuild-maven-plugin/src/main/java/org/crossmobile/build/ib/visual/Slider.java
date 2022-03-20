/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class Slider extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();

        addSupportedAttribute("value", Values.Integer);
        addSupportedAttribute("minValue", Values.Integer);
        addSupportedAttribute("maxValue", Values.Integer);
        addSupportedChild("minimumTrackTintColor", Elements.Color);
        addSupportedChild("maximumTrackTintColor", Elements.Color);
        addSupportedAttribute("continuous", Values.Boolean);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        appendAttribute(out, "value");
        appendAttribute(out, "minValue");
        appendAttribute(out, "maxValue");
        return out.toString();
    }

}
