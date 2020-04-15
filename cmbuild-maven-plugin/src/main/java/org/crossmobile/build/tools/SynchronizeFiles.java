// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.utils.Log;

import java.io.File;
import java.util.*;

import static java.io.File.separator;
import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.bridge.system.MaterialsCommon.MATERIALS_TAG;
import static org.crossmobile.prefs.Config.EXCEPTIONS;
import static org.crossmobile.utils.FileUtils.Predicates.extensions;
import static org.crossmobile.utils.FileUtils.Predicates.noHidden;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.TextUtils.plural;

public class SynchronizeFiles {

    private final static String[] BLACK_LIST =
            {"org" + separator + "crossmobile" + separator + "sys",
                    "org" + separator + "crossmobile" + separator + MATERIALS_TAG};
    private static final List<String> WHITELIST = Collections.singletonList("Dummy.swift");

    public static boolean synchronizeChangedJavaFiles(File classesDir, File current, File diff) {
        delete(diff);
        int changedFiles = sync(classesDir, current, diff, true, base -> {
            if (base.isFile() && !base.getName().endsWith(".class"))
                return false;
            for (String blackListed : BLACK_LIST)
                if (base.getPath().endsWith(blackListed))
                    return false;
            return true;
        });
        trimEmptyDirs(current);
        trimEmptyDirs(diff);
        Log.info(changedFiles == 0 ? "Source files are in sync" : "Found " + changedFiles + " new class" + plural(changedFiles, "es"));
        return changedFiles > 0;
    }

    public static void syncChangedObjCFiles(File classesDir, File cacheSource, File xcodeSource) {
        sync(cacheSource, xcodeSource, null, false, null);

        Collection<String> existing = new LinkedHashSet<>();
        forAllFiles(classesDir, extensions(".class"), (path, file) -> {
            String cname = path + '_' + file.getName().substring(0, file.getName().length() - 6);
            cname = cname.replace('/', '_').replace('\\', '_').replace('.', '_').replace('$', '_');
            existing.add(cname);
        });

        existing.addAll(Arrays.asList(EXCEPTIONS));

        StringBuilder willRemove = new StringBuilder();
        forAllFiles(xcodeSource, noHidden(), (path, f) -> {
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

    public static void cleanUpJavaFiles(File java, File objc) {
        Set<String> javaFiles = new HashSet<>();
        for (File child : listFiles(java))
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
            for (File child : listFiles(source))
                cleanUpJavaFiles(child, prefix, list);
        }
    }

    private static String getName(String prefix, String name) {
        name = name.replace('$', '_');
        return prefix.isEmpty() ? name : prefix + "_" + name;
    }

    private static int matchObjCFiles(Set<String> javaFiles, File objc) {
        int deleted = 0;
        for (File file : listFiles(objc)) {
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


}
