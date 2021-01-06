/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.bridge.ann.*;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.crossmobile.plugin.actions.FormerTests.*;
import static org.crossmobile.plugin.utils.Texters.annName;
import static org.crossmobile.utils.NamingUtils.execSignature;
import static org.crossmobile.utils.NamingUtils.getClassNameFull;
import static org.crossmobile.utils.ReflectionUtils.appearsFirstIn;
import static org.crossmobile.utils.ReflectionUtils.getAnnotation;

public final class Parser {

    private static final String ANN_CLASS_NAMES = annName(CMClass.class) + ", " + annName(CMEnum.class) + ", " + annName(CMStruct.class) + ", " + annName(CMBundle.class) + ", " + annName(CMTarget.class) + ", " + annName(CMReference.class);
    private static final String ANN_METH_NAMES = annName(CMSelector.class) + ", " + annName(CMGetter.class) + ", " + annName(CMSetter.class) + ", " + annName(CMBlock.class) + ", " + annName(CMFunction.class);
    private static Class<?> last;

    private final Registry reg;

    public Parser(Registry reg) {
        this.reg = reg;
    }

    public void parse(Class<?> c) {
        if (!Modifier.isPublic(c.getModifiers()))
            return;
        if (reg.objects().retrieve(c) != null)
            return;

        last = c;
        CMClass annClass = c.getAnnotation(CMClass.class);
        CMEnum annEnum = c.getAnnotation(CMEnum.class);
        CMStruct annStruct = c.getAnnotation(CMStruct.class);
        CMBundle annBundle = c.getAnnotation(CMBundle.class);
        CMTarget annTarget = c.getAnnotation(CMTarget.class);
        CMReference annReference = c.getAnnotation(CMReference.class);

        int countObjectTypes = 0;
        if (annClass != null)
            countObjectTypes++;
        if (annEnum != null)
            countObjectTypes++;
        if (annStruct != null)
            countObjectTypes++;
        if (annBundle != null)
            countObjectTypes++;
        if (annTarget != null)
            countObjectTypes++;
        if (annReference != null)
            countObjectTypes++;
        if (countObjectTypes > 1) {
            Log.error("Class " + getClassNameFull(c) + " has more than one annotation of type " + ANN_CLASS_NAMES);
            return;
        }
        if (countObjectTypes < 1) {
            Log.error("Class " + getClassNameFull(c) + " should define one annotation of type " + ANN_CLASS_NAMES);
            return;
        }

        if (annClass != null && testClass(c, annClass))
            formClass(c);
        else if (annEnum != null && testEnum(c, annEnum))
            formEnum(c);
        else if (annStruct != null && testStruct(c, annStruct))
            formStruct(c);
        else if (annBundle != null && testBundle(c, annBundle))
            formBundle(c);
        else if (annTarget != null && testTarget(c, annTarget))
            formTarget(c);
        else if (annReference != null && testReference(c, annReference))
            formReference(c);
    }

    private void formClass(Class<?> c) {
        form(c.isInterface() ? new NObject("protocol", c) : new NObject("class", c));
    }

    private void formEnum(Class<?> c) {
        // no need to parse
    }

    private void formStruct(Class<?> c) {
        form(new NObject("struct", c));
    }

    private void formBundle(Class<?> c) {
        form(new NObject("bundle", c));
    }

    private void formTarget(Class<?> c) {
        form(new NObject("target", c));
    }

    private void formReference(Class<?> c) {
        form(new NObject("reference object", c));
    }

    private void form(NObject nobj) {
        Class<?> c = nobj.getType();
        boolean isCBased = nobj.isCBased();
        boolean isStruct = nobj.isStruct();
        NativeCode code = c.getAnnotation(NativeCode.class);
        if (code != null)
            nobj.setCode(code.value());
        ElementParser parser = new ElementParser(reg);

        if (!c.isInterface())
            if (isCBased) {
                if (nobj.isStruct()) {
                    if (!c.getSuperclass().equals(Object.class))
                        Log.error("Struct " + getClassNameFull(c) + " should directly inherit " + getClassNameFull(Object.class));
                } else if (reg.objects().getNSObject().isAssignableFrom(c))
                    Log.error("Class " + getClassNameFull(c) + " should not inherit NSObject");
            } else if (!reg.objects().getNSObject().isAssignableFrom(c))
                Log.error("Class " + getClassNameFull(c) + " should inherit NSObject");

        for (Constructor<?> cs : c.getConstructors()) {
            CMConstructor constr = cs.getAnnotation(CMConstructor.class);
            if (Modifier.isPublic(cs.getModifiers())) {
                Constructor<?> annConstr = cs;
                while (annConstr != null && (constr == null || constr.value().isEmpty())) {
                    annConstr = ReflectionUtils.getParentConstructor(annConstr);
                    constr = annConstr == null ? null : annConstr.getAnnotation(CMConstructor.class);
                }
                if (constr == null || constr.value().isEmpty())
                    Log.error("Constructor " + execSignature(cs) + " is not bound to native code on any parent class");
                else
                    parser.parseConstructor(nobj, cs, constr);
            }
        }

        for (Method m : c.getMethods()) {
            if (!m.isBridge() && appearsFirstIn(m, nobj.getType())) {
                CMSelector selector = getAnnotation(m, CMSelector.class);
                CMGetter getter = getAnnotation(m, CMGetter.class);
                CMSetter setter = getAnnotation(m, CMSetter.class);
                CMFunction func = getAnnotation(m, CMFunction.class);
                CMBlock block = getAnnotation(m, CMBlock.class);
                int count = (selector == null ? 0 : 1) + (getter == null ? 0 : 1) + (setter == null ? 0 : 1) + (func == null ? 0 : 1) + (block == null ? 0 : 1);
                if (count > 1)
                    Log.error("Only one of " + ANN_METH_NAMES + " is allowed for method " + execSignature(m));
                else if (isCBased && !isStruct && func == null)
                    Log.error("Unable to locate " + annName(CMFunction.class) + " for method " + execSignature(m));
                else if (isCBased && !isStruct && count > 1)
                    Log.error("Only annotation " + annName(CMFunction.class) + " is allowed for method " + execSignature(m));
                else if (!isCBased && count == 0)
                    Log.error("Unable to locate " + ANN_METH_NAMES + " for method " + execSignature(m));
                else if (selector != null)
                    parser.parseMethod(nobj, m, selector.staticMapping(), selector.value(), selector.sizeResolver(), selector.sinceIos());
                else if (getter != null)
                    parser.parseGetter(nobj, m, getter.value(), getter.sizeResolver(), getter.sinceIos());
                else if (setter != null)
                    parser.parseSetter(nobj, m, setter.value(), setter.sinceIos());
                else if (func != null)
                    parser.parseFunc(nobj, m, func.value(), func.sizeResolver(), reg);
                else if (block != null)
                    parser.parseBlock(nobj, m, block.value());
                else
                    Log.error("There is something wrong with method " + execSignature(m));
            }
        }
        testSelectorsConsistency(nobj);
        reg.objects().register(nobj);
    }

}
