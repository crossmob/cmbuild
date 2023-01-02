/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.i18n;

import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalizedType {
    public final String locale;
    public final String fullPath;
    public final String basePath;
    public final String tableName;
    public final boolean isBase;

    public static LocalizedType getLocalized(File current, File baseDir) {
        String relative = FileUtils.getRelative(baseDir, current);
        if (relative == null)
            throw new RuntimeException("Unable to parse localizations on file " + current.getAbsolutePath() + " : Not inside " + baseDir.getAbsolutePath() + " directory");
        return getLocalized(relative);
    }

    public static LocalizedType getLocalized(String relativePath) {
        if (!relativePath.contains(".lproj")) // Optimization
            return null;
        List<String> parts = new ArrayList<>(Arrays.asList(relativePath.replace('\\', '/').split("/")));
        return parts.size() < 2 || !parts.get(parts.size() - 2).endsWith(".lproj")
                ? null
                : new LocalizedType(relativePath, parts);
    }

    private LocalizedType(String fullPath, List<String> parts) {
        this.fullPath = fullPath;
        String locale = parts.get(parts.size() - 2);
        this.locale = locale.substring(0, locale.length() - 6);  // ".lproj".length()
        isBase = this.locale.equals("Base");
        parts.remove(parts.size() - 2);
        basePath = TextUtils.iterableToString(parts, "/");
        tableName = FileUtils.removeExtension(basePath);
    }
}
