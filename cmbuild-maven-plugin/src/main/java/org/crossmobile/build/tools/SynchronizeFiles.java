/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.bridge.system.BaseUtils.throwException;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.TextUtils.plural;

public class SynchronizeFiles {

    public static boolean synchronizeChangedJavaFiles(File classesDir, File current, File diff) {
        delete(diff);
        int changedFiles = sync(classesDir, current, diff, true, f -> f.isDirectory() || f.getName().endsWith(".class"));
        trimEmptyDirs(current);
        trimEmptyDirs(diff);
        Log.info(changedFiles == 0 ? "Source files are in sync" : "Found " + changedFiles + " new class" + plural(changedFiles, "es"));
        return changedFiles > 0;
    }

    public static void syncChangedObjCFiles(Collection<String> allObjCFiles, File cacheSource, File xcodeSource) {
        // Copy cached files to their correct location
        int updated = 0;
        for (File diffFile : listFiles(cacheSource))
            if (diffFile.getName().endsWith(".h") || diffFile.getName().endsWith(".m"))
                if (writeIfDiffers(new File(xcodeSource, diffFile.getName()), FileUtils.read(diffFile)) == null)
                    throwException(new IOException("Unable to move file " + diffFile.getAbsolutePath() + " to directory " + xcodeSource.getAbsolutePath()));
                else {
                    Log.debug("Updating file " + diffFile.getName());
                    updated++;
                }
        if (updated > 0)
            Log.info(updated + " file" + plural(updated) + " updated");

        // Clean up removed files
        int removed = 0;
        for (File objcFile : listFiles(xcodeSource)) {
            String fileName = objcFile.getName();
            if (fileName.endsWith(".h") || fileName.endsWith(".m")) {
                String objcName = fileName.substring(0, fileName.length() - 2);
                if (!allObjCFiles.contains(objcName)) {
                    FileUtils.delete(objcFile);
                    Log.debug("Removing file " + fileName);
                    removed++;
                }
            }
        }
        if (removed > 0)
            Log.info("Removed " + removed + " obsolete file" + plural(removed));
    }
}
