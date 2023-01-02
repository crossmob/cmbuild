/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.actions;

import org.crossmobile.bridge.ann.CMLibTarget;
import org.crossmobile.bridge.ann.CMLibTarget.BaseTarget;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.Log;

import java.io.File;
import java.util.function.Function;

import static org.crossmobile.bridge.system.BaseUtils.listFiles;
import static org.crossmobile.utils.FileUtils.copy;

public class CreateBundles {

    private static BundleResolver getFileResolver(String extension, Registry reg) {
        return (filename, packg) -> {
            if (filename.endsWith(extension)) {
                String clsname = filename.substring(0, filename.length() - extension.length());
                int dollar = clsname.indexOf('$');
                if (dollar > 0)
                    clsname = clsname.substring(0, dollar);
                if (clsname.isEmpty())
                    return new PluginAndTarget(reg.packages().getPlugin(packg), reg.packages().getTarget(packg));
                else {
                    clsname = packageToJavaPackage(packg) + clsname;
                    return new PluginAndTarget(reg.plugins().getPlugin(clsname), reg.targets().getTarget(clsname));
                }
            } else
                return new PluginAndTarget(reg.packages().getPlugin(packg), reg.packages().getTarget(packg));
        };
    }

    public static BundleResolver getNoClassResolver(Registry reg) {
        return (filename, packg) -> new PluginAndTarget(reg.packages().getPlugin(packg),
                filename.endsWith(".class") ? CMLibTarget.SOURCEONLY : reg.packages().getTarget(packg));
    }

    public static BundleResolver classResolver(Registry reg) {
        return getFileResolver(".class", reg);
    }

    public static BundleResolver sourceResolver(Registry reg) {
        return getFileResolver(".java", reg);
    }

    public static void bundleFiles(File source, Function<String, File> filedest, BundleResolver resolver, BaseTarget filter) {
        bundleFiles(source, filedest, resolver, filter, false, "", true);
    }

    public static void bundleFilesAndReport(File source, Function<String, File> filedest, BundleResolver resolver, BaseTarget filter) {
        bundleFiles(source, filedest, resolver, filter, true, "", true);
    }

    private static void bundleFiles(File source, Function<String, File> filedest, BundleResolver resolver, BaseTarget filter, final boolean letsReportThisTime, String packg, boolean is_root) {
        if (source.isDirectory())
            for (File child : listFiles(source))
                bundleFiles(child, filedest, resolver, filter, letsReportThisTime, packageToJavaPackage(packg) + (is_root ? "" : source.getName()), false);
        else if (source.isFile()) {
            PluginAndTarget pt = resolver.resolve(source.getName(), packg);
            if (letsReportThisTime && pt.target == CMLibTarget.UNKNOWN)
                Log.warning("Unable to match bundle of file " + source.getAbsolutePath());
            else if (pt.target.matches(filter))
                copy(source, getOutput(filedest.apply(pt.plugin), packg, source.getName()));
        }
    }

    private static String packageToJavaPackage(String packg) {
        return packg + (packg.isEmpty() ? "" : ".");
    }

    private static File getOutput(File root, String pkg, String filename) {
        return new File(root, pkg.replace('.', File.separatorChar) + File.separator + filename);
    }

    public interface BundleResolver {

        PluginAndTarget resolve(String filename, String packg);
    }

    public static class PluginAndTarget {

        public final String plugin;
        public final CMLibTarget target;

        public PluginAndTarget(String plugin, CMLibTarget target) {
            this.plugin = plugin;
            this.target = target;
        }
    }
}
