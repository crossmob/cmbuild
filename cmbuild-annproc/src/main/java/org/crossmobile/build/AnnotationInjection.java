/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static org.crossmobile.build.AnnotationConfig.*;

public final class AnnotationInjection {

    static final String METHOD = "method";
    static final String CODE = "code";

    public final Collection<String> actions = new ArrayList<>();
    public final Map<String, String> outlets = new HashMap<>();
    public final static Map<String, Collection<String>> generatedOutlets = new HashMap<>();
    public final Map<String, Map<String, String>> nativemethods = new HashMap<>();
    public boolean inMainTarget;
    public boolean principalClass;
    public String otherTargets;

    public AnnotationInjection(boolean dummy) { // really?????? - why should be like this?
    }

    // Why this? Because it seems that library calls are not very friendly with compile-time code (i.e. no JSON could be used)
    public final void serializeForXcode(Writer out) throws IOException {
        for (String action : actions)
            out.append(ACTION).append(SEP).append(esc(action)).append(END);
        for (String outletKey : outlets.keySet()) {
            String convType = outlets.get(outletKey).replace('.', '_');
            out.append(OUTLET).append(SEP).append(esc(outletKey + "_" + convType)).append(SEP).append(esc(convType)).append(END);
        }
        for (String nativeMethodKey : nativemethods.keySet()) {
            Map<String, String> nativeMethod = nativemethods.get(nativeMethodKey);
            out.append(NATIVE).append(SEP).append(esc(nativeMethodKey)).append(SEP).append(esc(nativeMethod.get(METHOD))).append(SEP).append(esc(nativeMethod.get(CODE))).append(END);
        }
        if (otherTargets != null)
            out.append(TARGET).append(SEP).
                    append(inMainTarget ? "true" : "false").append(SEP).
                    append(principalClass ? "true" : "false").append(SEP).
                    append(otherTargets).append(END);
        out.flush();
        out.close();
    }

    public static final String esc(String input) {
        return input.
                replaceAll("\\\\", "\\\\\\\\").
                replaceAll("\n", "\\\\n").
                replaceAll("\t", "\\\\t");
    }

    public boolean hasOutlets() {
        return !outlets.isEmpty();
    }
}
