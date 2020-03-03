// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class Toolbar extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("barStyle", new Value.Selections(new String[]{"default", "black", "blackTransucent"}));
        addSupportedAttribute("translucent", Values.Boolean);
        addSupportedChild("barTintColor", Elements.Color);

    }

    @Override
    public String toCode() {
        @SuppressWarnings("ReplaceStringBufferByString")
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
