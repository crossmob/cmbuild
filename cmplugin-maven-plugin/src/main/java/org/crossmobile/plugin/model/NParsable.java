/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.model;

public class NParsable {

    private String originalCode;

    public void setOriginalCode(String originalCode) {
        this.originalCode = originalCode == null ? "<unknown>" : originalCode.trim();
    }

    public String getOriginalCode() {
        return originalCode;
    }

}
