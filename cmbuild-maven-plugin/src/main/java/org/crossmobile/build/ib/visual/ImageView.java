/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Values;

public class ImageView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();

        addSupportedAttribute("image", Values.String);
        addSupportedAttribute("highlightedImage", Values.String);
        addSupportedAttribute("highlighted", Values.Boolean);
        addSupportedAttribute("placeholderIntrinsicHeight", Values.Float);
        addSupportedAttribute("placeholderIntrinsicWidth", Values.Float);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        addImage(out, "image", "setImage");
        addImage(out, "highlightedImage", "setHighlightedImage");
        appendAttribute(out, "highlighted");
        return out.toString();
    }

    private void addImage(StringBuilder out, String attr, String methodName) {
        String img = attr(attr);
        if (img != null)
            append(out, methodName, "UIImage.imageNamed(" + img + ")");
    }

}
