// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.xcode.resources;

import org.crossmobile.build.utils.SynchronizeHelpers;
import org.crossmobile.utils.FileUtils;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import static org.crossmobile.bridge.system.BaseUtils.listFiles;

public class ResourceItem {

    private final String name;
    private final String[] elements;

    private final Set<File> files = new TreeSet<>();

    public ResourceItem(String name, String... elements) {
        this.name = name;
        this.elements = elements;
        SynchronizeHelpers.checkVariable("elements", elements);
        SynchronizeHelpers.checkVariable("name", name);
    }

    public void update(File base) {
        for (String path : elements) {
            path = path == null ? "" : path.trim();
            if (path.isEmpty())
                continue;

            // With relative paths, find actual target path
            File fpath = new File(path);
            if (!fpath.isAbsolute())
                fpath = new File(base, path);
            if (!fpath.exists())
                continue;

            FileUtils.fixLastModified(fpath);
            if (FileUtils.endsWithPathSeparator(path)) {   // File.separator - get children of this file
                for (File item : listFiles(fpath))
                    if (!item.getName().equals(".DS_Store"))
                        files.add(item);
            } else
                files.add(fpath);
        }
    }

    public Set<File> getResources() {
        return files;
    }

}
