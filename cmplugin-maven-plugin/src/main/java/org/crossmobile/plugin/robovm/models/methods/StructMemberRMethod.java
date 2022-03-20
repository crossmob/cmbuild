/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm.models.methods;

import javassist.CtClass;
import javassist.Modifier;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.robovm.models.parameters.RParam;
import org.crossmobile.plugin.utils.WaterPark;

import java.util.List;

import static org.crossmobile.plugin.bro.JavaTransformer.STRUCT_ANN;
import static org.crossmobile.utils.JavassistAnnParam.toParam;
import static org.crossmobile.utils.JavassistUtils.addAnnotation;

public class StructMemberRMethod extends FunctionRMethod {
    public StructMemberRMethod(NSelector selector, RParam returnParam, List<RParam> parameters, NObject object, CtClass cclass, WaterPark wp) {
        super(selector, returnParam, parameters, object, cclass, wp);
    }

    @Override
    protected void setModifiers() {
        method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT | Modifier.NATIVE);
    }

    @Override
    protected void addMethodAnnotation() {
        addAnnotation(method, STRUCT_ANN, toParam("value", getSelector().getStructRef().index));
    }
}
