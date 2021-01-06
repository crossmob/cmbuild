/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.bro;

import javassist.ClassPool;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.robovm.ClassBuilderFactory;
import org.crossmobile.plugin.utils.WaterPark;
import org.crossmobile.utils.CollectionUtils;
import org.crossmobile.utils.Log;

import java.io.File;
import java.util.Collection;
import java.util.function.Function;

/**
 * JavaTransformer takes crossmobile api classes and transforms them to robovm compatible
 */
public class JavaTransformer {
    public static final String PROPERTY_ANN = "org.robovm.objc.annotation.Property";
    public static final String STRUCT_ANN = "org.robovm.rt.bro.annotation.StructMember";
    public static final String BRIDGE_ANN = "org.robovm.rt.bro.annotation.Bridge";
    public static final String METHOD_ANN = "org.robovm.objc.annotation.Method";
    public static final String BLOCK_ANN = "org.robovm.objc.annotation.Block";
    public static final String OBJC_OBJ = "org.robovm.objc.ObjCObject";
    public static final String OBJC_CLS = "org.robovm.objc.ObjCClass";
    public static final String OBJC_PROT = "org.robovm.objc.ObjCProtocol";
    public static final String NSOBJ_OBJ = "org.robovm.apple.foundation.NSObject";
    public static final String NSOBJ_OBJ_PROT = "org.robovm.apple.foundation.NSObjectProtocol";
    public static final String CFT_OBJ = "org.robovm.apple.corefoundation.CFType";
    public static final String WEAK_ANN = "org.robovm.rt.annotation.WeaklyLinked";
    public static final String MSSINT_ANN = "org.robovm.rt.bro.annotation.MachineSizedSInt";
    public static final String MSUINT_ANN = "org.robovm.rt.bro.annotation.MachineSizedUInt";
    public static final String MSFLOAT_ANN = "org.robovm.rt.bro.annotation.MachineSizedFloat";
    public static final String BYVAL_ANN = "org.robovm.rt.bro.annotation.ByVal";
    public static final String NATIVECLASS_ANN = "org.robovm.objc.annotation.NativeClass";
    public static final String LIBRARY_ANN = "org.robovm.rt.bro.annotation.Library";
    public static final String POINTER_ANN = "org.robovm.rt.bro.annotation.Pointer";
    public static final String MARSHALERS_ANN = "org.robovm.rt.bro.annotation.Marshalers";
    public static final String MARSHALER_ANN = "org.robovm.rt.bro.annotation.Marshaler";
    public static final String GLOBALVALUE_ANN = "org.robovm.rt.bro.annotation.GlobalValue";
    public static final String OBJC_RUNTIME = "org.robovm.objc.ObjCRuntime";
    public static final String RT_BRO = "org.robovm.rt.bro.Bro";
    public static final String RT_PTR = "org.robovm.rt.bro.ptr.Ptr";
    public static final String STRUCT = "org.robovm.rt.bro.Struct";
    public static final String BYTE_PTR = "org.robovm.rt.bro.ptr.BytePtr";

    public static final String[] MARSHLERS = {
            "org.robovm.apple.foundation.NSArray$AsListMarshaler",
            "org.robovm.apple.foundation.NSDictionary$AsStringMapMarshaler",
            "org.robovm.apple.foundation.NSSet$AsSetMarshaler"
    };

    private static final Function<NType, String> nname = ntype -> ntype.getType().getName();
    private static final Function<Collection<NParam>, Collection<NType>> nparam = nparams -> CollectionUtils.asList(nparams, NParam::getNType);

    public static void exec(ClassPool cp, Registry reg, File filedest) {
        WaterPark wp = new WaterPark(cp, reg);
        try {
            ClassBuilderFactory cbf = new ClassBuilderFactory(wp, reg, filedest);
            for (NObject obj : reg.objects().retrieveAll())
                cbf.getClass(obj).finaliseClass();
            cbf.write();
        } catch (Exception e) {
            Log.error(e);
            BaseUtils.throwException(e);
        }
    }
}
