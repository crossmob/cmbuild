/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm.models.parameters;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.robovm.ClassBuilder;
import org.crossmobile.plugin.utils.WaterPark;
import org.crossmobile.utils.Log;

public class StrongRParam extends RParam {
    private final String nativeS;

    public StrongRParam(NParam nParam, Class<?> parrameter, NType type, WaterPark wp) {
        super(nParam, parrameter, type);
        if (!wp.contains(parrameter.getName()))
            Log.error("Water Park does not contain : " + parrameter.getName());
        nativeS = wp.get(super.getNative() + "$" + getType().getType().getSimpleName() + "Ptr").getName();
    }

    @Override
    public String getNative() {
        return nativeS;
    }


    @Override
    public String conversion() {
        return getNative() + " " + reference() + "$strong = new " + getNative() + "();\n" + reference() + "$strong.set(" + reference() + ".get());";
    }

    @Override
    public String convRef() {
        return reference() + "$strong";
    }
}
