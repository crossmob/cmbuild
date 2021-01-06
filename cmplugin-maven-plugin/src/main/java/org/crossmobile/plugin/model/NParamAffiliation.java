/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.model;

public class NParamAffiliation {

    private final NParam parameter;
    private final Type type;

    public NParamAffiliation(NParam parameter, Type type) {
        this.parameter = parameter;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public NParam getParameter() {
        return parameter;
    }

    public static enum Type {
        SELECTOR,
        MEMBLOCK;
    }
}
