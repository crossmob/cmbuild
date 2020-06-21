/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.bridge.ann.CMLibParam.ParamContext;

import static org.crossmobile.bridge.ann.CMLibParam.ParamContext.Regular;
import static org.crossmobile.utils.TextUtils.replaceOld;
import static org.crossmobile.utils.TextUtils.replaceOldString;

public class PluginParam {

    private String description = "";
    private String paramMeta = "";
    private ParamContext paramContext = Regular;

    public void setDescription(String description, String info) {
        this.description = replaceOldString(this.description, description, info);
    }

    public String getDescription() {
        return description;
    }

    public void setMeta(String paramMeta, String info) {
        this.paramMeta = replaceOldString(this.paramMeta, paramMeta, info);
    }

    public String getMeta() {
        return paramMeta;
    }

    public void setContext(ParamContext paramContext, String info) {
        this.paramContext = replaceOld(this.paramContext == Regular ? null : this.paramContext, paramContext, info);
    }

    public ParamContext getContext() {
        return paramContext;
    }
}
