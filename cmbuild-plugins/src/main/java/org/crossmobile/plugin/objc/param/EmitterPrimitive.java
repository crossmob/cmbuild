// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;

import static org.crossmobile.plugin.reg.TypeRegistry.getJavaBoxed;
import static org.crossmobile.plugin.reg.TypeRegistry.getObjCUnboxed;
import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.utils.ReflectionUtils.getBareClass;

class EmitterPrimitive extends Emitter {

    private final boolean boxed;
    private final String boxedType;
    private final String boxedName;

    EmitterPrimitive(NParam param, boolean forward) {
        this(param.getName(), param.getVarname(), param.getNType(), !param.getJavaParameter().getType().isPrimitive(), forward);
    }

    EmitterPrimitive(String paramName, String varName, NType type, boolean boxed, boolean forward) {
        super(paramName, varName, type, boxed, forward);
        this.boxed = boxed;
        this.boxedType = toObjC(getJavaBoxed(type.getType()));
        this.boxedName = getObjCUnboxed(type.getType());
    }

    @Override
    protected String embedForward() {
        return boxed
                ? "[" + givenVar() + " unbox]"
                : super.embedForward();
    }

    @Override
    protected String initReverse() {
        return boxed
                ? boxedType + "* " + paramVar() + " = [[" + boxedType + " alloc] initWith" + boxedName + ":" + givenVar() + "];\n"
                : super.initReverse();
    }

    @Override
    protected String destroyReverse() {
        return boxed
                ? "[" + paramVar() + " release];\n"
                : super.destroyReverse();
    }

}
