// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: AGPL-3.0-only

package org.crossmobile.build.tools;

import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.build.ib.Element;
import org.crossmobile.build.ib.helper.XIBList;
import org.crossmobile.build.ib.helper.XibClassStart;
import org.crossmobile.build.ib.i18n.IBParserMeta;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.XMLWalker;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.crossmobile.build.AnnotationConfig.GENERATED_EXT;
import static org.crossmobile.utils.FileUtils.Predicates.extensions;
import static org.crossmobile.utils.FileUtils.Predicates.noHidden;
import static org.crossmobile.utils.FileUtils.forAllFiles;
import static org.crossmobile.utils.FileUtils.sync;
import static org.crossmobile.utils.TextUtils.plural;

public class IBObjectsCreator {

    public static XIBList parse(File materials, File ann) {
        ann.mkdirs();
        forAllFiles(ann, f -> f.getName().equals(GENERATED_EXT), (path, file) -> file.delete());
        XIBList root = new XIBList(new IBParserMeta(ann));
        forAllFiles(materials, noHidden().and(extensions(".xib", ".storyboard")), (path, file) -> {
            root.getMeta().beginFile(file, materials);
            XMLWalker walker = XMLWalker.load(file).node("document");
            String type = walker.attribute("type").toLowerCase();
            if (type.endsWith(".storyboard.xib")) {
                XibClassStart classStart = ((XibClassStart) root.addChild("xibclassstart", null, root.getMeta()));

                classStart.setFilename(toFilename(materials, file));
                classStart.setInitialViewController(walker.attribute("initialViewController"));
                walker.node("scenes").nodes(node -> construct(root, node));

                root.addChild("xibclassend", null, root.getMeta());
            } else if (type.endsWith(".cocoatouch.xib"))
                walker.nodes(node -> construct(root, node));
            root.getMeta().endFile();
        });
        return root;
    }

    private static void construct(Element parent, XMLWalker walker) {
        walker.nodes(node -> {
            Element child = parent.addChild(node.name(), node.attribute("key"), parent.getMeta());
            if (child != null) {
                node.attributes(child::setAttribute);
                child.performChecks();
                child.applyLocalizations(parent);
                construct(child, node);
            }
        });
    }

    public static void createJavaSource(XIBList xiblist, File destination, File cache) {
        destination.getParentFile().mkdirs();
        int size = xiblist.countItems();
        if (size > 0) {
            cache.getParentFile().mkdirs();
            try (Writer out = new OutputStreamWriter(new FileOutputStream(cache), StandardCharsets.UTF_8)) {
                out.append(xiblist.toCode());
                Log.info("Added " + size + " Interface Builder resource class" + plural(size, "es"));
            } catch (IOException ex) {
                BaseUtils.throwException(ex);
            }
        }
        if (cache.exists())
            sync(cache, destination, null, false, null);
    }

    private static String toFilename(File baseDir, File currentFile) {
        return FileUtils.getRelative(baseDir, currentFile)
                .replace("\\", "_")
                .replace("/", "_")
                .replace(".", "_")
                .replace(" ", "_");
    }
}
