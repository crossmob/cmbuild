/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.crossmobile.plugin.parser.antlr.CMAnnotBaseListener;
import org.crossmobile.plugin.parser.antlr.CMAnnotParser;
import org.crossmobile.plugin.reg.Registry;


abstract class BaseListener<T> extends CMAnnotBaseListener {

    public T data;
    private boolean found = false;
    protected final Registry reg;

    public BaseListener(T data, Registry reg) {
        this.data = data;
        this.reg = reg;
    }

    public void found() {
        this.found = true;
    }

    public T foundData() {
        return found ? data : null;
    }

    abstract ParseTree getTree(CMAnnotParser parser);

    abstract void setOriginalCode(String code);

}
