/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm.models.methods;

import javassist.CannotCompileException;
import javassist.CtClass;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.robovm.models.parameters.RParam;
import org.crossmobile.plugin.utils.WaterPark;

import java.lang.reflect.Modifier;
import java.util.List;

public class InterfaceRMethod extends RMethod {
    public InterfaceRMethod(NSelector selector, RParam returnParam, List<RParam> parameters, NObject object, CtClass cclass, WaterPark wp) {
        super(selector, returnParam, parameters, object, cclass, wp);
    }

    @Override
    protected void setModifiers() throws CannotCompileException {
        method.setModifiers(Modifier.PUBLIC);
        method.setBody("{" + (needsReturn() ? "return null;" : "") + "}");
    }
}
