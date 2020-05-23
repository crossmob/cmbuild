/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import java.io.File;

public class Repository {

    String id = "userplugins";
    String name = "User Plugins";
    String url;
    File file;
    boolean cleanEntries = false;

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public boolean isCleanEntries() {
        return cleanEntries;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
