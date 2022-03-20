/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.utils.Log;
import org.crossmobile.utils.ParamList;
import org.crossmobile.utils.ProjectException;
import org.crossmobile.utils.TemplateUtils;

import java.io.File;

public class LocalPropertiesManager {

    public static void createIfNotExist(File basedir) {
        File localfile = new File(basedir, "local.properties");
        if (!localfile.exists())
            try {
                TemplateUtils.updateProperties("local.properties", new File(basedir, "local.properties"), new ParamList(), null);
            } catch (ProjectException ex) {
                Log.error(ex);
            }
    }
}
