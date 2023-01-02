/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Values;

public class Window extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("visibleAtLaunch", Values.Boolean);
        addSupportedAttribute("resizesToFullScreen", Values.Boolean);
    }

    @Override
    public String toCode() {
        @SuppressWarnings("ReplaceStringBufferByString")
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
