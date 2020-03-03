// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Values;

public class TableViewCellContentView extends View {
    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("tableViewCell", Values.String);
        addSupportedAttribute("insetsLayoutMarginsFromSafeArea", Values.Boolean);
        addSupportedAttribute("preservesSuperviewLayoutMargins", Values.Boolean);
    }

    @Override
    public String toCode() {
        return toCodeTo(null);
    }

    @Override
    public String toCodeTo(String variable) {
        variable = variable + ".contentView()";
        StringBuilder out = new StringBuilder(super.toCodeTo(variable));
        out.append(super.addSubviews(variable));
        out.append(late(variable(), true).replaceAll(variable(), variable));
        return out.toString();
    }
}
