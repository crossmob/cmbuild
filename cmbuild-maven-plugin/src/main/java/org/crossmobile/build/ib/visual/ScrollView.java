// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class ScrollView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("indicatorStyle", new Value.Selections(new String[]{"default", "black", "white"}));
        addSupportedAttribute("showsVerticalScrollIndicator", Values.Boolean);
        addSupportedAttribute("showsHorizontalScrollIndicator", Values.Boolean);
        addSupportedAttribute("scrollEnabled", Values.Boolean);
        addSupportedAttribute("pagingEnabled", Values.Boolean);
        addSupportedAttribute("directionalLockEnabled", Values.Boolean);
        addSupportedAttribute("bounces", Values.Boolean);
        addSupportedAttribute("alwaysBounceHorizontal", Values.Boolean);
        addSupportedAttribute("alwaysBounceVertical", Values.Boolean);
        addSupportedAttribute("bouncesZoom", Values.Boolean);
        addSupportedAttribute("minimumZoomScale", Values.Integer);
        addSupportedAttribute("maximumZoomScale", Values.Integer);
        addSupportedAttribute("delaysContentTouches", Values.Boolean);
        addSupportedAttribute("canCancelContentTouches", Values.Boolean);
        addSupportedAttribute("keyboardDismissMode", new Value.Selections(new String[]{"noDismiss", "onDrag", "interactive"}));

    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        appendAttribute(out, "showsVerticalScrollIndicator");
        appendAttribute(out, "showsHorizontalScrollIndicator");
        appendAttribute(out, "scrollEnabled");
        appendAttribute(out, "pagingEnabled");
        appendAttribute(out, "directionalLockEnabled");
        appendAttribute(out, "bounces");
        appendAttribute(out, "alwaysBounceHorizontal");
        appendAttribute(out, "alwaysBounceVertical");
        appendAttribute(out, "delaysContentTouches");
        appendAttribute(out, "canCancelContentTouches");
        return out.toString();
    }

}
