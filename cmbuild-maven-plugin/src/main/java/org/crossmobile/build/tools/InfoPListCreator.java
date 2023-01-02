/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.tools;

import org.crossmobile.build.ng.CMBuildEnvironment;
import org.crossmobile.build.utils.InfoPlist;
import org.crossmobile.build.utils.Templates;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.XcodeTarget;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import static org.crossmobile.build.utils.Templates.*;
import static org.crossmobile.utils.ParamsCommon.*;

public class InfoPListCreator extends GenericPropertiesCreator {

    private final Collection<String> fontlist;
    private final String injected;

    public InfoPListCreator(Properties sysprops, File output, Collection<String> fontlist, String injected, File projectpath) {
        super(sysprops, projectpath, output);
        this.fontlist = fontlist == null ? Collections.emptyList() : fontlist;
        this.injected = injected == null ? "" : injected;
    }

    @Override
    public void execute(CMBuildEnvironment env) {
        Log.info("Writing Info.plist file for application " + prop(ARTIFACT_ID.tag().name));
        InfoPlist info = new InfoPlist(Templates.INFO_PLIST);
        info.setApplication(prop(ARTIFACT_ID.tag().name));
        info.setIdentifier(prop(GROUP_ID.tag().name) + "." + prop(ARTIFACT_ID.tag().name));
        info.setVersion(prop(BUNDLE_VERSION.tag().name));
        info.setDisplayName(prop(DISPLAY_NAME.tag().name));
        info.setFileSharing(prop(FILESHARINGENABLED.tag().name));
        info.setDefaultOrientation(prop(ORIENTATIONS_INITIAL.tag().name));
        info.setSupportedOrientations(prop(ORIENTATIONS_SUPPORTED.tag().name));
        info.setLaunchStoryboard(prop(LAUNCH_STORYBOARD.tag().name));
        info.setMainStoryboard(prop(MAIN_STORYBOARD.tag().name));
        info.setFonts(fontlist);
        info.setInjected(injected);
        write(info);
    }

    public static File getPlist(File baseDir, String targetName) {
        return new File(baseDir, targetName + "-Info.plist");
    }

    public static void createExtensionPlist(File baseDir, String bundleId, XcodeTarget target) {
        String extId = target.getMeta().isEmpty() ? "" : "\t\t<key>NSExtensionPointIdentifier</key>\n\t\t<string>" + target.getMeta() + "</string>\n";
        String priCl = target.getPrincipalClass() == null ? "" : "\t\t<key>NSExtensionPrincipalClass</key>\n\t\t<string>" + target.getPrincipalClass() + "</string>\n";
        String data = Templates.EXT_INFO_PLIST.
                replace(DISPLAYNAME_ANCHOR, target.name).
                replace(BUNDLEID_ANCHOR, bundleId + "." + target.name).
                replace(EXTENSION_ANCHOR, extId + priCl);
        FileUtils.write(getPlist(baseDir, target.name), data);
    }
}
