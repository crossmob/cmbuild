/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;

class EmitterStringToChar extends Emitter {

    EmitterStringToChar(NParam param, boolean forward) {
        this(param.getName(), param.getVarname(), param.getNType(), forward);
    }

    EmitterStringToChar(String paramName, String varName, NType type, boolean forward) {
        super(paramName, varName, type, true, forward);
    }

    @Override
    protected String embedForward() {
        return "[" + givenVar() + " UTF8String]";
    }

    @Override
    protected String initReverse() {
        return "NSString* " + paramVar() + " = [[NSString alloc] initWithUTF8String:" + givenVar() + "];\n";
    }

    @Override
    protected String destroyReverse() {
        return "[" + paramVar() + " release];\n";
    }

}
