/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.bridge.ann.AssociationType;
import org.crossmobile.plugin.model.*;
import org.crossmobile.plugin.objc.param.Emitter;
import org.crossmobile.plugin.objc.param.ParamEmitter;
import org.crossmobile.plugin.objc.param.ResultEmitter;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;

import static org.crossmobile.plugin.utils.Texters.*;
import static org.crossmobile.utils.CollectionUtils.forEach;
import static org.crossmobile.utils.NamingUtils.getClassNameSimple;
import static org.crossmobile.utils.NamingUtils.toObjC;

public class SelectorEmitter {

    protected final NSelector selector;

    private static final String FUNC_REF = "func$ref";
    private final String selfName;
    private final Registry reg;

    public SelectorEmitter(NSelector selector, Registry reg) {
        this(selector, reg, null);
    }

    public SelectorEmitter(NSelector selector, Registry reg, String selfName) {
        this.selector = selector;
        this.selfName = selfName == null ? "self" : selfName;
        this.reg = reg;
    }

    public final void emitSignature(Streamer out) throws IOException {
        emitDefinition(out);
        out.append(";\n");
    }

    public final SelectorEmitter emitImplementation(Streamer out) throws IOException {
        emitDefinition(out);
        emitOpenBracket(out);
        if (selector.requireIosVersion())
            out.append("if (isIosAtLeast(").append(selector.getSinceIosMajor())
                    .append(",").append(selector.getSinceIosMinor()).append(")){\n").tab();

        if (selector.getStructRef() != null)
            emitSelectorAsStructMember(out);
        else {
            emitBreakEarly(out);
            emitAssociate(out);
            ParamEmitter p = getParams();
            ResultEmitter result = p.getResultEmitter();
            for (Emitter e : p.getNativeParameters()) {
                out.append(e.init());
                result.appendDestroy(e.destroy());
            }
            String selValue = "";
            switch (selector.getMethodType()) {
                case SELECTOR:
                case BLOCK:
                case VA_SELECTOR:
//                selValue = shouldEmitSelectorAsMethodImp() ? emitSelectorAsIMP(p) : emitSelectorAsObjC(p);
                    selValue = emitSelectorAsObjC(p);
                    break;
                case FUNCTION:
                case VA_FUNCTION:
                    selValue = emitFunction(p);
                    break;
                case EXTERNAL:
                    selValue = selector.getName();
                    break;
            }
            result.emit(out, selValue);
//        emitMethodImpDefinition(out);
        }
        if (selector.requireIosVersion()) {
            if (selector.getReturnType().getType() != void.class) {
                out.untab().append("} else {\n").tab();
                out.append("return ").append(selector.getReturnType().getDefaultValue()).append(";\n");
            }
            out.untab().append("}\n");
        }

        emitCloseBracket(out);
        return this;
    }


    private void emitMethodImpDefinition(Streamer out) throws IOException {
        if (shouldEmitSelectorAsMethodImp()) {
            out.append("static IMP ").append(FUNC_REF).append(" = nil;\n");
            out.append("if (!").append(FUNC_REF).append(")\n").tab();
            out.append(FUNC_REF).append(" = class_getMethodImplementation(");
            out.append(selector.isStatic() ? getMetaClassNameReference(selector.getContainer()) : getClassNameReference(selector.getContainer()));
            out.append(", @selector(").append(selector.getObjCSignature()).append("));\n").untab();
        }
    }

    private String emitSelectorAsIMP(ParamEmitter emitter) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append("((").append(selector.getReturnType().getSafeNativeType()).append("(*)(").append("id,SEL");
        forEach(emitter.getNativeParameters()).
                setFilter(e -> !e.isParameterHidden()).onAny(e -> {
            out.append(",").append(e.getSafeNativeType());
        }).go();
        out.append("))").append(FUNC_REF);
        out.append(")(").append(emitter.isStatic() ? getClassNameReference(selector.getContainer()) : "self");
        out.append(", @selector(").append(selector.getObjCSignature()).append(")");

