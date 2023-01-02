/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Values;

public class Stepper extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("value", Values.Integer);
        addSupportedAttribute("minimumValue", Values.Integer);
        addSupportedAttribute("maximumValue", Values.Integer);
        addSupportedAttribute("stepValue", Values.Integer);
        addSupportedAttribute("continuous", Values.Boolean);
        addSupportedAttribute("autorepeat", Values.Boolean);
        addSupportedAttribute("wraps", Values.Boolean);

    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
