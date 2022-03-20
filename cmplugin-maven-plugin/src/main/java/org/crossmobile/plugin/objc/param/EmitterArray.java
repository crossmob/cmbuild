/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc.param;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;

import static org.crossmobile.utils.TextUtils.TAB;

class EmitterArray extends Emitter {

    private final boolean isStringMultyChar;
    private final String arrayType;
    private final String sizeResolver;

    EmitterArray(NParam param, boolean forward) {
        this(param.getName(), param.getVarname(), param.getNType(), param.getJavaType(), forward);
    }

    EmitterArray(String paramName, String varName, NType type, Class javaType, boolean forward) {
        this(paramName, varName, type, isStringMultiChar(javaType, type), findArrayType(javaType), type.getSizeResolver(), forward);
    }

    private EmitterArray(String paramName, String varName, NType type, boolean isStringMultyChar, String arrayType, String sizeResolver, boolean forward) {
        super(paramName, varName, type, isStringMultyChar, forward);
        this.isStringMultyChar = isStringMultyChar;
        this.arrayType = arrayType;
        this.sizeResolver = sizeResolver;
    }

    @Override
    protected String initForward() {
        if (isStringMultyChar) {
            String i = paramVar() + "$i";
            String string = paramVar() + "$str";
            return "char** " + paramVar() + " = NULL;\n"
                    + "if (" + givenVar() + " != JAVA_NULL && " + givenVar() + "->length > 0) {\n"
                    + TAB + paramVar() + " = malloc(" + givenVar() + "->length * (sizeof(char*)));\n"
                    + TAB + "NSString * " + string + ";\n"
                    + TAB + "for (int " + i + " = 0; " + i + " < " + givenVar() + "->length; " + i + "++) {\n"
                    + TAB + TAB + string + " = " + givenVar() + "->array.o[" + i + "];\n"
                    + TAB + TAB + "if (" + string + " == nil || " + string + " == JAVA_NULL || " + string + " == NULL)\n"
                    + TAB + TAB + TAB + paramVar() + "[" + i + "] = 0;\n"
                    + TAB + TAB + "else\n"
                    + TAB + TAB + TAB + paramVar() + "[" + i + "] = (char*)[argv$conv$str UTF8String];\n"
                    + TAB + "}\n"
                    + "}\n";
        } else
            return "";
    }

    @Override
    protected String embedForward() {
        return isStringMultyChar ? super.embedForward() : "(" + givenVar() + " == JAVA_NULL ? NULL : " + givenVar() + "->array.data)";
    }

    @Override
    protected String destroyForward() {
        if (isStringMultyChar)
            return "free(" + paramVar() + ");\n";
        else
            return "";
    }

    @Override
    protected String execForward(String exec, boolean needsDestroy) {
        exec = "[XMLVMArray createSingleDimensionWithType:" + arrayType + " size:" + sizeResolver + " andData:(void*)" + exec + "]";
        return super.execForward(exec, needsDestroy);
    }

    private static boolean isStringMultiChar(Class javaType, NType nativeType) {
        return javaType.equals(String[].class) && nativeType.getType().equals(char[][].class);
    }

    private static String findArrayType(Class array) {
        array = array.getComponentType();
        switch (array.getName()) {
            case "boolean":
                return "1/*boolean*/";
            case "byte":
                return "3/*byte*/";
            case "short":
                return "4/*short*/";
            case "int":
                return "5/*int*/";
            case "long":
                return "8/*long*/";
            case "float":
                return "6/*float*/";
            case "double":
                return "7/*double*/";
            case "char":
                return "2/*char*/";
            default:
                return "2/*object*/";
        }
    }
}
