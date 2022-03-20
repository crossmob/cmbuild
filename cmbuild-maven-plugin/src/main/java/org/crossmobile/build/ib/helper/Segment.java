/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Values;

public class Segment extends RealElement {

    @Override
    protected void addSupported() {
        addSupportedAttribute("title", Values.LocalizedString);
        addSupportedAttribute("image", Values.String);
    }

    @Override
    public String toCode() {
        if (attr("image") != null)
            return "UIImage.imageNamed(" + attr("image") + ")";
        if (attr("title") != null)
            return attr("title");
        return "";
    }
}
