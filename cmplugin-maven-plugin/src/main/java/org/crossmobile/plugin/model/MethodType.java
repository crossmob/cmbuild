/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.model;

public enum MethodType {
    SELECTOR,
    FUNCTION,
    EXTERNAL,
    BLOCK,
    VA_SELECTOR,
    VA_FUNCTION;

    public boolean isVarArgs() {
        return this == VA_SELECTOR || this == VA_FUNCTION;
    }

    public boolean isExternal() {
        return this == EXTERNAL;
    }

    public boolean isFunction() {
        return this == FUNCTION || this == VA_FUNCTION;
    }
}
