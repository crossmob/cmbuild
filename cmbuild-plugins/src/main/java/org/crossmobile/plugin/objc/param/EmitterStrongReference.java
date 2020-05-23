/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import crossmobile.rt.StrongReference;
import org.crossmobile.plugin.model.NParam;

import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.plugin.utils.Texters.toObjCType;
import static org.crossmobile.utils.TextUtils.TAB;

class EmitterStrongReference extends Emitter {

    EmitterStrongReference(NParam param, boolean forward) {
        super(param.getName(), param.getVarname(), param.getNType(), true, forward);
    }

    @Override
    protected String initForward() {
        return givenVar() + " = " + givenVar() + " == JAVA_NULL ? nil : " + givenVar() + ";\n"
                + "id " + paramVar() + " = nil;\n";
    }

    @Override
    protected String embedForward() {
        return "(" + givenVar() + " ? &" + paramVar() + " : nil)";
    }

    @Override
    protected String destroyForward() {
        return "if (" + givenVar() + ")\n"
                + TAB + "[" + givenVar() + " set___java_lang_Object:(" + paramVar() + " ? " + paramVar() + " : JAVA_NULL)];\n";
    }

    @Override
    protected String initReverse() {
        return toObjCType(StrongReference.class) + " " + paramVar() + " = " + givenVar() + " ? [["
                + toObjC(StrongReference.class)
                + " alloc] __init_crossmobile_ios_StrongReference___java_lang_Object:*" + givenVar() + "] : JAVA_NULL;\n";
    }

    @Override
    protected String destroyReverse() {
        return "[" + paramVar() + " release];\n";
    }

}
