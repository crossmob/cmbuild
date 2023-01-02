/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.build.ib.i18n.IBParserMeta;
import org.crossmobile.build.ib.i18n.LocalizedType;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.JarUtils;
import org.crossmobile.utils.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.jar.JarFile;

import static org.crossmobile.bridge.system.RuntimeCommons.MATERIALS_TAG;
import static org.crossmobile.build.tools.StringsToPropertiesConverter.parseStrings;
import static org.crossmobile.build.tools.StringsToPropertiesConverter.parseStringsDict;
import static org.crossmobile.utils.FileUtils.forAllFiles;

public class MaterialsManager {

    //   private static final BufferedImage shape = ImageUtils.getImage("/buildres/layers/shape.png");

    public static void parseMaterials(IBParserMeta meta, File materialsDir, File outputDir) {
        forAllFiles(materialsDir, (path, file) -> {
            String name = file.getName();
            File output = outputDir == null ? null : new File(new File(outputDir, path), name); // could be null, no copy required
            if (name.startsWith("."))  // handle hidden files
                Log.warning("Ignoring hidden file: " + file.getAbsolutePath());
            else if (name.endsWith(".strings")) {
                LocalizedType localized = LocalizedType.getLocalized(file, materialsDir);
                if (output != null)     // need to copy localized files
                    FileUtils.write(output, parseStrings(file, localized, meta == null ? null : meta.get(localized == null || localized.isBase ? null : localized.tableName)));
                else if (localized != null && !localized.isBase)    // only display info about localized files, no copy needed
                    parseStrings(file, localized, meta == null ? null : meta.get(localized.tableName));
            } else if (output != null)  // parse the rest of files only if copy needed
                if (name.endsWith(".stringsdict"))
                    FileUtils.write(output, parseStringsDict(file));
                else
                    FileUtils.copy(file, output);
        });
    }

    @SuppressWarnings("UseSpecificCatch")
    public static void copyAndroidSys(Collection<File> plugins, File andrAsset, File andrRes) {
        String path = MaterialsManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath;
        try {
            decodedPath = URLDecoder.decode(path, "UTF-8");
            Log.info("jar path : " + decodedPath);
        } catch (UnsupportedEncodingException ex) {
        }
        try {
            JarUtils.copyJarResources(new JarFile(path), "res", andrRes);
        } catch (IOException ex) {
        }

        for (File plugin : plugins) {
            try {
                JarUtils.copyJarResources(new JarFile(plugin), "org/crossmobile/" + MATERIALS_TAG + "/sys", new File(andrRes, "drawable"));
            } catch (Exception ex) {
            }
            try {
                JarUtils.copyJarResources(new JarFile(plugin), "org/crossmobile/" + MATERIALS_TAG + "/app", andrAsset);
            } catch (Exception ex) {
            }
        }
    }
}
