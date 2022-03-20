/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.helper.Objects;

public class TableViewController extends ViewController {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedChild("view", Elements.TableView);
    }

    @Override
    protected void constructorOverrides(StringBuilder out) {
        String view = (item("view") != null) ? ((View) item("view")).variable() : "";
        if (!view.equals("")) {
            out.append(NEWLINE);
            out.append(I3).append("protected void loadViewFromStoryboard() {").append(NEWLINE);
            out.append(I4).append("setView(tableView());").append(NEWLINE);
            out.append(I4).append("tableView().reloadData();").append(NEWLINE);
            out.append(I4).append(Objects.LATE_INITTER).append(view).append("();").append(NEWLINE);
            out.append(I3).append("}").append(NEWLINE);
            out.append(I3).append("@Override").append(NEWLINE);
            out.append(I3).append("public UITableView tableView() {").append(NEWLINE);
            out.append(I4).append("return ").append(Objects.GETTER).append(view).append("();").append(NEWLINE);
            out.append(I3).append("}").append(NEWLINE);
        }
    }


}
