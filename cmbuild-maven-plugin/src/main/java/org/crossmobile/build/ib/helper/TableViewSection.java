/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

import java.util.Collection;

public class TableViewSection extends Subviews {

    @Override
    protected void addSupported() {
        addSupportedAttribute("id", Values.String);
        addSupportedAttribute("headerTitle", Values.LocalizedString);
        addSupportedAttribute("footerTitle", Values.LocalizedString);
        addSupportedChild(Elements.Cells);
    }

    public String addSection() {
        Collection<Cells> parts = parts(Elements.Cells);
        String cells;
        if (parts.size() > 1 || parts.isEmpty())
            cells = "null";
        else cells = parts.iterator().next().constructor();
        return attr("headerTitle") + ", " + attr("footerTitle") + ", " + cells;
    }
}
