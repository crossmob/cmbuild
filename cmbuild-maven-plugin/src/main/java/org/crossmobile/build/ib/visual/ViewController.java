/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Values;
import org.crossmobile.build.ib.helper.LayoutGuides;
import org.crossmobile.build.ib.helper.Objects;
import org.crossmobile.build.ib.helper.RealElement;

public class ViewController extends RealElement {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("storyboardIdentifier", Values.String);
        addSupportedAttribute("sceneMemberID", Values.String);
        addSupportedAttribute("restorationIdentifier", Values.String);
        addSupportedAttribute("automaticallyAdjustsScrollViewInsets", Values.Boolean);
        addSupportedChild("view", Elements.View);
        addSupportedAttribute("title", Values.LocalizedString);
        addSupportedChild("navigationItem", Elements.NavigationItem);
        addSupportedChild("tabBarItem", Elements.TabBarItem);
        addSupportedChild(Elements.LayoutGuides);

    }

    public String toCodeBare() {
        if (attrName("sceneMemberID") != null && !attrName("sceneMemberID").equals(""))
            Objects.SCENE_VC = variable();
        StringBuilder out = new StringBuilder();
        out.append(toCodeSuper());
        out.append(printConnections(variable() + ".this"));
        for (LayoutGuides guide : parts(Elements.LayoutGuides))
            out.append(guide.toCode());
        return out.toString();
    }

    @Override
    public String toCode() {
        Objects.addVC(attrName("storyboardIdentifier"), variable());
        StringBuilder out = new StringBuilder(toCodeBare());
        Element navItem;
        if ((navItem = item("navigationItem")) != null)
            out.append(navItem.toCode());
        Element tabItem;
        if ((tabItem = item("tabBarItem")) != null)
            out.append(tabItem.toCode());
        appendAttributeCall(out, "tabBarItem");
        appendAttributeCall(out, "title");
        appendAttributeCall(out, "restorationIdentifier");
        out.append(I4).append("org.crossmobile.build.StoryBoardBinder.bindStoryboardWithViewController(").append(variable()).append(".this, ").append("get_Storyboard());").append(NEWLINE);
        out.append(segueMap());

        return out.toString();
    }

    @Override
    protected String publicConstructor() {
        StringBuilder out = new StringBuilder();
        out.append("private ").append(getClassName()).append(" ").append(Objects.GETTER).append(variable()).append("() {").append(NEWLINE);
        out.append(I4).append("return this;").append(NEWLINE);
        out.append(I3).append("}").append(NEWLINE).append(I3);
        constructorOverrides(out);
        out.append(super.publicConstructor());
        return out.toString();
    }

    protected void constructorOverrides(StringBuilder out) {
        String view = (item("view") != null) ? ((View) item("view")).variable() : "";
        if (!view.equals("")) {
            out.append(NEWLINE);
            out.append(I3).append("protected void loadViewFromStoryboard() {").append(NEWLINE);
            out.append(I4).append("setView(").append(Objects.GETTER).append(view).append("());").append(NEWLINE);
            out.append(I4).append(Objects.LATE_INITTER).append(view).append("();").append(NEWLINE);
            out.append(I4).append("view().layoutSubviews();").append(NEWLINE);
            out.append(I3).append("}").append(NEWLINE);
        }
    }

    private String segueMap() {
        View view = (View) item("view");
        return view == null ? "" : view.segueMap(variable());
    }

    protected String toCodeSuper() {
        return "";
    }
}
