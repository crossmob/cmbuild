// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;

public class TextAttributes extends Element {

    @Override
    protected void addSupported() {
        //TODO Attributed text
        addSupportedAttribute("key", Values.String);
        addSupportedChild("textColor", Elements.Color);
        addSupportedChild("textShadowOffset", Elements.OffsetWrapper);
    }

    @Override
    public String toCode() {
        return "";
    }

}
