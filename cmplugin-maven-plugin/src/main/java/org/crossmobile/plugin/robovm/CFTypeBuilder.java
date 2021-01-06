/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.WaterPark;

import java.io.IOException;

import static org.crossmobile.plugin.bro.JavaTransformer.CFT_OBJ;

public class CFTypeBuilder extends CObjectBuilder {
    CFTypeBuilder(ClassBuilderFactory cbf) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        super(cbf.registry().objects().retrieve(cbf.registry().objects().getCFType()), cbf);
        //these should be reenabled when we migrate to our own NSOBject
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
    }

    @Override
    void createClass() {
        setCclass(cbf.waterpark().classPool().makeClass(target.getName(), cbf.waterpark().get(CFT_OBJ)));
    }


}
