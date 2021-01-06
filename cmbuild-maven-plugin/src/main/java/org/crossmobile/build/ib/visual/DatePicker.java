/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Elements;
import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class DatePicker extends Control {

    @Override
    protected void addSupported() {
        super.addSupported();

        addSupportedAttribute("minuteInterval", Values.Integer);
        addSupportedAttribute("useCurrentDate", Values.Boolean);
        addSupportedAttribute("countDownDuration", Values.Integer);
        addSupportedAttribute("datePickerMode", new Value.Selections(new String[]{"time", "date", "dateAndTime", "countDownTimer"}));

        addSupportedChild("date", Elements.Date);
        addSupportedChild("minimumDate", Elements.Date);
        addSupportedChild("maximumDate", Elements.Date);
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
