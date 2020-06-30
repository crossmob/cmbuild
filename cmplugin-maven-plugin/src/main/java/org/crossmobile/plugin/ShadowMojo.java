/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.crossmobile.build.ArtifactInfo;
import org.crossmobile.build.GenericMojo;
import org.crossmobile.build.utils.MojoLogger;
import org.crossmobile.utils.Dependency;
import org.crossmobile.utils.FileUtils;
import org.crossmobile.utils.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.crossmobile.plugin.utils.Statics.*;
import static org.crossmobile.utils.Pom.SHADOW_ARTIFACT;

@Mojo(name = "shadow", defaultPhase = LifecyclePhase.INSTALL)
public class ShadowMojo extends GenericMojo {

    @Parameter(property = "artifacts")
    private String artifacts;

    private static final String[] REPOS = {"https://repo.maven.apache.org/maven2", "https://dl.google.com/dl/android/maven2", "https://jcenter.bintray.com"};

    @Override
    public void exec() {
        if (artifacts == null)
            return;
        MojoLogger.register(getLog());
        Collection<Dependency> deps = new ArrayList<>();
        for (String a : artifacts.split(";")) {
            String[] parts = a.split(":");
            if (parts.length == 3)
                deps.add(Dependency.find(parts[0], parts[1], parts[2], null, null, "jar"));
            else if (parts.length == 4)
                deps.add(Dependency.find(parts[0], parts[1], parts[3], null, null, parts[2]));
            else
                throw new IllegalArgumentException("Unable to parse artifact " + a);
        }
        for (Dependency dep : deps)
            fetch(dep);
    }

    private void fetch(Dependency dep) {
        for (String repo : REPOS)
            if (fetch(dep, repo))
                return;
        Log.error("Unable to find artifact " + dep);
        System.exit(1);
    }

    private boolean fetch(Dependency dep, String repo) {
        String base = repo + "/" + dep.groupId.replace('.', '/') + "/" + dep.artifactId + "/" + dep.version + "/" + dep.artifactId + "-" + dep.version;
        File pom = createPom(dep);
        if (pom == null)
            return false;
        File art = fetchFile(base + "." + dep.packaging);
        if (art == null)
            return false;
        installArtifact(new ArtifactInfo(art, SHADOW_ARTIFACT + "." + dep.groupId, dep.artifactId, dep.version, "jar", null, pom));
        return true;
    }

    private File createPom(Dependency dep) {
        File pom;
        try {
            pom = File.createTempFile("shadow_", ".pom");
        } catch (IOException e) {
            return null;
        }
        String pomData = POM_XML.
                replace(POM_GROUPID, dep.groupId).
                replace(POM_ARTIFACTID, dep.artifactId).
                replace(POM_VERSION, dep.version).
                replace(POM_PACKAGING, dep.packaging).
                replace(POM_DEPENDENCIES, "");
        FileUtils.write(pom, pomData);
        return pom;
    }

    private File fetchFile(String url) {
        File jar;
        try {
            jar = File.createTempFile("shadow_", ".jar");
            jar.deleteOnExit();
        } catch (IOException e) {
            return null;
        }
        try (ZipInputStream in = new ZipInputStream(new URL(url).openStream()); BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(jar))) {
            Log.info("Fetching " + url);
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory() && entry.getName().equals("classes.jar")) {
                    byte[] bytesIn = new byte[10000];
                    int read;
                    while ((read = in.read(bytesIn)) >= 0)
                        out.write(bytesIn, 0, read);
                    return jar;
                }
                in.closeEntry();
                entry = in.getNextEntry();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
