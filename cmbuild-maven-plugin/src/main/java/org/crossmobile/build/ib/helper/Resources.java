/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;

public class Resources extends Element {
    @Override
    protected void addSupported() {
        addSupportedChild(Elements.Image);
        addSupportedChild(Elements.SystemColor);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();

        out.append(I2).append("public static class Resources {").append(NEWLINE);
        for (Image image : parts(Elements.Image))
            out.append(image.toCode());
        for (SystemColor systemColor : parts(Elements.SystemColor))
            out.append(systemColor.toCode()).append(NEWLINE);
        out.append(I2).append("}").append(NEWLINE);
        return out.toString();
    }
}
