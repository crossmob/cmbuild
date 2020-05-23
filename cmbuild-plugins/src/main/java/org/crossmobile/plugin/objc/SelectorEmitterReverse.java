/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.objc.param.ParamEmitter;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;

import static org.crossmobile.utils.CollectionUtils.forEach;
import static org.crossmobile.utils.NamingUtils.execSignature;

public class SelectorEmitterReverse extends SelectorEmitter {

    private final String blockvar;
    private final ReverseImportRegistry handleRegistry;

    public SelectorEmitterReverse(NSelector selector, ReverseImportRegistry handleRegistry) {
        this(selector, null, handleRegistry);
    }

    SelectorEmitterReverse(NSelector selector, String blockvar, ReverseImportRegistry handleRegistry) {
        super(selector);
        this.blockvar = blockvar;
        this.handleRegistry = handleRegistry;
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
        return ParamEmitter.reverse(selector, handleRegistry, blockvar);
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
