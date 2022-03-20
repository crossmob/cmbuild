/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.utils;

import org.crossmobile.utils.CollectionUtils;
import org.crossmobile.utils.FilteredConsumer;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.plugin.DependencyItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static org.crossmobile.utils.CollectionUtils.asList;

public class DependencyJarResolver {

    public static final FilteredConsumer<String> ERROR = new FilteredConsumer<>(Log::error, t -> !t.contains("Class JavaLaunchHelper is implemented"));

    public static Collection<File> gatherLibs(DependencyItem root, boolean asRuntime) {
        Collection<File> jars = new TreeSet<>();
        gatherProgramAndEmbeddedLibs(root, asRuntime, null, jars, null);
        return jars;
    }

    public static void gatherProgramAndEmbeddedLibs(DependencyItem root, boolean asRuntime, String[] embedlibs, Collection<File> libraryjars, Collection<File> embeddedjars) {
        Collection<Pattern> patterns = new ArrayList<>();
        if (embedlibs != null && embedlibs.length > 0)
            for (String embedlib : embedlibs)
                if (embedlib != null && !embedlib.trim().isEmpty())
                    try {
                        patterns.add(Pattern.compile(embedlib));
                    } catch (Exception ignored) {
                    }
        Collection<DependencyItem> embeditems = CollectionUtils.asCollection(TreeSet.class, asRuntime ? root.getRuntimeDependencies(true) : root.getCompiletimeDependencies(true));
        Collection<DependencyItem> libitems = new TreeSet<>();
        addLibs(root, asRuntime, patterns, libitems);
        embeditems.removeAll(libitems);
        if (libraryjars != null) {
            libraryjars.addAll(asList(libitems, DependencyItem::getFile));
            Log.debug("JARs registered as library: " + libraryjars);
        }
        if (embeddedjars != null) {
            embeddedjars.addAll(asList(embeditems, DependencyItem::getFile));
            Log.debug("JARs registered as embedded: " + embeddedjars);
        }
    }

    private static void addLibs(DependencyItem parent, boolean asRuntime, Collection<Pattern> patterns, Collection<DependencyItem> libraryjars) {
        for (DependencyItem item : asRuntime ? parent.getRuntimeDependencies(false) : parent.getCompiletimeDependencies(false)) {
            boolean found = false;
            for (Pattern pattern : patterns)
                if (pattern.matcher(item.toString()).matches()) {
                    found = true;
                    break;
                }
            if (!found) {
                libraryjars.add(item);
                addLibs(item, asRuntime, patterns, libraryjars);
            }
        }
    }
}