        forEach(emitter.getNativeParameters()).setFilter(e -> !e.isParameterHidden()).
                onAny(e -> out.append(",").append(e.embed())).go();
        out.append(")");
        return out.toString();
    }

    private void emitSelectorAsStructMember(Streamer out) throws IOException {
        ParamEmitter p = getParams();
        NStructField structRef = selector.getStructRef();
        if (selector.isGetter()) {
            if (structRef.type.isPrimitive())
                out.append("return " + p.getInstanceName() + "->" + structRef.objc_name + ";\n");
            else
                out.append("return [" + p.getInstanceName() + "->" + structRef.objc_name + " retain];\n");
        } else {
            String givenVar = p.getNativeParameters().iterator().next().givenVar();
            if (structRef.type.isPrimitive())
                out.append(p.getInstanceName() + "->" + structRef.objc_name + " = " + givenVar + ";\n");
            else {
                String childName = getClassNameSimple(structRef.type);
                out.append("[" + p.getInstanceName() + "->" + structRef.objc_name + " set" + childName + ":[" + givenVar + " get" + childName + "]];\n");
            }
        }
    }

    private String emitSelectorAsObjC(ParamEmitter emitter) {
        StringBuilder out = new StringBuilder();
        out.append("[").append(targetName(emitter)).append(" ").append(emitter.getName());
        forEach(emitter.getNativeParameters()).
                setFilter(e -> !e.isParameterHidden()).onTail(e -> out.append(" ")).onAny(e -> {
            out.append(e.paramName());
            out.append(e.objCParamSeparator());
            out.append(e.embed());
        }).go();
        out.append("]");
        return out.toString();
    }

    protected String emitFunction(ParamEmitter emitter) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append(emitter.getName()).append("(");
        forEach(emitter.getNativeParameters()).
                setFilter(e -> !e.isParameterHidden()).onTail(e -> out.append(", ")).onAny(e -> out.append(e.embed())).go();
        out.append(")");
        return out.toString();
    }

    protected void emitBreakEarly(Streamer out) throws IOException {
        for (NParam p : selector.getParams())
            if (p.shouldNotBeNull())
                out.append("if (").append(p.getVarname()).append(" == ").
                        append(p.getNType().getType().isPrimitive() ? "0" : "JAVA_NULL").append(")\n").tab().
                        append("return;\n").untab();
    }

    protected void emitAssociate(Streamer out) throws IOException {
        for (NParam p : selector.getParams())
            if (p.getAssociation().needsAccosiation()) {
                String var = p.getVarname();
                out.append("objc_setAssociatedObject(self, ");
                if (p.getAssociation() == AssociationType.ASSOCIATE)
                    out.append("@selector(").append(selectorSignature(selector)).append(")");
                else
                    out.append("(void*)").append(var);
                out.append(", ");
                if (p.getAssociation() == AssociationType.REMOVESELF)
                    out.append("nil");
                else
                    out.append(var);
                out.append(", OBJC_ASSOCIATION_RETAIN_NONATOMIC);\n");
            }
    }

    protected void emitDefinition(Streamer out) throws IOException {
        Executable ex = selector.getJavaExecutable();
        out.append(Modifier.isStatic(ex.getModifiers()) ? "+" : "-").append(" (");
        out.append(selector.getReturnType().getCanonicalNativeType());
        out.append(") ").append(methodObjCName(ex));
        for (NParam p : selector.getParams()) {
            Class<?> jParam = p.getJavaType();
            /* Ignore this parameter if:
                a) No java parameter exists, usually because this parameter is deducted in Java, e.g. array size parameters
                b) There's some static mapping on the native size
             */
            if (jParam != null && p.getStaticMapping() != StaticMappingType.NATIVE)
                out.append(":(").append(toObjCType(jParam)).append(")").append(" ").append(p.getVarname()).append(" ");
        }
    }

    protected void emitOpenBracket(Streamer out) throws IOException {
        out.append("\n{\n").tab();
    }

    protected void emitCloseBracket(Streamer out) throws IOException {
        out.untab().append("}\n\n");
    }

    private String targetName(ParamEmitter emitter) {
        if (selector.isFakeConstructor())
            return "[" + getParentName() + " alloc]";
        if (!emitter.isStatic())
            return emitter.getInstanceName();
        else
            return getParentName();
    }

    private String getParentName() {
        NObject parent = selector.getContainer();
        return parent.isCBased()
                ? toObjC(parent.getType())
                : toObjC(getClassNameSimple(parent.getType()));
    }

    protected ParamEmitter getParams() {
        return ParamEmitter.forward(selector, reg, selfName);
    }

    protected String getOriginalCode() {
        return selector.getOriginalCode();
    }

    private boolean shouldEmitSelectorAsMethodImp() {
        return false;
//        return parent != null && parent.obj.isObjCBased() && !selector.isConstructor()
//                && (selector.getMethodType() == MethodType.SELECTOR || selector.getMethodType() == MethodType.BLOCK);
    }

//    public void emitSwift(Streamer out) throws IOException {
//        if (selector.getSwiftMethod().isEmpty())
//            return;
//        out.append("\t@objc\n");
//        out.append("\tstatic func ").append(getClassNameSimple(selector.getContainer().getType()));
//        out.append("_").append(selector.getName()).append("(");
//        forEach(selector.getParams()).
//                onAny(e -> {
//                    try {
//                        out.append("_ ").append(e.getVarname()).append(":");
//                        out.append(getSwiftType(e.getNType()));
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                    }
//                }).
//                onFront(e -> out.append(", ")).go();
//        out.append(") -> ").append(getSwiftType(selector.getReturnType())).append(" {\n\t\t");
//        if (!selector.getReturnType().getType().equals(void.class))
//            out.append("return ");
//        out.append(selector.getSwiftMethod().replace("va_array", "convArgs(va_array"));
//        out.append("\n\t}\n\n");
//    }
//
//    private String getSwiftType(NType ntype) {
//        if (ntype.getVarargType() != null && ntype.getVarargType().virtual)
//            return "[Any]";
//        String type = ntype.getNativeType().trim();
//        if (type.equals("void"))
//            return "Void";
//        if (type.equals("id"))
//            return "Any";
//        if (type.equals("instancetype")) {
//            type = getClassNameSimple(selector.getContainer().getType());
//            if (type.equals("String"))  // Maybe other types as well?
//                return "NSString";
//        }
//        if (type.endsWith("*"))
//            type = type.substring(0, type.length() - 2).trim();
//        return type;
//    }
}
