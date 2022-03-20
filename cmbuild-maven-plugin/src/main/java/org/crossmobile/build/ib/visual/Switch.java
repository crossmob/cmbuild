/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class Switch extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("on", Values.Boolean);
        addSupportedChild("onTintColor", Elements.Color);
        addSupportedChild("thumbTintColor", Elements.Color);

    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
