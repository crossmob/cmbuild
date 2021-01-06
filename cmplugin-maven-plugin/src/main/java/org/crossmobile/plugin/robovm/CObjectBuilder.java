/*
 * (c) 2021 by Panayotis Katsaloulis
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

import static org.crossmobile.plugin.bro.JavaTransformer.RT_BRO;

public class CObjectBuilder extends ObjectBuilder {
    protected CObjectBuilder(NObject obj, ClassBuilderFactory cbf) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        super(obj, cbf);
        addBroBind(getCclass());
    }

    private void addBroBind(CtClass cclass) throws CannotCompileException {
        CtConstructor classInitializer = cclass.makeClassInitializer();
        classInitializer.setBody(RT_BRO + ".bind(" + cclass.getName() + ".class);");
    }
}
