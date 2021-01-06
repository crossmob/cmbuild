/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.robovm.models.parameters;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;

public class IdRParam extends RParam {
    public IdRParam(NParam nParam, Class<?> parrameter, NType type) {
        super(nParam, parrameter, type);
    }


    @Override
    public String getNative() {
        return "NSObject";
    }
}
