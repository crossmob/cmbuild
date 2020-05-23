/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib;

import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.build.xcode.XcodeTargetRegistry;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ProjectException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.bridge.system.BaseUtils.throwExceptionAndReturn;
import static org.crossmobile.build.AnnotationConfig.*;

public class AnnotationHelpers {

    public static Map<String, CodeAnnotations> getAnnotations(File annotationsDir) {
        if (annotationsDir == null) {
            Log.warning("Unable to locate null annotations directory");
            return Collections.emptyMap();
        }
        //noinspection ResultOfMethodCallIgnored
        annotationsDir.mkdirs();
        if (!annotationsDir.isDirectory()) {
            Log.warning("Annotations directory " + annotationsDir.getAbsolutePath() + " should be a folder");
            return Collections.emptyMap();
        }

        Map<String, CodeAnnotations> result = new HashMap<>();
        for (File f : listFiles(annotationsDir)) {
            if (!f.getName().toLowerCase().endsWith(OBJECTS_EXT))
                continue;
            String name = f.getName().substring(0, f.getName().length() - OBJECTS_EXT.length());
            CodeAnnotations ann = new CodeAnnotations();
            result.put(name, ann);
            try {
                FileUtils.read(new FileInputStream(f), f.getName(), line -> parseLine(ann, name, line));
            } catch (IOException ex1) {
                return throwExceptionAndReturn(ex1);
            } catch (ProjectException ex2) {
                return throwExceptionAndReturn(ex2.getCause());
            }
        }
        return result;
    }

    private static void parseLine(CodeAnnotations ann, String name, String line) {
        if (line.startsWith(ACTION + SEP))
            ann.actions.add(validate(line, 1)[0]);
        else if (line.startsWith(OUTLET + SEP)) {
            String[] parts = validate(line, 2);
            ann.outlets.put(parts[0], parts[1]);
        } else if (line.startsWith(NATIVE + SEP)) {
            String[] parts = validate(line, 3);
            ann.nativeCode.put(parts[0], new NativeCode(parts[1], parts[2]));
        } else if (line.startsWith(TARGET + SEP)) {
            String[] parts = validate(line, 3);
            XcodeTargetRegistry.register(name, Boolean.parseBoolean(parts[0]), Boolean.parseBoolean(parts[1]), parts[2].split(":"));
        } else
            throw new RuntimeException("Unknown annotation: " + line);
    }

    private static String[] validate(String line, int howMany) {
        String[] split = line.split(SEP);
        if (split.length != (howMany + 1))
            throw new RuntimeException("Invalid number of arguments: " + howMany + " required, " + (split.length - 1) + " found. Line: " + line);
        String[] result = new String[howMany];
        for (int i = 0; i < result.length; i++)
            result[i] = split[i + 1].replaceAll("\\\\\\\\", "\\\\s").replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t").replaceAll("\\\\s", "\\\\\\\\");
        return result;
    }

    public static class CodeAnnotations {

        private final Set<String> actions = new HashSet<>();
        private final Map<String, String> outlets = new HashMap<>();
        private final Map<String, NativeCode> nativeCode = new HashMap<>();

        public Iterable<String> getActions() {
            return actions;
        }

        public Iterable<String> getOutlets() {
            return outlets.keySet();
        }

        public String getOutletType(String outlet) {
            return outlets.get(outlet);
        }

        public Iterable<String> getNatives() {
            return nativeCode.keySet();
        }

        public NativeCode getNativeCode(String nativeName) {
            return nativeCode.get(nativeName);
        }
    }

    public static class NativeCode {

        public final String signature;
        public final String body;

        public NativeCode(String signature, String body) {
            this.signature = signature;
            this.body = body;
        }
    }
}
