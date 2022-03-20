/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import org.crossmobile.plugin.reg.Registry;

import java.io.IOException;

import static org.crossmobile.plugin.bro.JavaTransformer.NSOBJ_OBJ;
import static org.crossmobile.plugin.bro.JavaTransformer.OBJC_RUNTIME;

public class NSObjectBuilder extends ObjectBuilder {
    NSObjectBuilder(ClassBuilderFactory cbf) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        super(cbf.registry().objects().retrieve(cbf.registry().objects().getNSObject()), cbf);
        //these should be re-enabled when we migrate to our own NSObject
//        {
//            // Sanitize root objects
//            skipInitP = new CtClass[]{wp.classPool().makeClass("crossmobile.ios.foundation.NSObject$SkipInit")};
//            handleP = new CtClass[]{wp.classPool().makeClass("crossmobile.ios.foundation.NSObject$Handle"), CtClass.longType};
//
//            CtConstructor skipInitC = new CtConstructor(skipInitP, getCclass());
//            skipInitC.setBody(null);
//            getCclass().addConstructor(skipInitC);
//
//            CtConstructor handleC = new CtConstructor(handleP, getCclass());
//            handleC.setBody("super($2);");
//            getCclass().addConstructor(handleC);
//
//        }
        addNSObjectCostructors(getCclass());
        addObjcRuntimeBind();
    }

    private void addObjcRuntimeBind() throws CannotCompileException {
        CtConstructor classInitializer = getCclass().makeClassInitializer();
        classInitializer.setBody(OBJC_RUNTIME + ".bind(" + getCclass().getName() + ".class);");
    }

    @Override
    void createClass() {
        setCclass(cbf.waterpark().classPool().makeClass(target.getName(), cbf.waterpark().get(NSOBJ_OBJ)));
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
