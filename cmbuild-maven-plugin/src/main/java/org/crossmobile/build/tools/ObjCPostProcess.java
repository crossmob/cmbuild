/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import javassist.CtClass;
import javassist.CtMethod;
import org.crossmobile.bridge.system.Pair;
import org.crossmobile.build.ib.AnnotationHelpers.CodeAnnotations;
import org.crossmobile.build.ib.AnnotationHelpers.NativeCode;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.ReverseCodeCollection;
import org.crossmobile.utils.ReverseCodeCollection.ReverseMethod;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Matcher.quoteReplacement;
import static org.crossmobile.build.tools.ObjCSourceFile.getObjCName;
import static org.crossmobile.utils.NamingUtils.execSignature;
import static org.crossmobile.utils.NamingUtils.toObjC;


public class ObjCPostProcess {
    private static final Pattern javaclass = Pattern.compile("[a-zA-Z][a-zA-Z_0-9$]*(\\.[a-zA-Z][a-zA-Z_0-9$]*)*");
    private static final Pattern unicodeP = Pattern.compile("\\\\([0-9]+)");
    private static final Pattern constrP = Pattern.compile(": (crossmobile_ios_[^\\s]+)");

    /**
     * @param cacheSource    The directory location of the objective C file to process
     * @param ignoreIncludes a semicolon separated list of canonical class names,  used to remove dependencies in
     *                       external libraries that are not implemented yet.
     */
    public static Collection<String> updateObjCFiles(File cacheSource, File cacheSuper, File javaDiff, File javaAll, ReverseCodeCollection dbn, Map<String, CodeAnnotations> annotations, String ignoreIncludes) {
        Log.info("Updating ObjectiveC files");
        List<Pattern> ignoreIncludePatterns = retrieveIgnoreIncludePattern(ignoreIncludes);
        return ObjCSourceFile.find(cacheSource, cacheSuper, javaDiff, javaAll, o -> {
            StringBuilder hAppend = new StringBuilder();
            StringBuilder mAppend = new StringBuilder();

            { /* Inject reverse methods */
                StringBuilder mIncludes = new StringBuilder();
                Collection<String> superImports = new TreeSet<>();
                Map<String, Collection<String>> superCode = new HashMap<>();
                for (CtMethod m : o.cls.getDeclaredMethods()) {
                    String signature = execSignature(m);
                    Pair<ReverseMethod, CtClass> methodData = dbn.getMethodData(o.cls, signature);
                    if (methodData != null) {
                        ReverseMethod data = methodData.a;
                        CtClass baseClass = methodData.b;
                        mAppend.append(data.getReverse());
                        data.getReverseImports().forEach(i -> mIncludes.append("#import \"").append(i).append(".h\"\n"));

                        if (!baseClass.isInterface()) {
                            superImports.add(toObjC(baseClass.getName()));
                            superImports.addAll(data.getSuperImports());
                            superCode.computeIfAbsent(getObjCName(baseClass.getName()), a -> new HashSet<>()).add(data.getSuper());
                        }
                    }
                }
                o.declareSuper(superImports, superCode);
                if (mIncludes.length() > 0)
                    o.mUpdate(o.mRetrieve().replaceFirst("#import", quoteReplacement(mIncludes.toString() + "#import")));
            }

            CodeAnnotations codeAnnotations = annotations.get(o.objCName);
            if (codeAnnotations != null) {
                /* Inject IBAction */
                for (String action : codeAnnotations.getActions()) {
                    hAppend.append("- (IBAction) ").append(action).append(":(id) sender;\n");
                    mAppend.append("- (IBAction) ").append(action).append(":(id) sender {\n    [self ").append(action).append("___java_lang_Object:sender];\n}\n\n");
                }
                /* Inject IBOutlet */
                for (String oname : codeAnnotations.getOutlets()) {
                    String otype = codeAnnotations.getOutletType(oname);
                    String fname = oname.replace("_" + otype, "") + "_field";
                    String target = "@property " + otype + "* " + fname + ";";
                    if (o.hContains(target)) {
                        o.hReplace(target, "@property (strong) IBOutlet " + o.simplifyType(otype) + "* " + fname + ";");
                        if (otype.startsWith("crossmobile_ios_"))
                            o.hReplace("@class " + otype + ";", "#import \"" + otype + ".h\"");
                    } else
                        Log.error("Unable to find " + oname + ", skipping");
                }
                /* Inject native methods */
                for (String oldmethod : codeAnnotations.getNatives()) {
                    String oldcode = "// [NATIVE PLACEHOLDER] " + oldmethod;
                    NativeCode nativeCode = codeAnnotations.getNativeCode(oldmethod);
                    if (!o.mContains(oldcode))
                        throw new RuntimeException("Unbale to match signature " + oldcode);
                    o.mReplace(oldcode, nativeCode.body);
                    o.mReplace(oldmethod + "\n{", nativeCode.signature + "\n{");
                    o.mReplace(oldmethod + "\r\n{", nativeCode.signature + "\r\n{");
                }
            }

            /* Update constructors */
            Matcher cmatcher = constrP.matcher(o.hRetrieve());
            if (cmatcher.find())
                o.hUpdate(cmatcher.replaceFirst(": $1\\$Ext"));

            /* Update Unicode */
            o.hUpdate(parseUnicode(o.hRetrieve()));
            o.mUpdate(parseUnicode(o.mRetrieve()));

            /* Safer instanceof */
            o.mUpdate(o.mRetrieve().replaceAll("isKindOfClass: objc_getClass\\(\"crossmobile_ios_", "isKindOfClass: objc_getClass(\"").
                    replaceAll("conformsToProtocol: objc_getProtocol\\(\"crossmobile_ios_", "conformsToProtocol: objc_getProtocol(\""));

            /* Ignore given includes */
            for (Pattern pattern : ignoreIncludePatterns) {
                Matcher matcher = pattern.matcher(o.mRetrieve());
                if (matcher.find()) {
                    Log.debug("Found ignoring imported pattern " + pattern.pattern());
                    o.mUpdate(matcher.replaceFirst("// " + matcher.group()));
                }
            }

            if (hAppend.length() > 0)
                o.hReplace("@end", hAppend.toString() + "\n@end");
            if (mAppend.length() > 0)
                o.mReplace("@end", mAppend.toString() + "\n@end");
            o.commit();
        });
    }

    private static String parseUnicode(String input) {
        Matcher m = unicodeP.matcher(input);
        while (m.find()) {
            input = input.substring(0, m.start()) + new String(Character.toChars(Integer.parseInt(m.group(1), 8))) + input.substring(m.end());
            m.reset(input);
        }
        return input;
    }

    static List<Pattern> retrieveIgnoreIncludePattern(String ignoreIncludes) {
        if (ignoreIncludes == null || ignoreIncludes.trim().isEmpty())
            return Collections.emptyList();
        List<Pattern> patterns = new ArrayList<>();
        for (String part : ignoreIncludes.split(";")) {
            part = part.trim();
            if (part.isEmpty())
                continue;
            if (javaclass.matcher(part).matches()) {
                Log.debug("Ignoring inclusion of imported class " + part);
                part = part.replace('.', '_').replaceAll("\\.", "\\.");
                patterns.add(Pattern.compile("#import \"" + part + "\\.h\""));
            } else
                Log.error("User requested to ignore class name " + part + ", which doesn't look like a Java class.");
        }
        return patterns;
    }
}
