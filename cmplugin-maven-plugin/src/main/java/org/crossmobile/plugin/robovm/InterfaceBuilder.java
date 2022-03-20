/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.WaterPark;

import static org.crossmobile.plugin.bro.JavaTransformer.NSOBJ_OBJ_PROT;

public class InterfaceBuilder extends ClassBuilder {
    InterfaceBuilder(NObject obj, ClassBuilderFactory cbf) {
        super(obj,cbf);
        setCclass(cbf.waterpark().classPool().makeInterface(target.getName(), cbf.waterpark().get(NSOBJ_OBJ_PROT)));
    }
}
