/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.objc.SelectorEmitterBlock;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Streamer;
import org.crossmobile.plugin.utils.Texters;

import java.io.IOException;

class EmitterBlock extends Emitter {

    private final NSelector block;
    private final Registry reg;
    private final Class<?> containerObject;
    private final String containerSelector;

    EmitterBlock(NParam param, Registry reg, boolean forward) {
        this(param.getName(), param.getVarname(), param.getNType(), reg,
                param.getContainer().getContainer().getType(),
                Texters.getSelectorSignature(param.getContainer()),
                forward);
    }

    EmitterBlock(String varName, NType type, boolean forward) {
        this("", varName, type, null, null, null, forward);
    }

    private EmitterBlock(String paramName, String varName, NType type, Registry reg, Class<?> containerObject, String containerSelector, boolean forward) {
        super(paramName, varName, type, true, forward);
        this.reg = reg;
        this.containerObject = containerObject;
        this.containerSelector = containerSelector;
        if (type.getBlock() == null) {
            NObject nobj = reg.objects().retrieve(type.getType());
            this.block = nobj == null || nobj.getSelectors().isEmpty() ? null : nobj.getSelectors().iterator().next();
        } else
            this.block = type.getBlock();
    }

    @Override
    protected String embedForward() {
        if (block == null)
            return "";
        Streamer out = Streamer.asString();
        try {
            new SelectorEmitterBlock(block, givenVar(), reg).emitImplementation(out);
            return "(" + givenVar() + " == JAVA_NULL ? nil : ^" + out + ")";
        } catch (IOException ex) {
            return "";
        }
    }

    @Override
    protected String initReverse() {
        String randomClass = reg.imports().requestRandomClass(containerObject, containerSelector, block);
        return randomClass + "* " + paramVar() + " = [[" + randomClass + " alloc] initWithCMBlock:" + givenVar() + "];\n";
    }

    @Override
    protected String destroyReverse() {
        return "[" + paramVar() + " release];\n";
    }
}
