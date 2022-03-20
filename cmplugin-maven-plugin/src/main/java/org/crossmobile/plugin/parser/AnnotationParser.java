/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.crossmobile.plugin.model.NProperty;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.parser.antlr.CMAnnotLexer;
import org.crossmobile.plugin.parser.antlr.CMAnnotParser;
import org.crossmobile.plugin.reg.Registry;

import java.io.StringReader;
import java.util.regex.Pattern;

public class AnnotationParser {

    private static final Pattern pattern = Pattern.compile("\\s+");

    AnnotationParser() {
    }

    public static NSelector parseSelector(String signature, Registry reg) {
        return parse(signature, new SelectorListener(reg));
    }

    public static NProperty parseProperty(String signature, Registry reg) {
        return parse(signature, new PropertyListener(reg));
    }

    public static NSelector parseFunction(String signature, Registry reg) {
        return parse(signature, new FunctionListener(reg));
    }

    public static NSelector parseBlock(String signature, Registry reg) {
        return parse(signature, new BlockListener(reg));
    }

    @SuppressWarnings("UseSpecificCatch")
    static <T> T parse(String signature, BaseListener<T> listener) {
        if (!signature.trim().endsWith(";"))
            signature += ";";
        signature = pattern.matcher(signature).replaceAll(" ");
        listener.setOriginalCode(signature);
        try {
            CodePointCharStream input = CharStreams.fromString(signature);
            CMAnnotLexer lexer = new CMAnnotLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CMAnnotParser parser = new CMAnnotParser(tokens);
            ParseTree tree = listener.getTree(parser);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);
        } catch (Exception ex) {
            return null;
        }
        return listener.foundData();
    }

    public static String getText(ParserRuleContext ctx) {
        return ctx.start.getInputStream().getText(new Interval(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    }

}
