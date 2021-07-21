/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import crossmobile.rt.StrongReference;
import org.crossmobile.bridge.ann.CMRef;
import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.model.StaticMappingType;
import org.crossmobile.plugin.robovm.models.parameters.*;
import org.crossmobile.plugin.utils.WaterPark;
import org.crossmobile.utils.CustomTypeClasses;
import org.crossmobile.utils.Log;

import java.lang.reflect.Method;

public class ParamBuilder {

    public static RParam createParam(NParam nParam, NType type, Class<?> parameter, NSelector selector, WaterPark wp) {
        if (nParam != null && !nParam.getStaticMapping().equals(StaticMappingType.NATIVE) && nParam.getJavaType() != null && nParam.getJavaDeclaredAnnotation(CMRef.class) != null)
            return new StructRParam(nParam, parameter, type, nParam.getJavaDeclaredAnnotation(CMRef.class).value());
        else if (type.getNativeType().equals("id") && type.getType().equals(parameter))
            return new IdRParam(nParam, parameter, type);
        else if (type.getType().isArray() && type.getType().equals(parameter) && type.getType().getComponentType() != null && type.getType().getComponentType().isPrimitive())
            return new PrimArrayRParam(nParam, parameter, type);
        else if (type.getType().isArray() && type.getType().equals(parameter))
            return new ArrayRParam(nParam, parameter, type);
        else if (type.getType().isArray() && String[].class.equals(parameter))
            return new StringArrayRParam(nParam, parameter, type);
        else if (Method.class.equals(parameter)) {
            //ignore for now
            Log.error("No RParam available for Ntype : " + type.getType() + " , with parameter : " + parameter + " and native type : " + type.getNativeType() + ", for selector : " + selector.getObjCSignature() + ", in class : " + selector.getJavaExecutable().getDeclaringClass().getName());
        } else if (type.getType().equals(parameter))
            return new NativeRParam(nParam, parameter, type);
        else if (type.isPrimitive() && objectified(type.getType()).equals(parameter) && wp.contains(parameter.getName()))
            return new NativeRParam(nParam, parameter, type);
        else if (type.getType().equals(CustomTypeClasses.Instance.class) && (parameter == null || parameter.equals(selector.getJavaExecutable().getDeclaringClass())))
            return new InstanceRParam(nParam, selector.getJavaExecutable().getDeclaringClass(), type);
        else if (parameter == null && type.getType().equals(selector.getJavaExecutable().getDeclaringClass()))
            return new StaticRefRParam(nParam, parameter, type);
        else if (parameter != null && parameter.equals(StrongReference.class))
            return new StrongRParam(nParam, parameter, type, wp);
            // This "parameter.getCanonicalName().equals("crossmobile.ios.foundation.NSObject")" is not a misstype
            // calling NSObject.class throws exception because it cannot find NativeObject (which is not in classpool anyway)
        else if ((parameter == null || parameter.getCanonicalName().equals("crossmobile.ios.foundation.NSObject")) && type.getNativeType().equals("id"))
            return new IdRParam(nParam, selector.getJavaExecutable().getDeclaringClass(), type);
        else if (type.getType().equals(char[].class) && parameter.equals(String.class))
            return new StringRParam(nParam, parameter, type);
        else if (type.getType().equals(int.class) && (parameter.isArray() || parameter.equals(String.class)))
            return new LengthRParam(nParam, parameter, type);
        else if (type.getType().equals(CustomTypeClasses.VoidRef.class))
            return new PointerRParam(nParam, parameter, type);
        else if (type.getType().equals(String.class) && parameter.equals(Class.class))
            return new ClassRParam(nParam, parameter, type);
        else if (type.getNativeType().endsWith("Ref") && type.getType().isAssignableFrom(parameter))
            return new RefRParam(nParam, parameter, type);
        else
            Log.error("No RParam available for Ntype : " + type.getType() + " , with parameter : " + parameter + " and native type : " + type.getNativeType() + ", for selector : " + selector.getObjCSignature() + ", in class : " + selector.getJavaExecutable().getDeclaringClass().getName());
        return null;
    }

    private static Object objectified(Class<?> type) {
        if (type.equals(int.class))
            return Integer.class;
        if (type.equals(long.class))
            return Long.class;
        if (type.equals(float.class))
            return Float.class;
        if (type.equals(double.class))
            return Double.class;
        if (type.equals(byte.class))
            return Byte.class;
        if (type.equals(boolean.class))
            return Boolean.class;
        return type;
    }

}
