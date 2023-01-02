/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import org.codehaus.plexus.util.FileUtils;

import java.io.File;

import static org.crossmobile.utils.TextUtils.requireValue;

public class ArtifactInfo {

    public final File source;
    public final String groupId;
    public final String artifactId;
    public final String version;
    public final File pomFile;
    public final String packaging;
    public final String classifier;

    public ArtifactInfo(String groupid, String artifactid, String version, String packaging) {
        this(null, groupid, artifactid, version, packaging, "", null);
    }

    public ArtifactInfo(String groupid, String artifactid, String version, String packaging, String classifier) {
        this(null, groupid, artifactid, version, packaging, classifier, null);
    }

    public ArtifactInfo(File fileToInstall, String groupid, String artifactid, String version, String classifier, File pomfile) {
        this(fileToInstall, groupid, artifactid, version, FileUtils.extension(fileToInstall.getName()), classifier, pomfile);
    }

    public ArtifactInfo(File fileToInstall, String groupid, String artifactid, String version, String packaging, String classifier, File pomfile) {
        requireValue(groupid, "Group ID could not be null");
        requireValue(artifactid, "Artifact ID could not be null");
        requireValue(version, "Version could not be null");
        requireValue(packaging, "Packaging could not be null");
        this.source = fileToInstall;
        this.groupId = groupid;
        this.artifactId = artifactid;
        this.version = version;
        this.packaging = packaging;
        this.classifier = classifier == null ? "" : classifier;
        this.pomFile = pomfile;
    }

    @Override
    public String toString() {
        return "ArtifactInfo{groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version + ", packaging=" + packaging + ", classifier=" + classifier + '}';
    }
}
