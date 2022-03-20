/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.build.utils.SynchronizeHelpers;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.PluginMetaData;

import java.io.File;

import static org.crossmobile.build.utils.Config.PLUGIN_LAUNCHER_FILE;
import static org.crossmobile.build.utils.Templates.*;
import static org.crossmobile.utils.TextUtils.plural;

public class PluginsLauncher {

    private final Iterable<PluginMetaData> metadata;
    private final File generated;
    private final File cache;

    public PluginsLauncher(Iterable<PluginMetaData> metaData, File generated, File cache) {
        this.metadata = metaData;
        this.generated = new File(generated, PLUGIN_LAUNCHER_FILE);
        this.cache = new File(cache, PLUGIN_LAUNCHER_FILE);
    }

    public void execute() {
        SynchronizeHelpers.checkVariable("generated", cache);
        generated.getParentFile().mkdirs();
        cache.getParentFile().mkdirs();
        if (!generated.getParentFile().isDirectory())
            throw new RuntimeException("Unable to create plugin list");

        if (!cache.getParentFile().isDirectory())
            throw new RuntimeException("Unable to create plugin list");

        StringBuilder initializeData = new StringBuilder();
        int size = getCode("initialize", null, initializeData);
        StringBuilder earlyInitializeData = new StringBuilder();
        getCode("earlyInitialize", "Object", earlyInitializeData);

        FileUtils.write(cache, PLUGININTIALIZER_TEMPLATE
                .replace(PLUGININITIALIZER_ANCHOR, initializeData.toString())
                .replace(PLUGINEARLYINITIALIZER_ANCHOR, earlyInitializeData.toString())
        );
        if (size > 0)
            Log.info("Recognized " + size + " plugin initializer" + plural(size));
        FileUtils.sync(cache, generated, null, true, null);
    }

    private int getCode(String methodName, String argType, StringBuilder result) {
        int found = 0;
        for (PluginMetaData info : metadata) {
            String classname = info.getJavaInitializer();
            if (classname != null && !classname.isEmpty()) {
                result.append("        try {\n"
                        + "            Class<?> pluginClass = Class.forName(\"" + classname + "\");\n"
                        + "            pluginClass.getMethod(\"" + methodName + "\""
                        + (argType == null ? "" : ", " + argType + ".class") + ").invoke(pluginClass.newInstance()"
                        + (argType == null ? "" : ", context") + ");\n"
                        + "        } catch (Exception e) {\n"
                        + "            System.err.println(\"Plugin not initialized: " + classname + "\\n    \" + e.toString());\n"
                        + "        }\n");
                found++;
            }
        }
        return found;
    }

}
