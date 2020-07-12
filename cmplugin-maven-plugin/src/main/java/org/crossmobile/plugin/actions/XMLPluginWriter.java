/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.Version;
import org.crossmobile.plugin.PluginRegistryFile;
import org.crossmobile.plugin.reg.Plugin;
import org.crossmobile.plugin.reg.PluginParam;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.XMLWalker;
import org.crossmobile.utils.plugin.DependencyItem;

import static org.crossmobile.bridge.ann.CMLibParam.ParamContext.Regular;

public class XMLPluginWriter {
    public static void storeForPlugin(PluginRegistryFile regFile, DependencyItem root, Registry reg) {
        XMLWalker walker = prepare(regFile);
        for (String pluginName : reg.plugins().plugins()) {
            if (pluginName.equals(Version.ARTIFACTID))
                continue;
            Plugin plugin = reg.plugins().getPluginData(pluginName);
            walker.toTag().add("plugin").tag("p");
            walker.toTag("p").add("groupId").setText(root.getGroupID());
            walker.toTag("p").add("artifactId").setText("cmplugin-" + plugin.getName());
            walker.toTag("p").add("version").setText(root.getVersion());
            walker.toTag("p").add("name").setText(plugin.getDisplayName());
            walker.toTag("p").add("description").setText(plugin.getDescription());
            if (!plugin.getUrl().isEmpty())
                walker.toTag("p").add("url").setText(plugin.getUrl());
            for (String paramName : plugin.getParams()) {
                PluginParam param = plugin.getParam(paramName);
                walker.toTag("p").add("param").tag("m");
                walker.toTag("m").add("property").setText(paramName);
                walker.toTag("m").add("description").setText(param.getDescription().isEmpty() ? paramName : param.getDescription());
                if (!param.getMeta().isEmpty())
                    walker.toTag("m").add("meta").setText(param.getMeta());
                if (param.getContext() != Regular)
                    walker.toTag("m").add("context").setText(param.getContext().name().toLowerCase());
            }
        }
        walker.store(regFile.file, true);
    }

    public static void storeForTheme(PluginRegistryFile regFile, String groupId, String artifactId, String version, String name, String description) {
        XMLWalker walker = prepare(regFile);
        walker.toTag().add("plugin").tag("p");
        walker.toTag("p").add("groupId").setText(groupId);
        walker.toTag("p").add("artifactId").setText(artifactId);
        walker.toTag("p").add("version").setText(version);
        walker.toTag("p").add("name").setText(name);
        walker.toTag("p").add("description").setText(description);
        walker.store(regFile.file, true);
    }

    private static XMLWalker prepare(PluginRegistryFile regFile) {
        FileUtils.mkdirs(regFile.file.getParentFile());
        if (regFile.file.exists() && !regFile.file.delete())
            throw new RuntimeException("Unable to delete file " + regFile.file.getAbsolutePath());
        XMLWalker walker = new XMLWalker();
        walker.add("plugins").tag().setAttribute("type", regFile.type.name());
        return walker;
    }
}
