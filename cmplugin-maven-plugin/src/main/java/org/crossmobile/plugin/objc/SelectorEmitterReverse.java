/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.objc.param.ParamEmitter;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;

import static org.crossmobile.utils.CollectionUtils.forEach;
import static org.crossmobile.utils.NamingUtils.execSignature;

public class SelectorEmitterReverse extends SelectorEmitter {

    private final String blockvar;
    private final Registry reg;

    public SelectorEmitterReverse(NSelector selector, Registry reg) {
        this(selector, null, reg);
    }

    SelectorEmitterReverse(NSelector selector, String blockvar, Registry reg) {
        super(selector, reg);
        this.blockvar = blockvar;
        this.reg = reg;
    }

    @Override
    protected void emitDefinition(Streamer out) throws IOException {
        out.append(selector.isStatic() ? "+" : "-").append(" (");
        out.append(selector.getReturnType().getNativeType()).append(") ").append(selector.getName());
        forEach(selector.getParams()).
                onTail(p -> out.append(" ")).
                onAny(p -> out.append(p.getName()).append(":").append("(").append(p.getNType().getNativeType()).append(")").append(" ").append(p.getVarname())).
                go();
    }

    @Override
    protected ParamEmitter getParams() {
        return ParamEmitter.reverse(selector, reg, blockvar);
    }

    @Override
    protected String getOriginalCode() {
        return execSignature(selector.getJavaExecutable(), false) + ";";
    }

    @Override
    protected void emitAssociate(Streamer out) throws IOException {
    }

    @Override
    protected void emitBreakEarly(Streamer out) throws IOException {
    }

}
