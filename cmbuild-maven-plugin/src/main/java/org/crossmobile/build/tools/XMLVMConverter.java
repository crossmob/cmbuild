/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.utils.Commander;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.SystemDependent;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.crossmobile.utils.FileUtils.delete;

public class XMLVMConverter {

    public static void convertJavaToObjC(File input, File output, File xmlvm, boolean safe) {
        Log.info("Start XMLVM conversion");
        delete(output);
        output.mkdirs();
        // Run XMLVM
        String[] command = new String[]{SystemDependent.getJavaExec(),
                "-Xmx1G", "-Dfile.encoding=UTF-8", "-jar",
                xmlvm.getAbsolutePath(),
                "--in=" + input.getAbsolutePath(),
                "--out=" + output.getAbsolutePath(),
                "--target=objc",
                safe ? "--safe-inheritance" : "",
                "--debug=warning",
                "--enable-ref-counting"};
        StringBuilder error = new StringBuilder();
        AtomicBoolean castError = new AtomicBoolean(false);
        Commander commander = new Commander(command);
        commander.setErrListener(txt -> {
            error.append(txt).append('\n');
            if (txt.contains("ClassCastException") && txt.contains("InterfaceMethodRef"))
                castError.set(true);
        });
        commander.setOutListener(Log::warning);
        commander.exec();
        commander.waitFor();
        if (commander.exitValue() != 0) {
            Log.error(error.toString());
            throw new RuntimeException(castError.get()
                    ? "The invocation of a call to super method in a default interface implementation is not supported yet"
                    : "Unable to run XMLVM, see debug log for more information");
        }
    }
}
