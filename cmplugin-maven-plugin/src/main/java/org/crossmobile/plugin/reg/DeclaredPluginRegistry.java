/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import org.crossmobile.bridge.ann.CMLib;
import org.crossmobile.bridge.ann.CMLibDepends;
import org.crossmobile.bridge.ann.CMLibParam;
import org.crossmobile.bridge.ann.CMPod;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.NamingUtils;
import org.crossmobile.utils.ReflectionUtils;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.crossmobile.plugin.utils.Texters.annName;
import static org.crossmobile.utils.NamingUtils.getClassNameFull;

public class DeclaredPluginRegistry {

    private static final Collection<Pattern> BLACKLIST = Arrays.asList(compile("\\.netbeans_automatic_build"),
            compile("java\\..*"),
            compile("META-INF\\..*"));

    private final Map<String, Plugin> plugins = new TreeMap<>();
    private final Map<String, Plugin> otherPlugins = new HashMap<>();
    private final Map<String, String> classes = new TreeMap<>();
    private final Registry reg;
    private boolean dirty = true;

    DeclaredPluginRegistry(Registry reg) {
        this.reg = reg;
    }

    public void register(Class<?> cls) {
        registerImpl(cls, plugins, false);
    }

    public void registerDependencies(Class<?> cls) {
        registerImpl(cls, otherPlugins, true);
    }

    private void registerImpl(Class<?> cls, Map<String, Plugin> repository, boolean isExternalDependency) {
        dirty = true;
        CMLib plugin = getPluginAnnotation(cls);
        if (plugin != null) {
            String name = getPluginName(plugin, cls, !isExternalDependency);
            if (isExternalDependency && name.isEmpty())
                return;
            classes.put(cls.getName(), name);
            Plugin data = repository.get(name);
            if (data == null)
                repository.put(name, data = new Plugin(name, isExternalDependency));
            data.setDisplayName(plugin.displayName(), annName(CMLib.class) + " property name for object " + getClassNameFull(cls));
            data.setDescription(plugin.description(), annName(CMLib.class) + " property description for object " + getClassNameFull(cls));
            data.setUrl(plugin.url(), annName(CMLib.class) + " property url for object" + getClassNameFull(cls));
            data.setInitializer(plugin.initializer(), annName(CMLib.class) + " property initializer for object " + getClassNameFull(cls));
            data.addAndroidInj(plugin.androidInjections());
            data.addImports(cls, plugin.includes());
            data.setOptionalLibraryBinary(plugin.optionalLibraryBinary());

            for (String lib : plugin.libs())
                data.addLib(lib);
            for (CMLibDepends dep : plugin.depends())
                data.addDependency(dep, cls, reg, !isExternalDependency);
            for (CMPod pod : plugin.pods())
                data.addPod(pod);
            for (String perm : plugin.permissions())
                data.addPermission(perm);
            for (CMLibParam param : plugin.params()) {
                String property = param.property();
                if (property.trim().isEmpty())
                    Log.error("Unable to retrieve empty property name " + annName(CMLibParam.class) + " for object " + getClassNameFull(cls));
                else {
                    PluginParam pd = data.getParam(property);
                    if (pd == null)
                        data.addParam(property, pd = new PluginParam());
                    pd.setDescription(param.description(), annName(CMLib.class) + " parameter description of property " + property + " for object " + getClassNameFull(cls));
                    pd.setMeta(param.meta(), annName(CMLib.class) + " parameter meta of property " + property + " for object " + getClassNameFull(cls));
                    pd.setContext(param.context(), annName(CMLib.class) + " parameter context of property " + property + " for object " + getClassNameFull(cls));
                }
            }
        }
    }

    public Iterable<String> plugins() {
        if (dirty)
            checkRegistry();
        return plugins.keySet();
    }

    public boolean pluginExists(String name) {
        return plugins.containsKey(name);
    }

    public Iterable<Plugin> pluginsData() {
        if (dirty)
            checkRegistry();
        return plugins.values();
    }

    private CMLib getPluginAnnotation(Class<?> cls) {
        CMLib library = cls.getAnnotation(CMLib.class);
        if (library == null) {
            Package pkg = ReflectionUtils.findPackage(cls.getPackage(), CMLib.class);
            if (pkg != null)
                library = pkg.getAnnotation(CMLib.class);
        }
        // We don't really care if the annotation is missing, since this is called only when registering
        return library;
    }

    private String getPluginName(CMLib plugin, Class<?> cls, boolean requireName) {
        String name = plugin.name();
        if (name.isEmpty())
            name = reg.packages().getPlugin(cls.getPackage().getName());
        if (name.isEmpty() && requireName)
            Log.error("Unable to locate plugin name for class " + cls.getName());
        return name;
    }

    public String getPlugin(String className) {
        if (dirty)
            checkRegistry();
        String plugin = classes.get(className);
        if (plugin != null && !plugin.isEmpty())
            return plugin;
        plugin = reg.packages().getPlugin(NamingUtils.getPackageName(className));
        if (plugin != null && !plugin.isEmpty())
            return plugin;
        if (!mightIgnoreItem(className))
            Log.error("Unable to locate registered plugin name of class " + className);
        return null;
    }

    public Plugin getPluginData(String plugin) {
        if (dirty)
            checkRegistry();
        Plugin result = plugins.get(plugin);
        return result == null
                ? otherPlugins.get(plugin)
                : result;
    }

    public String getNativeLibraries(String plugin) {
        if (dirty)
            checkRegistry();
        Plugin pd = plugins.get(plugin);
        if (pd == null) {
            Log.error("Unable to retrieve plugin " + plugin);
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            for (String lib : pd.getLibs())
                out.append(" ; ").append(lib);
            String libs = out.toString();
            return libs.isEmpty() ? libs : libs.substring(3);   // " ; ".length()
        }
    }

    public boolean mightIgnoreItem(String classname) {
        for (Pattern bl : BLACKLIST)
            if (bl.matcher(classname).matches())
                return true;
        return false;
    }

    private void checkRegistry() {
        dirty = false;
        for (String plugin : plugins.keySet()) {
            Plugin pd = plugins.get(plugin);
            if (pd.getDisplayName().isEmpty())
                Log.error("Plugin " + plugin + " has empty name");
            if (pd.getDescription().isEmpty())
                Log.error("Plugin " + plugin + " has empty description");
            for (String param : pd.getParams())
                if (pd.getParam(param).getDescription().isEmpty())
                    Log.error("Property " + param + " in plugin " + plugin + " has empty description");
        }
    }

    //TODO
    public String getLibs(String name) {
        return "UIKit.lib";
    }
}
