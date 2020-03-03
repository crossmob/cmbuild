// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.utils.ReflectionUtils;

import java.lang.reflect.Method;

import static org.crossmobile.plugin.utils.Texters.methodObjCName;

class EmitterSelector extends Emitter {

    private final String selector;

    EmitterSelector(NParam param, boolean forward) {
        super(param.getName(), param.getVarname(), param.getNType(), false, forward);
        Class baseClass = param.getAffiliation() != null
                ? param.getAffiliation().getParameter().getJavaParameter().getType()
                : param.getJavaParameter().getType();
        Method m = ReflectionUtils.getLambdaMethod(baseClass);
        StringBuilder sel = new StringBuilder();
        sel.append(methodObjCName(m));
        for (int i = 0; i < m.getParameterCount(); i++)
            sel.append(":");
        selector = sel.toString();
    }

    @Override
    protected String embedForward() {
        return "@selector(" + selector + ")";
    }

    @Override
    public String getInstanceName() {
        return isForward
                ? "(" + givenVar() + " == JAVA_NULL ? nil : " + givenVar() + ")"
                : "(" + givenVar() + " ? JAVA_NULL : " + givenVar() + ")";
    }

}
