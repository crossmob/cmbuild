/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.bridge.system.JsonHelper;

import java.util.*;

import static org.crossmobile.bridge.system.BaseUtils.throwExceptionAndReturn;

public class ObjCSuperMethods {
    private static final String SUPER_IMPORTS_KEY = "superImports";
    private static final String SUPER_CODE_KEY = "superCode";

    private final Collection<String> superImports = new TreeSet<>();
    private final Map<String, Collection<String>> superCode = new TreeMap<>();

    @SuppressWarnings("unchecked")
    public void declare(String className, String data) {
        try {
            Map<String, Object> json = (Map<String, Object>) JsonHelper.decode(data);
            add((Collection<String>) json.get(SUPER_IMPORTS_KEY), (Map<String, Collection<String>>) json.get(SUPER_CODE_KEY));
        } catch (Exception e) {
            BaseUtils.throwException(new IllegalArgumentException("Unable to parse cache from super methods of class " + className, e));
        }
    }

    public String declare(Collection<String> cImports, Map<String, Collection<String>> cCode) {
        add(cImports, cCode);
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put(SUPER_IMPORTS_KEY, cImports);
            data.put(SUPER_CODE_KEY, cCode);
            return JsonHelper.encode(data, false);
        } catch (Exception e) {
            return throwExceptionAndReturn(e);
        }
    }

    private void add(Collection<String> cImports, Map<String, Collection<String>> cCode) {
        superImports.addAll(cImports);
        cCode.forEach((key, value) -> superCode.computeIfAbsent(key, k -> new TreeSet<>()).addAll(value));
    }

    public String getDefinitions() {
        StringBuilder out = new StringBuilder();
        for (String importedClass : superImports)
            out.append("#import \"").append(importedClass).append(".h\"\n");

        superCode.forEach((objCClass, codeSegments)->{
            if (!codeSegments.isEmpty()) {
                out.append("\n@implementation ").append(objCClass).append("$Ext (cm_super_impl)\n\n");
                for (String selector : codeSegments)
                    out.append(selector);
                out.append("@end\n");
            }
        });
        return out.toString();
    }
}
