/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.WaterPark;

import java.io.IOException;

import static org.crossmobile.plugin.bro.JavaTransformer.STRUCT;

public class StructBuilder extends CObjectBuilder {
    protected StructBuilder(NObject obj, ClassBuilderFactory cbf) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        super(obj, cbf);
        getCclass().setSuperclass(cbf.waterpark().get(STRUCT));
    }
}
