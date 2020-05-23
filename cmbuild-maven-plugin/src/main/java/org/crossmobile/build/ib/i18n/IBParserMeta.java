/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.i18n;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class IBParserMeta {
    private final Map<String, TranslationTable> tables = new TreeMap<>();
    private TranslationTable current;
    private final File annotationLocation;

    public IBParserMeta(File annotationLocation) {
        this.annotationLocation = annotationLocation;
    }

    public void beginFile(File current, File baseDir) {
        LocalizedType localizedType = LocalizedType.getLocalized(current, baseDir);
        if (localizedType != null && localizedType.isBase) {
            this.current = new TranslationTable(localizedType.tableName);
            tables.put(this.current.table, this.current);
        } else
            this.current = null;
    }

    public void endFile() {
        current = null;
    }

    public void addLocalization(String classType, String itemId, String property, String text) {
        if (current == null)
            throw new IllegalArgumentException("Unable to add localizations outside of an active file");
        current.add(new TranslationElement(classType, itemId, property, text));
    }

    public boolean currentFileIsLocalizable() {
        return current != null;
    }

    public TranslationTable current() {
        return current;
    }

    public TranslationTable get(String tableName) {
        return tableName == null ? null : tables.get(tableName);
    }

    public File getAnnotationLocation() {
        return annotationLocation;
    }
}
