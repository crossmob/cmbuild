/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm.models.parameters;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.reg.TypeRegistry;
import org.crossmobile.plugin.robovm.ClassBuilder;
import org.crossmobile.plugin.utils.WaterPark;
import org.crossmobile.utils.JavassistAnnParam;

import static org.crossmobile.plugin.bro.JavaTransformer.BLOCK_ANN;
import static org.crossmobile.plugin.bro.JavaTransformer.BYVAL_ANN;
import static org.crossmobile.plugin.bro.JavaTransformer.MARSHALER_ANN;
import static org.crossmobile.plugin.bro.JavaTransformer.MSFLOAT_ANN;
import static org.crossmobile.plugin.bro.JavaTransformer.MSSINT_ANN;
import static org.crossmobile.plugin.bro.JavaTransformer.MSUINT_ANN;

public abstract class RParam {
    private final Class<?> param;
    private final NType type;
    private final NParam nParam;


    public RParam(NParam nParam, Class<?> param, NType type) {
        this.type = type;
        this.nParam = nParam;
        this.param = param;
    }

    public boolean needsConvert() {
        return !type.getType().equals(param);
    }

    public Class<?> getJava() {
        return param;
    }

    public String getJavaType() {
        return getJava().getCanonicalName();
    }

    public String getNative() {
        return type.getType().getCanonicalName();
    }

    public String getNativeType() {
        return type.getNativeType();
    }

    public String convRef() {
        return reference();
    }

    public String conversion() {
        return "";
    }

    public String reference() {
        return nParam.getVarname();
    }

    public NType getType() {
        return type;
    }

    public String annotation() {
        return getTypeAnnotation(type);
    }

    public boolean isVoid() {
        return param != null && type.getType().equals(void.class) && param.equals(void.class);
    }

    public NParam getnParam() {
        return nParam;
    }

    private String getTypeAnnotation(NType type) {
        if (type.getNativeType().equals("NSInteger") && type.getType().equals(long.class))
            return MSSINT_ANN;
        if (type.getNativeType().equals("NSUInteger") && type.getType().equals(long.class))
            return MSUINT_ANN;
        if (type.getNativeType().equals("CGFloat"))
            return MSFLOAT_ANN;
        if (TypeRegistry.isStruct(type.getType()))
            return BYVAL_ANN;
        if (TypeRegistry.isBlock(type.getType()))
            return BLOCK_ANN;
        if (type.getNativeType().contains("NSArray"))
            return MARSHALER_ANN;
        if (type.getNativeType().contains("NSDictionary"))
            return MARSHALER_ANN;
        if (type.getNativeType().contains("NSSet"))
            return MARSHALER_ANN;
        return "";
    }

    public JavassistAnnParam[] annotationParams(WaterPark wp) {
        if (getType().getNativeType().contains("NSArray")) {
            return new JavassistAnnParam[]{
                    JavassistAnnParam.toParam("value", wp.toClass("org.robovm.apple.foundation.NSArray$AsListMarshaler"))
            };

        }
        if (getType().getNativeType().contains("NSDictionary")) {
            return new JavassistAnnParam[]{
                    JavassistAnnParam.toParam("value", wp.toClass("org.robovm.apple.foundation.NSDictionary$AsStringMapMarshaler"))
            };
        }
        if (getType().getNativeType().contains("NSSet")) {
            return new JavassistAnnParam[]{
                    JavassistAnnParam.toParam("value", wp.toClass("org.robovm.apple.foundation.NSSet$AsSetMarshaler"))
            };
        }
        return new JavassistAnnParam[]{};
    }

    public String getNativeReturn() {
        return getNative();
    }
}
