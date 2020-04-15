// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.utils.Log;

import java.io.File;
import java.util.*;

import static java.io.File.separator;
import static org.crossmobile.bridge.system.MaterialsCommon.MATERIALS_TAG;
import static org.crossmobile.prefs.Config.EXCEPTIONS;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.FileUtils.Predicates.extensions;
import static org.crossmobile.utils.FileUtils.Predicates.noHidden;
import static org.crossmobile.utils.TextUtils.plural;

public class SynchronizeFiles {

    private final static String[] BLACK_LIST =
            {"org" + separator + "crossmobile" + separator + "sys",
                    "org" + separator + "crossmobile" + separator + MATERIALS_TAG};
    private static final List<String> WHITELIST = Collections.singletonList("Dummy.swift");

    public static boolean synchronizeChangedFiles(File base, File current, File diff) {
        delete(diff);
        Collection<File> result = new LinkedHashSet<>();

        synchronizeChangedFiles(base, current, diff, result);

        int hm = result.size();
        if (hm == 0)
            Log.info("Source files are in sync");
        else
            Log.info("Found " + hm + " new class" + plural(hm, "es"));
        return hm > 0;
    }

    private static void synchronizeChangedFiles(File base, File current, File diff, Collection<File> result) {
        if (base.isDirectory()) {
            if (isBlackListed(base)) {
                delete(current);
                delete(diff);
            } else {
                if (!current.isDirectory()) {
                    delete(current);
                    //noinspection ResultOfMethodCallIgnored
                    current.mkdirs();
                }
                if (!diff.isDirectory()) {
                    delete(diff);
                    //noinspection ResultOfMethodCallIgnored
                    diff.mkdirs();
                }
                File[] children = base.listFiles();
                if (children != null && children.length > 0)
                    for (File child : children)
                        synchronizeChangedFiles(child, new File(current, child.getName()), new File(diff, child.getName()), result);
            }
        } else if (base.isFile())
            if (current.getName().toLowerCase().endsWith(".class") && !equalFiles(base, current)) {
                delete(current);
                delete(diff);
                copy(base, current);
                copy(base, diff);
                result.add(diff);
            }
    }

    private static boolean isBlackListed(File base) {
        for (String blackListed : BLACK_LIST)
            if (base.getPath().endsWith(blackListed))
                return true;
        return false;
    }

    public static void cleanUpJavaFiles(File java, File objc) {
        Set<String> javaFiles = new HashSet<>();
        File[] jFiles = java.listFiles();
        if (jFiles != null)
            for (File child : jFiles)
                cleanUpJavaFiles(child, "", javaFiles);
        javaFiles.addAll(Arrays.asList(EXCEPTIONS));
        int deleted = matchObjCFiles(javaFiles, objc);
        if (deleted > 0)
            Log.info("Cleaned " + deleted + " file" + plural(deleted));
    }

    private static void cleanUpJavaFiles(File source, String prefix, Set<String> list) {
        if (source.isFile()) {
            String name = source.getName();
            if (name.toLowerCase().endsWith(".class")) {
                name = name.substring(0, name.length() - 6);
                if (!name.isEmpty())
                    list.add(getName(prefix, name));
            }
        } else if (source.isDirectory()) {
            prefix = getName(prefix, source.getName());
            File[] files = source.listFiles();
            if (files != null)
                for (File child : files)
                    cleanUpJavaFiles(child, prefix, list);
        }
    }

    private static String getName(String prefix, String name) {
        name = name.replace('$', '_');
        return prefix.isEmpty() ? name : prefix + "_" + name;
    }

    private static int matchObjCFiles(Set<String> javaFiles, File objc) {
        int deleted = 0;
        File[] objCFiles = objc.listFiles();
        if (objCFiles != null)
            for (File file : objCFiles) {
                String name = file.getName();
                String lname = name.toLowerCase();
                if (lname.endsWith(".h") || lname.endsWith(".m")) {
                    String strippedName = name.substring(0, name.length() - 2);
                    if (!javaFiles.contains(strippedName)) {
                        delete(file);
                        deleted++;
                    }
                }
            }
        return deleted;
    }

    public static void syncChangedObjCFiles(File classesDir, File cacheSource, File xcodeSource) {
        sync(cacheSource, xcodeSource, false);

        Collection<String> existing = new LinkedHashSet<>();
        forAllRecursively(classesDir, extensions(".class"), (path, file) -> {
            String cname = path + '_' + file.getName().substring(0, file.getName().length() - 6);
            cname = cname.replace('/', '_').replace('\\', '_').replace('.', '_').replace('$', '_');
            existing.add(cname);
        });

        existing.addAll(Arrays.asList(EXCEPTIONS));

        StringBuilder willRemove = new StringBuilder();
        forAllRecursively(xcodeSource, noHidden(), (path, f) -> {
            if (!existing.contains(getBasename(f.getName())) && !WHITELIST.contains(f.getName())) {
                Log.warning("Removing missing file " + f.getName());
                //noinspection ResultOfMethodCallIgnored
                f.delete();
                willRemove.append(' ').append(f.getName());
            }
        });
        if (willRemove.length() > 0)
            Log.warning("Removed missing files:" + willRemove.toString());
    }
}
