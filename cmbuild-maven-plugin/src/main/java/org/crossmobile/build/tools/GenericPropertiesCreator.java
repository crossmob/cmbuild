// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.utils.FileUtils;

import java.io.File;
import java.util.Properties;

public abstract class GenericPropertiesCreator {

    private final File output;
    protected final File projectpath;
    private final Properties prop;

    public GenericPropertiesCreator(Properties properties, File projectpath, File output) {
        this.output = output;
        this.projectpath = projectpath;
        this.prop = properties;
        output.getParentFile().mkdirs();
    }

    protected String prop(String property) {
        String val = prop.getProperty(property);
        if (val == null)
            throw new RuntimeException("Unable to find property " + property);
        return val;
    }

    protected void write(Object data) {
        FileUtils.write(output, data.toString());
    }

    public abstract void execute(CMBuildEnvironment env);

}
