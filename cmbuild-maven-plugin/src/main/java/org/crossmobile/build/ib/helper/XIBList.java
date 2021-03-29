/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.i18n.IBParserMeta;
import org.crossmobile.build.utils.Config;
import org.crossmobile.build.utils.Templates;

public class XIBList extends Element {
    public XIBList(IBParserMeta meta) {
        super(meta);
    }

    @Override
    protected void addSupported() {
        addSupportedChild(Elements.Objects);
        addSupportedChild(Elements.XibClassStart);
        addSupportedChild(Elements.XibClassEnd);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();
        out.append(Templates.AUTOGEN_TEMPLATE);
        out.append("package " + Config.DYNAMIC_CONTENT_PACKAGE + ";").append(NEWLINE);
        out.append(NEWLINE);
        out.append("import crossmobile.ios.uikit.*;").append(NEWLINE);
        out.append("import crossmobile.ios.foundation.*;").append(NEWLINE);
        out.append("import crossmobile.ios.coregraphics.*;").append(NEWLINE);
        out.append("import java.util.*;").append(NEWLINE);
        out.append(NEWLINE);
        out.append("public class IBObjects {").append(NEWLINE);

        for (Objects item : parts(Elements.Objects))
            if (!item.filenameIsSet())
                out.append(item.toCode());
        for (Objects item : parts(Elements.Objects))
            if (item.filenameIsSet())
                out.append(item.toCode());
        out.append("}").append(NEWLINE);
        return out.toString();
    }

    public int countItems() {
        return parts(Elements.Objects).size();
    }


}
