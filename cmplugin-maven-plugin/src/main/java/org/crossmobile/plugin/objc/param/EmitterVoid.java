/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NType;

public class EmitterVoid extends Emitter {

    public static final Emitter TYPE = new EmitterVoid();

    private EmitterVoid() {
        super("", "", new NType("void", Void.TYPE), false, false);
    }

    @Override
    protected String execForward(String caller, boolean needsDestroy) {
        return caller + ";\n";
    }

    @Override
    protected String execReverse(String caller, boolean needsDestroy) {
        return execForward(caller, needsDestroy);
    }

    @Override
    protected String resultForward(String exec, boolean needsDestroy) {
        return "";
    }

    @Override
    protected String resultReverse(String exec, boolean needsDestroy) {
        return "";
    }

}
