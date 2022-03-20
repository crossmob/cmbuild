/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.utils;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.ClassFile;

import java.io.IOException;

public class ClassWriter {
    public static void saveClass(CtClass cls, String directoryName) throws CannotCompileException, IOException {
        cls.getClassFile().setMajorVersion(ClassFile.JAVA_8);
        cls.writeFile(directoryName);
        cls.defrost();
    }
}
