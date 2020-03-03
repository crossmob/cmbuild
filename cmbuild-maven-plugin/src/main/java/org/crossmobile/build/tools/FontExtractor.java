// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.utils.Log;

import java.awt.*;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.crossmobile.utils.FileUtils.Predicates.extensions;
import static org.crossmobile.utils.FileUtils.forAll;
import static org.crossmobile.utils.TextUtils.*;

public class FontExtractor {

    public static Map<String, File> findFonts(File materialPath) {
        Map<String, File> fonts = new LinkedHashMap<>();
        forAll(materialPath, extensions(".ttf", ".otf"), (path, file) -> fonts.put(path + (path.isEmpty() ? "" : "/") + file.getName(), file));
        if (!fonts.isEmpty())
            Log.info("Project contains " + fonts.size() + " font" + plural(fonts.size()));
        return fonts;
    }

    @SuppressWarnings("UseSpecificCatch")
    public static String getFontDataAsResource(Map<String, File> fonts) {
        Map<String, Map<String, FontInfo>> fontlist = new LinkedHashMap<>();
        for (String fontfile : fonts.keySet())
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fonts.get(fontfile));
                String family = font.getFamily();
                String fontname = font.getFontName();
                boolean bold = font.isBold();
                boolean italic = font.isItalic();
                Map<String, FontInfo> familygroup = fontlist.computeIfAbsent(family, k -> new LinkedHashMap<>());
                FontInfo obsolete = familygroup.get(fontname);
                FontInfo current = new FontInfo(fontfile, bold, italic);
                if (obsolete != null) {
                    // Keep font closer to root, swap if older is better
                    if (countCharacter(obsolete.location, '/') <= countCharacter(current.location, '/')) {
                        FontInfo swap = current;
                        current = obsolete;
                        obsolete = swap;
                    }
                    Log.warning("Font located at " + obsolete.location
                            + (current.bold == obsolete.bold && current.italic == obsolete.italic ? "" : " with diferent weights")
                            + " has been already registered at " + current.location + ". Ignoring.");
                }
                familygroup.put(fontname, current);
            } catch (Exception ex) {
                Log.error("Unable to parse font file " + fonts.get(fontfile), ex);
            }

        StringBuilder out = new StringBuilder();
        out.append("<fontlist>\n");
        for (String family : fontlist.keySet()) {
            Map<String, FontInfo> familygroup = fontlist.get(family);
            for (String fontname : familygroup.keySet()) {
                FontInfo info = familygroup.get(fontname);
                out.append("  <font file=\"").append(safeXML(info.location)).
                        append("\" family=\"").append(safeXML(family)).
                        append("\" name=\"").append(safeXML(fontname)).
                        append("\" bold=\"").append(info.bold ? "true" : "false").
                        append("\" italic=\"").append(info.italic ? "true" : "false").
                        append("\"/>\n");
            }
        }
        out.append("</fontlist>\n");
        return out.toString();
    }

    private static class FontInfo {

        private final String location;
        private final boolean bold;
        private final boolean italic;

        private FontInfo(String location, boolean bold, boolean italic) {
            this.location = location;
            this.bold = bold;
            this.italic = italic;
        }
    }
}
