/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.i18n;

public class TranslationElement implements Comparable<TranslationElement> {
    public final String classType;
    public final String key;
    public final String objectId;
    public final String property;
    public final String text;

    public static String toKey(String objectId, String property) {
        return objectId + "." + property;
    }

    public TranslationElement(String classType, String itemID, String property, String text) {
        this.classType = classType;
        this.objectId = itemID;
        this.property = property;
        this.text = text;
        this.key = toKey(objectId, property);
    }

    @Override
    public int compareTo(TranslationElement o) {
        int v = classType.compareTo(o.classType);
        if (v == 0) {
            v = objectId.compareTo(o.objectId);
            if (v == 0) {
                v = property.compareTo(o.property);
                if (v == 0)
                    v = 1;  // Shouldn't come here
            }
        }
        return v;
    }

    @Override
    public String toString() {
        return "TranslationElement{" +
                "classType='" + classType + '\'' +
                ", objectId='" + objectId + '\'' +
                ", property='" + property + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

