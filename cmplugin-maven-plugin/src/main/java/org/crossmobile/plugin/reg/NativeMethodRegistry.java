/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;

public class NativeMethodRegistry {
    private final Collection<Class<?>> withNativeCode = new HashSet<>();

    public void register(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            if (Modifier.isNative(method.getModifiers())) {
                withNativeCode.add(cls);
                break;
            }
        }
    }
}
