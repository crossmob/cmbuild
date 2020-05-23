/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.Objects;
import org.crossmobile.build.ib.helper.RealElement;

public class NavigationItem extends RealElement {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("title", Values.LocalizedString);
        addSupportedChild("rightBarButtonItem", Elements.BarButtonItem);
        addSupportedChild("leftBarButtonItem", Elements.BarButtonItem);
        addSupportedChild("titleView", Elements.View);
    }

    public String toCode() {
        StringBuilder out = new StringBuilder();
        appendAttributeTo(out, "navigationItem()", "title");
        if (item("rightBarButtonItem") != null)
            appendTo(out, "navigationItem()", "setRightBarButtonItem", Objects.GETTER + ((BarButtonItem) item("rightBarButtonItem")).variable() + "()");
        if (item("leftBarButtonItem") != null)
            appendTo(out, "navigationItem()", "setLeftBarButtonItem", Objects.GETTER + ((BarButtonItem) item("leftBarButtonItem")).variable() + "()");
        if (item("titleView") != null)
            appendTo(out, "navigationItem()", "setTitleView", Objects.GETTER + ((View) item("titleView")).variable() + "()");
        return out.toString();
    }
}
