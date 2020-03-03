// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;

import static org.crossmobile.utils.CollectionUtils.forEach;

public class SelectorEmitterBlock extends SelectorEmitterReverse {

    public SelectorEmitterBlock(NSelector selector, String blockvar) {
        super(selector, blockvar, null);
    }

    @Override
    protected void emitDefinition(Streamer out) throws IOException {
        forEach(selector.getParams()).onHead(p -> out.append("(")).
                onTail(p -> out.append(", ")).
                onAny(p -> out.append(p.getNType().getNativeType()).append(" ").append(p.getVarname())).
                onLast(p -> out.append(") ")).
                go();
    }

    @Override
    protected void emitOpenBracket(Streamer out) throws IOException {
        out.append("{\n").tab();
    }

    @Override
    protected void emitCloseBracket(Streamer out) throws IOException {
        out.untab().append("}");
    }

}
