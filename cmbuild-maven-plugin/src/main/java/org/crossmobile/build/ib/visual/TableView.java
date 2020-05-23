/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.Prototypes;
import org.crossmobile.build.ib.helper.Sections;

import java.util.Collection;

public class TableView extends ScrollView {

    @Override
    protected void addSupported() {
        super.addSupported();

        addSupportedAttribute("style", new Value.Selections(new String[]{"plain", "grouped"}));
        addSupportedAttribute("separatorStyle", new Value.Selections(new String[]{"default", "none", "singleLine", "singleLineEtched"}));
        addSupportedChild("separatorColor", Elements.Color);
        addSupportedAttribute("minX", Values.Integer);
        addSupportedAttribute("minY", Values.Integer);
        addSupportedAttribute("maxX", Values.Integer);
        addSupportedAttribute("maxY", Values.Integer);
        addSupportedAttribute("showsSelectionImmediatelyOnTouchBegin", Values.Boolean);
        addSupportedAttribute("sectionIndexMinimumDisplayRowCount", Values.Integer);
        addSupportedAttribute("rowHeight", Values.Float);
        addSupportedAttribute("sectionFooterHeight", Values.Float);
        addSupportedAttribute("sectionHeaderHeight", Values.Float);
        addSupportedAttribute("estimatedRowHeight", Values.Float);
        addSupportedAttribute("dataMode", Values.String);

        addSupportedAttribute("allowsSelection", Values.Boolean);

        addSupportedChild(Elements.Prototypes);
        addSupportedChild(Elements.Sections);
        addSupportedChild("separatorInset", Elements.Inset);
        addSupportedChild("sectionIndexColor", Elements.Color);
        addSupportedChild("sectionIndexBackgroundColor", Elements.Color);
        addSupportedChild("sectionIndexTrackingBackgroundColor", Elements.Color);

    }

    @Override
    protected String constructor() {
        StringBuilder out = new StringBuilder(super.constructor());
        String dataMode = attr("dataMode") == null ? "" : attrName("dataMode");
        out.append(";").append(NEWLINE);
        if (dataMode.equals("prototypes")) {
            for (Prototypes prototypes : parts(Elements.Prototypes))
                for (TableViewCell tableViewCell : prototypes.parts(Elements.TableViewCell))
                    out.append(tableViewCell.toCode(variable())).append(";").append(NEWLINE);
        }
        return out.toString();
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();
        String dataMode = attr("dataMode") == null ? "" : attrName("dataMode");
        appendValue(out, "separatorColor");
        if (dataMode.equals("static")) {
            Collection<Sections> parts = parts(Elements.Sections);
            if (parts != null && parts.size() <= 1 && !parts.isEmpty())
                out.append(parts.iterator().next().addSection());
        }
        out.append(super.toCode());
        appendAttribute(out, "rowHeight");
        appendAttribute(out, "allowsSelection");
        return out.toString();
    }

}
