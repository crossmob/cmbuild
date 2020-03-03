// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.plugin.robovm.models.parameters;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NType;

public class LengthRParam extends RParam {
    public LengthRParam(NParam nParam, Class<?> parrameter, NType type) {
        super(nParam, parrameter, type);
    }

    @Override
    public String convRef() {
        return getnParam().getAffiliation().getParameter().getVarname() + ".length" + (getJava().equals(String.class) ? "()" : "");
    }

    @Override
    public String reference() {
        return null;
    }
}
