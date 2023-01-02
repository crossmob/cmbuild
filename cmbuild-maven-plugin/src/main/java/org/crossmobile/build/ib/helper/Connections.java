/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;

public class Connections extends Element {

    @Override
    protected void addSupported() {
        addSupportedChild(Elements.Outlet);
        addSupportedChild(Elements.Action);
        addSupportedChild(Elements.Segue);
    }

    @Override
    public String toCode() {
        return "";
    }

    public String getOutlets(String variableName, String variableType) {
        StringBuilder out = new StringBuilder();
        for (Outlet o : parts(Elements.Outlet))
            out.append(o.getOutlet(variableName, variableType));
        return out.toString();
    }

    public String getActions(String variable) {
        StringBuilder out = new StringBuilder();
        for (Action o : parts(Elements.Action))
            out.append(o.addTarget(variable));
        return out.toString();
    }

    public String getSegues(String variable) {
        StringBuilder out = new StringBuilder();
        for (Segue o : parts(Elements.Segue))
            out.append(o.getTarget(variable));
        return out.toString();
    }

}
