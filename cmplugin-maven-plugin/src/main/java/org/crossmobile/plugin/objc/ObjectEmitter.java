/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ObjectEmitter {

    private final NObject obj;
    private final Registry reg;

    public ObjectEmitter(NObject obj, Registry reg) {
        this.obj = obj;
        this.reg = reg;
    }

    public void emitAndTerminate(Streamer header, Streamer body, boolean asImportHeaders, String... imports) throws IOException {
        emit(header, body, null, asImportHeaders, imports);
        if (header != null)
            header.close();
        if (body != null)
            body.close();
    }

    public void emit(Streamer header, Streamer body, String[] filter, boolean asImportHeaders, String... imports) throws IOException {
        new HeaderEmitter(obj, reg, asImportHeaders, imports).emit(header);
        if (!obj.getType().isInterface() && body != null)
            new BodyEmitter(obj, reg).emit(body, filter);
    }

}
