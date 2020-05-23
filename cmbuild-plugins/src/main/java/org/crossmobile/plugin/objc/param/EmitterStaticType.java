/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.model.StaticMappingType;

class EmitterStaticType extends Emitter {

    private final boolean nativeType;

    EmitterStaticType(NParam param, boolean forward) {
        this(param.getNType(), param.getVarname(), param.getStaticMapping() == StaticMappingType.NATIVE, forward);
    }

    EmitterStaticType(NType type, String paramName, boolean nativeType, boolean forward) {
        super("", nativeType ? "self" : paramName, type, false, forward);
        this.nativeType = nativeType;
    }

    @Override
    public String getInstanceName() {
        return nativeType
                ? super.getInstanceName()
                : "(" + givenVar() + " == JAVA_NULL ? nil : " + givenVar() + ")";
    }

    @Override
    public boolean isParameterHidden() {
        return !nativeType;
    }

}
