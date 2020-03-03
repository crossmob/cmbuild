// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.model;

public enum VarargType {
    /**
     * Null terminated vararg; all elements of the same type
     */
    OBJC(false, false),
    /**
     * One Object as head, following by an arbitrary number of unknown objects. This type could be only derived and not
     * specifically defined
     */
    OBJCVIRTUAL(false, true),
    /**
     * Null terminated C-typed varargs
     */
    C(true, false),
    /**
     * One object as head, following by an arbitrary number of unknown objects. This type could be only derived and not
     * specifically defined
     */
    CVIRTUAL(true, true);

    /**
     * Vararg in a C-style function
     */
    public final boolean function;
    /**
     * Vararg is virtual; no definition available, thus types need to be found   
     */
    public final boolean virtual;

    VarargType(boolean function, boolean virtual) {
        this.function = function;
        this.virtual = virtual;
    }
}
