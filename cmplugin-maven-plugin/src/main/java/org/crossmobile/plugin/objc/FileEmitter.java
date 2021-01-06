/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Statics;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;

import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.utils.NamingUtils.*;

public abstract class FileEmitter {

    protected final NObject obj;
    protected final Registry reg;

    public FileEmitter(NObject obj, Registry reg) {
        this.obj = obj;
        this.reg = reg;
    }

    public void emitInfo(Streamer out) throws IOException {
        out.append(Statics.COPYRIGHT);
        out.append("// ").append(toObjC(obj.getType())).append(" ").append(getFileType()).append("\n\n");
    }

    public void emitEnd(Streamer out, boolean extraSpace) throws IOException {
        out.append("@end\n");
        if (extraSpace)
            out.append("\n");
    }

    public abstract String getFileType();

    protected String simpleName() {
        return getClassNameSimple(obj.getType());
    }

    protected String fullName() {
        return fullName(obj.getType());
    }

    protected String fullName(Class cls) {
        return toObjC(getClassNameFull(cls));
    }

}
