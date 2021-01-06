/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import javassist.CtClass;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.ReverseCodeCollection;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.io.File.separator;
import static java.util.Collections.singleton;
import static org.crossmobile.bridge.system.RuntimeCommons.MATERIALS_TAG;

class ObjCSourceFile {
    private static final Pattern depack = Pattern.compile("crossmobile_ios_([a-zA-Z]*)_([a-zA-Z]*)");

    private final static Collection<String> BLACK_LIST = Arrays.asList(
            "org" + separator + "crossmobile" + separator + "sys",
            "org" + separator + "crossmobile" + separator + MATERIALS_TAG);

    public static final BiPredicate<String, File> OBJC_COMPATIBLE_CLASSES = (path, file) -> {
        if (!file.getName().endsWith(".class"))
            return false;
        for (String bl : BLACK_LIST)
            if (path.startsWith(bl))
                return false;
        return true;
    };

    final String objCName;
    final CtClass cls;
    private final ObjCSuperMethods superMethods;
    private final File hFileRef;
    private final File mFileRef;
    private final File cacheSuper;
    private String hFileData;
    private String mFileData;
    private boolean hDirty = false, mDirty = false;

    private ObjCSourceFile(CtClass cls, String objCName, File cacheSource, File cacheSuper, ObjCSuperMethods superMethods) {
        this.cls = cls;
        this.objCName = objCName;
        this.superMethods = superMethods;
        this.hFileRef = new File(cacheSource, objCName + ".h");
        this.mFileRef = new File(cacheSource, objCName + ".m");
        this.cacheSuper = cacheSuper;
        this.hFileData = FileUtils.read(hFileRef);
        this.mFileData = FileUtils.read(mFileRef);
        if (hFileData == null)
            throw new NullPointerException("Unable to read Objective C include file " + hFileRef.getAbsolutePath());
        if (mFileData == null)
            throw new NullPointerException("Unable to read Objective C source file " + mFileRef.getAbsolutePath());
    }

    static Collection<String> find(File cacheSource, File cacheSuper, File javaDiff, File javaAll, Consumer<ObjCSourceFile> active) {
        ReverseCodeCollection rcc = new ReverseCodeCollection(singleton(javaAll));
        ObjCSuperMethods superMethods = new ObjCSuperMethods();
        Collection<String> allObjCFiles = new HashSet<>();
        FileUtils.forAllFiles(javaAll, OBJC_COMPATIBLE_CLASSES, (relativePath, file) -> {
            File diffClassFile = new File(javaDiff, relativePath + separator + file.getName());
            String className = getClassName(relativePath, file.getName());
            String objCname = getObjCName(className);
            File cacheSuperFile = new File(cacheSuper, objCname);
            allObjCFiles.add(objCname);
            if (diffClassFile.isFile())
                active.accept(new ObjCSourceFile(rcc.getClassPool().getOrNull(className), objCname, cacheSource, cacheSuperFile, superMethods));
            else
                superMethods.declare(objCname, FileUtils.read(cacheSuperFile));
        });
        /* Create super method support */
        FileUtils.write(new File(cacheSource, "crossmobilesuperimpl.m"), superMethods.getDefinitions());
        allObjCFiles.add("crossmobilesuperimpl");
        return allObjCFiles;
    }

    private static String getClassName(String relativePath, String name) {
        return relativePath.replace('/', '.').replace('\\', '.') // make it class-like
                + "." + name.substring(0, name.length() - 6);  // remove the .class extension
    }

    String simplifyType(String type) {
        Matcher m = depack.matcher(type);
        return m.matches() ? m.replaceFirst("$2") : type;
    }

    boolean hContains(String target) {
        return hFileData.contains(target);
    }

    public String hRetrieve() {
        return hFileData;
    }

    void hReplace(String source, String target) {
        hUpdate(hFileData.replace(source, target));
    }

    boolean mContains(String target) {
        return mFileData.contains(target);
    }

    public String mRetrieve() {
        return mFileData;
    }

    void mReplace(String source, String target) {
        mUpdate(mFileData.replace(source, target));
    }

    public void hUpdate(String value) {
        if (!value.equals(hFileData)) {
            hDirty = true;
            hFileData = value;
        }
    }

    public void mUpdate(String value) {
        if (!value.equals(mFileData)) {
            mDirty = true;
            mFileData = value;
        }
    }

    void commit() {
        if (hDirty && FileUtils.write(hFileRef, hFileData) == null)
            BaseUtils.throwException(new IOException("Unable to save file " + hFileRef.getAbsolutePath()));
        if (mDirty && FileUtils.write(mFileRef, mFileData) == null)
            BaseUtils.throwException(new IOException("Unable to save file " + mFileRef.getAbsolutePath()));
    }

    public void declareSuper(Collection<String> superImports, Map<String, Collection<String>> superCode) {
        if (FileUtils.write(cacheSuper, superMethods.declare(superImports, superCode)) == null)
            BaseUtils.throwException(new IOException("Unable to preserve super implementations for Objective C file " + objCName));
    }

    public static String getObjCName(String className) {
        return className.replace('.', '_').replace('$', '_');
    }
}
