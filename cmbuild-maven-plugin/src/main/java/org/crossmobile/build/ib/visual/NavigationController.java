// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.helper.Connections;
import org.crossmobile.build.ib.helper.Segue;

public class NavigationController extends ViewController {

    private String destination = "";

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedChild(Elements.Connections);
        addSupportedChild("navigationBar", Elements.NavigationBar);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());
        NavigationBar navigationBar;
        if ((navigationBar = (NavigationBar) item("navigationBar")) != null)
            out.append(navigationBar.toCode(variable()));
        return out.toString();
    }

    @Override
    protected String toCodeSuper() {
        for (Connections c : parts(Elements.Connections))
            for (Segue s : c.parts(Elements.Segue))
                destination = (s.getKind().equals("relationship") && s.getRelation().equals("rootViewController")) ? s.getDestination() : destination;
        return destination.isEmpty() ? "" : "super(new " + variableFromID(destination) + "());" + NEWLINE;
    }
}
