/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.RealElement;
import org.crossmobile.utils.Log;

public class TabBarItem extends RealElement {

    @Override
    protected void addSupported() {
        addSupportedAttribute("image", Values.String);
        addSupportedAttribute("catalog", Values.String);
        addSupportedAttribute("title", Values.String);
        addSupportedAttribute("tag", Values.Integer);
        addSupportedAttribute("key", Values.String);
        super.addSupported();
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();
        out.append(I4).append("setTabBarItem(");
        out.append("new UITabBarItem(");
        out.append(attr("title", "null")).append(",");

        String catalog = attrName("catalog");
        if (catalog != null) {
            out.append("null, ");
            Log.warning("Catalog images for Tab Bar Items not supported yet");
        } else
            out.append("UIImage.imageNamed(").append(attr("image", "null")).append("), ");
        String tag = attrName("tag");
        out.append(tag == null ? "0" : tag);
        out.append("));").append(NEWLINE);
        return out.toString();
    }

}
