/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class WebView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("scalesPageToFit", Values.Boolean);
        addSupportedAttribute("phoneNumber", Values.Boolean);
        addSupportedAttribute("calendarEvent", Values.Boolean);
        addSupportedAttribute("keyboardDisplayRequiresUserAction", Values.Boolean);
        addSupportedAttribute("mediaPlaybackRequiresUserAction", Values.Boolean);
        addSupportedAttribute("mediaPlaybackAllowsAirPlay", Values.Boolean);
        addSupportedAttribute("suppressesIncrementalRendering", Values.Boolean);
        addSupportedAttribute("keyboardDisplayRequiresUserAction", Values.Boolean);
        addSupportedAttribute("paginationMode", new Value.Selections(new String[]{"unpaginated", "leftToRight", "topToBottom", "bottomTo Top", "rightToLeft"}));
        addSupportedAttribute("paginationBreakingMode", new Value.Selections(new String[]{"page", "column"}));
        addSupportedChild("dataDetectorTypes", Elements.DataDetectorType);

    }

    @Override
    public String toCode() {
        @SuppressWarnings("ReplaceStringBufferByString")
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
