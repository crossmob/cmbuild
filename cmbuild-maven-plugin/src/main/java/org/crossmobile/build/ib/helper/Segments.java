/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Elements;

public class Segments extends RealElement {

    @Override
    protected void addSupported() {
        addSupportedChild(Elements.Segment);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder("Arrays.asList(new Object[]{");
        String comma = "";
        for (Segment segment : parts(Elements.Segment)) {
            out.append(comma).append(NEWLINE).append(I5).append(segment.toCode());
            comma = ",";
        }
        out.append(NEWLINE).append(I4).append("})");
        return out.toString();
    }
}
