/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.WaterPark;

import java.io.IOException;

import static org.crossmobile.plugin.bro.JavaTransformer.NSOBJ_OBJ;
import static org.crossmobile.plugin.bro.JavaTransformer.OBJC_RUNTIME;

public class ObjcObjectBuilder extends ObjectBuilder {
    protected ObjcObjectBuilder(NObject obj, ClassBuilderFactory cbf) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        super(obj, cbf);
        addObjcRuntimeBind(getCclass());
        addNSObjectCostructors(getCclass());
    }

    private void addObjcRuntimeBind(CtClass cclass) throws CannotCompileException {
        CtConstructor classInitializer = cclass.makeClassInitializer();
        classInitializer.setBody(OBJC_RUNTIME + ".bind(" + cclass.getName() + ".class);");
    }

    /**
     * Adds skipinit and Handle constructors
     *
     * @param cclass
     * @throws CannotCompileException
     */
    private void addNSObjectCostructors(CtClass cclass) throws CannotCompileException {
        // SkipInit Cnstructor
        CtConstructor skipInitC = new CtConstructor(cbf.skipInitP, cclass);
        skipInitC.setBody("super((" + NSOBJ_OBJ + "$SkipInit)$1);");
        cclass.addConstructor(skipInitC);

        // Handle constructors
        CtConstructor handleC = new CtConstructor(cbf.handleP, cclass);
        handleC.setBody("super($$);");
        cclass.addConstructor(handleC);

    }
}
