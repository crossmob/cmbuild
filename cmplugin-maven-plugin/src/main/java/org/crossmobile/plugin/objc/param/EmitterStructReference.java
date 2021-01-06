/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;

import static org.crossmobile.plugin.utils.Factories.getDereferType;
import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.utils.NamingUtils.getClassNameSimple;
import static org.crossmobile.utils.TextUtils.TAB;

class EmitterStructReference extends Emitter {

    private final EmitterCType internal;
    private final NType deref;
    private final boolean constant;

    EmitterStructReference(NParam param, boolean forward) {
        super(param.getName(), param.getVarname(), param.getNType(), true, forward);
        deref = getDereferType(param.getNType());
        internal = new EmitterCType(param.getName(), param.getVarname(), deref, false, false, forward);
        constant = param.getNType().isConstant();
    }

    @Override
    protected String initForward() {
        return toObjC(getClassNameSimple(deref.getType())) + " " + paramVar() + ";\n"
                + "if (" + givenVar() + " != JAVA_NULL)\n"
                + TAB + paramVar() + " = " + internal.embedForward() + ";\n";
    }

    @Override
    protected String embedForward() {
        return "(" + givenVar() + " == JAVA_NULL ? NULL : &" + paramVar() + ")";
    }

    @Override
    protected String destroyForward() {
        return constant
                ? ""
                : "if (" + givenVar() + " != JAVA_NULL) \n"
                + TAB + "[" + givenVar() + " set" + toObjC(getClassNameSimple(deref.getType())) + ":" + paramVar() + "];\n";
    }

}
