/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.i18n;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

public class TranslationTable implements Comparable<TranslationTable>, Iterable<TranslationElement> {
    public final String table;
    private final Collection<TranslationElement> elements = new TreeSet<>();

    public TranslationTable(String table) {
        Objects.requireNonNull(table);
        this.table = table;
    }

    @Override
    public int compareTo(TranslationTable o) {
        return table.compareTo(o.table);
    }

    public void add(TranslationElement translationElement) {
        elements.add(translationElement);
    }

    @Override
    public Iterator<TranslationElement> iterator() {
        return elements.iterator();
    }
}
