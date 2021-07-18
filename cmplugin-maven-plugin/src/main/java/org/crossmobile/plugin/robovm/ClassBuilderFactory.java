/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.*;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.ClassWriter;
import org.crossmobile.plugin.utils.WaterPark;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.crossmobile.plugin.bro.JavaTransformer.*;

public class ClassBuilderFactory {
    private final Map<NObject, ClassBuilder> objects = new HashMap<>();
    private final Registry reg;
    private final File fileDest;
    private final WaterPark wp;

    CtClass[] skipInitP;
    CtClass[] handleP;

    public ClassBuilderFactory(WaterPark wp, Registry reg, File fileDest) throws CannotCompileException, NotFoundException, IOException, ClassNotFoundException {
        this.reg = reg;
        this.fileDest = fileDest;
        this.wp = wp;
        initializeObject();
        NObject nsobject = reg.objects().retrieve(reg.objects().getNSObject());
        NObject cftype = reg.objects().retrieve(reg.objects().getCFType());
        objects.put(cftype, new CFTypeBuilder(this));
        objects.put(nsobject, new NSObjectBuilder(this));
    }

    private void initializeObject() throws CannotCompileException, NotFoundException, IOException {
        CtClass strct = wp.classPool().makeClass(STRUCT);
        strct.addConstructor(CtNewConstructor.defaultConstructor(strct));
        strct.addMethod(CtNewMethod.make("public " + STRUCT + " next(long delta) {return null;}", strct));
        strct.addMethod(CtNewMethod.make("public static " + STRUCT + " allocate(java.lang.Class cls, int n) {return null;}", strct));

        CtClass ptr = wp.classPool().makeClass(RT_PTR, strct);
        ptr.addConstructor(CtNewConstructor.defaultConstructor(ptr));
        ptr.addMethod(CtNewMethod.make("public " + RT_PTR + " set(java.lang.Object o){return this;}", ptr));

        CtClass btptr = wp.classPool().makeClass(BYTE_PTR, strct);
        addPointer(btptr, null);

        wp.contains("java.lang.String");
        btptr.addMethod(CtNewMethod.make("public static " + BYTE_PTR + " toBytePtrAsciiZ(java.lang.String s) {return null;}", btptr));

        CtClass obj = wp.classPool().makeClass(OBJC_OBJ);
        obj.addConstructor(CtNewConstructor.defaultConstructor(obj));
        obj.addConstructor(new CtConstructor(new CtClass[]{CtClass.longType}, obj));
        obj.addMethod(CtMethod.make("protected void initObject(long handle) {}", obj));

        CtClass objcl = wp.classPool().makeClass(OBJC_CLS, obj);
        objcl.addMethod(CtNewMethod.make("public static " + OBJC_CLS + " getByType(java.lang.Class type) {return null;}", objcl));
        objcl.addMethod(CtNewMethod.make("public String getName() {return null;}", objcl));

        CtClass nsobj = wp.classPool().makeClass(NSOBJ_OBJ, obj);
        nsobj.addConstructor(CtNewConstructor.defaultConstructor(nsobj));
        nsobj.addConstructor(new CtConstructor(new CtClass[]{CtClass.longType}, nsobj));

        CtClass objProt = wp.classPool().makeInterface(OBJC_PROT);
        wp.classPool().makeInterface(NSOBJ_OBJ_PROT, objProt);
        {
            // Sanitize root objects
            skipInitP = new CtClass[]{wp.classPool().makeClass(NSOBJ_OBJ + "$SkipInit")};
            handleP = new CtClass[]{wp.classPool().makeClass(NSOBJ_OBJ + "$Handle"), CtClass.longType};

            CtConstructor skipInitC = new CtConstructor(skipInitP, nsobj);
            skipInitC.setBody(null);
            nsobj.addConstructor(skipInitC);

            CtConstructor handleC = new CtConstructor(handleP, nsobj);
            handleC.setBody("super($2);");
            nsobj.addConstructor(handleC);

        }

        for (String marshler : MARSHLERS) {
            CtClass mar = wp.classPool().makeClass(marshler);
            mar.addConstructor(CtNewConstructor.defaultConstructor(mar));
        }

        CtClass cft = wp.classPool().makeClass(CFT_OBJ);
        cft.addConstructor(CtNewConstructor.defaultConstructor(cft));

        CtClass objc_rt = wp.classPool().makeClass(OBJC_RUNTIME);
        objc_rt.addConstructor(CtNewConstructor.defaultConstructor(objc_rt));
        objc_rt.addMethod(CtNewMethod.make("public static void bind(Class c) {}", objc_rt));

        CtClass rt_bro = wp.classPool().makeClass(RT_BRO);
        rt_bro.addConstructor(CtNewConstructor.defaultConstructor(rt_bro));
        rt_bro.addMethod(CtNewMethod.make("public static void bind(Class c) {}", rt_bro));

    }

    void addPointer(CtClass cclass, String path) throws NotFoundException, CannotCompileException, IOException {
        CtClass nestedClass = cclass.makeNestedClass(cclass.getSimpleName() + "Ptr", true);
        nestedClass.setSuperclass(wp.classPool().getCtClass(RT_PTR));
        if (path != null)
            ClassWriter.saveClass(nestedClass, path);
    }

    public ClassBuilder getClass(NObject obj) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        if (!objects.containsKey(obj))
            objects.put(obj, getBuilder(obj));
        return objects.get(obj);
    }

    private ClassBuilder getBuilder(NObject obj) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        if (obj.getType().isInterface())
            return new InterfaceBuilder(obj, this);
        if (obj.isStruct())
            return new StructBuilder(obj, this);
        if (obj.isObjCBased())
            return new ObjcObjectBuilder(obj, this);
        if (obj.isCBased())
            return new CObjectBuilder(obj, this);
        return null;
    }

    public void write() throws CannotCompileException, IOException {
        for (ClassBuilder value : objects.values())
            ClassWriter.saveClass(value.getCclass(), fileDest.getAbsolutePath());
    }

    public WaterPark waterpark() {
        return wp;
    }

    public Registry registry() {
        return reg;
    }

    public File getFileDest() {
        return fileDest;
    }
}
