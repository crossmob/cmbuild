/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class NativeMethodRegistry {
    private final Collection<Class<?>> withNativeCode = new TreeSet<>(comparing(Class::getName));

    public void register(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            if (Modifier.isNative(method.getModifiers())) {
                withNativeCode.add(cls);
                break;
            }
        }
    }

    public Stream<Class<?>> stream() {
        return withNativeCode.stream();
    }

    public boolean hasNatives() {
        return !withNativeCode.isEmpty();
    }
}
