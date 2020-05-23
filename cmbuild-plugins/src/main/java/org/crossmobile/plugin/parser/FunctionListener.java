/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.crossmobile.plugin.model.MethodType;
import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.model.VarargType;
import org.crossmobile.plugin.parser.antlr.CMAnnotParser;
import org.crossmobile.plugin.parser.antlr.CMAnnotParser.Var_type_nameContext;
import org.crossmobile.plugin.utils.Factories;

import java.util.List;

class FunctionListener extends BaseListener<NSelector> {

    public FunctionListener() {
        super(new NSelector());
    }

    @Override
    ParseTree getTree(CMAnnotParser parser) {
        return parser.cparts();
    }

    @Override
    public void setOriginalCode(String code) {
        data.setOriginalCode(code);
    }

    @Override
    public void exitFunc(CMAnnotParser.FuncContext ctx) {
        initFunc(ctx.vartype(), ctx.name.getText(), MethodType.FUNCTION);
        List<Var_type_nameContext> params = ctx.listofvariables().var_type_name();
        if (params != null && !params.isEmpty())
            for (Var_type_nameContext typename : params) {
                NParam p = new NParam();
                p.setNType(Factories.getType(typename.vartype(), typename.varargs == null ? null : VarargType.C, typename.s3 != null));
                p.setVarname(typename.varname.getText());
                data.addParam(p);
            }
    }

    @Override
    public void exitExtconst(CMAnnotParser.ExtconstContext ctx) {
        initFunc(ctx.vartype(), ctx.name.getText(), MethodType.EXTERNAL);
    }

    private void initFunc(CMAnnotParser.VartypeContext vartype, String name, MethodType methodType) {
        data.setReturnType(Factories.getType(vartype));
        data.setName(name);
        data.setStatic(true);
        data.setMethodType(methodType);
        found();
    }

}
