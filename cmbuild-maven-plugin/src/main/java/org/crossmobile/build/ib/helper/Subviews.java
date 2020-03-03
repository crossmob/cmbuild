// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.ib.helper;

import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.visual.View;

public class Subviews extends Element {

    @Override
    protected void addSupported() {
        addSupportedChild(Elements.ActivityIndicatorView);
        addSupportedChild(Elements.AdBannerView);
        addSupportedChild(Elements.Button);
        addSupportedChild(Elements.Control);
        addSupportedChild(Elements.DatePicker);
        addSupportedChild(Elements.ImageView);
        addSupportedChild(Elements.Label);
        addSupportedChild(Elements.MapView);
        addSupportedChild(Elements.NavigationBar);
        addSupportedChild(Elements.PickerView);
        addSupportedChild(Elements.PageControl);
        addSupportedChild(Elements.ProgressView);
        addSupportedChild(Elements.ScrollView);
        addSupportedChild(Elements.SearchBar);
        addSupportedChild(Elements.SegmentedControl);
        addSupportedChild(Elements.Slider);
        addSupportedChild(Elements.StackView);
        addSupportedChild(Elements.Stepper);
        addSupportedChild(Elements.Switch);
        addSupportedChild(Elements.TabBar);
        addSupportedChild(Elements.TabBarItem);
        addSupportedChild(Elements.TableView);
        addSupportedChild(Elements.TableViewCell);
        addSupportedChild(Elements.TextField);
        addSupportedChild(Elements.TextView);
        addSupportedChild(Elements.Toolbar);
        addSupportedChild(Elements.View);
        addSupportedChild(Elements.WebView);
        addSupportedChild(Elements.Window);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder();
        for (View item : parts(Elements.View))
            out.append(item.toCode());
        return out.toString();
    }

}
