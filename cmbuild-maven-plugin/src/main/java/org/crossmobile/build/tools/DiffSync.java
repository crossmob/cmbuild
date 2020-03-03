// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.utils.Log;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

import static org.crossmobile.bridge.system.MaterialsCommon.MATERIALS_TAG;
import static org.crossmobile.utils.FileUtils.*;
import static org.crossmobile.utils.TextUtils.plural;

public class DiffSync {

    private final static String[] BLACK_LIST =
            {"org" + File.separator + "crossmobile" + File.separator + "sys",
                    "org" + File.separator + "crossmobile" + File.separator + MATERIALS_TAG};

    public static boolean exec(File base, File current, File diff) {
        delete(diff);
        Collection<File> result = new LinkedHashSet<>();
        execDo(base, current, diff, result);
        int hm = result.size();
        if (hm == 0)
            Log.info("Source files are in sync");
        else
            Log.info("Found " + hm + " new class" + plural(hm, "es"));
        return hm > 0;
    }

    private static void execDo(File base, File current, File diff, Collection<File> files) {
        if (base.isDirectory()) {
            if (isBlackListed(base)) {
                delete(current);
                delete(diff);
                return;
            }
            if (!current.isDirectory()) {
                delete(current);
                current.mkdirs();
            }
            if (!diff.isDirectory()) {
                delete(diff);
                diff.mkdirs();
            }
            File[] children = base.listFiles();
            if (children != null && children.length > 0)
                for (File child : children)
                    execDo(child, new File(current, child.getName()), new File(diff, child.getName()), files);
        } else if (base.isFile())
            if (current.getName().toLowerCase().endsWith(".class") && !equalFiles(base, current)) {
                delete(current);
                delete(diff);
                copy(base, current);
                copy(base, diff);
                files.add(diff);
            }
    }

    private static boolean isBlackListed(File base) {
        for (String blackListed : BLACK_LIST)
            if (base.getPath().endsWith(blackListed))
                return true;
        return false;
    }

}
