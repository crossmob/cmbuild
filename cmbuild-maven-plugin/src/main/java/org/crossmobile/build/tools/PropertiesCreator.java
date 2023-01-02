/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.build.utils.InfoPlist;
import org.crossmobile.build.utils.SynchronizeHelpers;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.Param;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

public class PropertiesCreator extends GenericPropertiesCreator {

    private final String mainclass;

    public PropertiesCreator(Properties properties, String mainclass, File output, File projectpath) {
        super(properties, projectpath, output);
        this.mainclass = mainclass;
    }

    @Override
    public void execute(CMBuildEnvironment env) {
        SynchronizeHelpers.checkValidDotNotation("main class", mainclass, true);
        Log.info("Writing CrossMobile properties files");
        // Write cross mobile properties
        Properties crossmobile = new Properties();
        for (Param p : env.getParamset().runtime()) {
            String cmname = p.name;
            if (!cmname.startsWith("cm.") && !cmname.startsWith("org.crossmobile.cmplugin-"))
                cmname = "cm." + cmname;
            crossmobile.setProperty(cmname, prop(p.name));
        }

        String orientation = prop("orientations.initial");
        // This property has values 1..4
        crossmobile.setProperty("cm.orientation.initial", Integer.toString(InfoPlist.getOrientation(orientation, false)));
        // This property has bitmap values 2..32 (2^1, 2^2, 2^3, 2^4, possibly combined)
        crossmobile.setProperty("cm.orientations.masked.supported", Integer.toString(InfoPlist.getMaskedOrientations(prop("orientations.supported"))));

        StringWriter out = new StringWriter();
        try {
            crossmobile.store(out, null);
        } catch (IOException ex) {
            BaseUtils.throwException(ex);
        }
        write(out);
    }
}
