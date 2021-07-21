/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;

class EmitterMemSize extends Emitter {

    private final String init;

    EmitterMemSize(NParam param, boolean forward) {
        super(param.getName(), param.getVarname(), param.getNType(), false, forward);
        String varname = param.getAffiliation().getParameter().getVarname();
        if (param.getAffiliation().getParameter().getJavaType().equals(String.class))
            init = "[" + varname + " length]";
        else
            init = "(" + varname + " == JAVA_NULL ? 0 : " + varname + "->length)";
    }

    @Override
    protected String embedForward() {
        return init;
    }

}
