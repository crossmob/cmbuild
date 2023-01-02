/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.helper.Connections;
import org.crossmobile.build.ib.helper.Segue;

public class TabBarController extends ViewController {

    @Override
    protected void addSupported() {
        super.addSupported();
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        out.append(I4).append("List<UIViewController> vcList = new ArrayList<>();").append(NEWLINE);
        for (Connections connection : this.parts(Elements.Connections))
            for (Segue segway : connection.parts(Elements.Segue))
                out.append(I4).append("vcList.add(new ").append(variableFromID(segway.getDestination())).append("());").append(NEWLINE);
        out.append(I4).append("setViewControllers(vcList);").append(NEWLINE);
        return out.toString();
    }

}
