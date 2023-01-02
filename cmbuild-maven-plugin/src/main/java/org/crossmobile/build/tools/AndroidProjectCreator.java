/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.build.utils.SynchronizeHelpers;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.Param;
import org.crossmobile.utils.PluginMetaData;
import org.crossmobile.utils.TextUtils;

import java.io.File;
import java.util.*;

import static org.crossmobile.bridge.ann.CMLibParam.ParamContext.Android;
import static org.crossmobile.build.utils.Config.*;
import static org.crossmobile.utils.ParamsCommon.*;

public class AndroidProjectCreator {

    public static void execute(CMBuildEnvironment env) {
        boolean debuggable = false;

        // Init variables
        String displayname = env.getProperties().getProperty(DISPLAY_NAME.tag().name);
        String bundleID = env.getProperties().getProperty(GROUP_ID.tag().name) + "." + env.getProperties().getProperty(ARTIFACT_ID.tag().name);
        String version = env.getProperties().getProperty(BUNDLE_VERSION.tag().name);
        File generated = new File(env.getBuilddir(), ANDROID_GENERATED_CMSOURCES);//here lies gen.absolute.dir
        File manifestfile = new File(env.getBuilddir(), ANDROID_MANIFEST);
        Iterable<PluginMetaData> metaData = env.root().getPluginMetaData();
        int numversion = calculateNumVersion(version);

        Log.info("Updating Android project with bundle ID " + bundleID);
        SynchronizeHelpers.createActivityAndApplication(bundleID, generated);
        SynchronizeHelpers.createAndroidManifest(manifestfile, bundleID, displayname, version, numversion,
                getApplicationExtras(env, metaData), getPermissions(env, metaData),
                !env.isRelease());
        SynchronizeHelpers.createBasePathAndroid(new File(env.getBuilddir(), ANDROID_BASEPATH), bundleID);
    }

    private static int calculateNumVersion(String version) {
        String data = (version + ".0.0.0.0").replaceAll("\\.+", ".");
        int numversion = 0;
        if (data.startsWith("."))
            data = data.substring(1);
        String[] partS = data.split("\\.", 4);
        if (partS[3].contains("."))
            partS[3] = partS[3].substring(0, partS[3].indexOf("."));
        for (int i = 0; i < 4; i++)
            try {
                int part = Integer.parseInt(partS[i]);
                if (part > 255 || (part > 127 && i == (partS.length - 1)))
                    throw new RuntimeException("Unable to parse text version at location " + i + " (" + partS[i] + "). Number out of scope (00-FF).");
                numversion |= part << ((3 - i) * 8);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Unable to parse text version at location " + i + " (" + partS[i] + ") as a number. Use numeric version instead.");
            }
        Log.info("Calculated numeric version is " + numversion);
        return numversion;
    }

    private static String getApplicationExtras(CMBuildEnvironment env, Iterable<PluginMetaData> metaData) {
        StringBuilder extras = new StringBuilder();
        Map<String, String> params = new HashMap<>();   // All parameters are post-processed with current parameters
        for (Param p : env.getParamset().runtime())
            if (p.context == Android && !p.meta.isEmpty()) {
                String value = env.getProperties().getProperty(p.name, "");
                params.put("${" + p.name + "}", value);
                extras.append("        <meta-data android:name=\"").
                        append(p.meta).
                        append("\" android:value=\"").
                        append(value).
                        append("\"/>\n");
            }
        for (PluginMetaData info : metaData)
            extras.append(info.getAndroidInjections().getAppSection());
        params.forEach((key, value) -> TextUtils.replaceMultiple(extras, key, value));
        return extras.toString();
    }

    private static String getPermissions(CMBuildEnvironment env, Iterable<PluginMetaData> infos) {
        Collection<String> permissions = new TreeSet<>();
        StringTokenizer tk = new StringTokenizer(env.getProperties().getProperty(USES_PERMISSIONS.tag().name), ":");
        while (tk.hasMoreTokens()) {
            String value = tk.nextToken().trim();
            if (!value.isEmpty()) {
                permissions.add(value);
                if (value.endsWith(".BLUETOOTH"))
                    permissions.add("android.permission.BLUETOOTH_ADMIN");
            }
        }
        for (PluginMetaData info : infos)
            permissions.addAll(info.getPermissions());

        StringBuilder output = new StringBuilder();
        for (String perm : permissions)
            output.append("    <uses-permission android:name=\"").append(perm).append("\"/>\n");

        String foundPermissions = output.toString();
        if (!foundPermissions.contains("WRITE_EXTERNAL_STORAGE"))
            foundPermissions += "    <uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\" android:maxSdkVersion=\"18\"/>\n";

        return foundPermissions;
    }
}
