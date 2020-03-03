// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Values;

public class Color extends Element {

    @Override
    protected void addSupported() {
        addSupportedAttribute("key", Values.String);
        addSupportedAttribute("red", Values.Float);
        addSupportedAttribute("green", Values.Float);
        addSupportedAttribute("blue", Values.Float);
        addSupportedAttribute("white", Values.Float);
        addSupportedAttribute("alpha", Values.Float);
        addSupportedAttribute("cocoaTouchSystemColor", Values.Method);
        addSupportedAttribute("colorSpace", Values.String);
        addSupportedAttribute("customColorSpace", Values.String);
    }

    @Override
    public String toCode() {
        String value = attr("white");
        if (value != null)
            return "UIColor.colorWithWhiteAlpha(" + value + ", " + attr("alpha") + ")";
        if ((value = attr("red")) != null)
            return "UIColor.colorWithRedGreenBlueAlpha(" + value + ", " + attr("green") + ", " + attr("blue") + ", " + attr("alpha") + ")";
        if ((value = attr("cocoaTouchSystemColor")) != null)
            return "UIColor." + value;
        return "UIColor.white()";   // failsafe
    }
}
