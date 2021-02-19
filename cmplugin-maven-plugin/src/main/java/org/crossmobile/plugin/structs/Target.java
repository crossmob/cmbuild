/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.structs;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Target {
    private final static Collection<String> validOS = Arrays.asList("linux", "windows", "macosx", "ios", "freebsd");
    private final static Collection<String> validArch = Arrays.asList("i386", "x86_64", "arm", "arm64");

    private String os;
    private String arch;
    private File cc;
    private List<String> ccflags;
    private List<String> lflags;
    private List<File> libraries;

    public String getOs() {
        return os;
    }

    public String getArch() {
        return arch;
    }

    public File getCc() {
        return cc;
    }

    public List<String> getCcflags() {
        return ccflags;
    }

    public List<File> getLibraries() {
        return libraries;
    }

    public List<String> getLflags() {
        return lflags;
    }

    public String getName() {
        return os + "-" + arch;
    }

    public Target asserted() {
        Objects.requireNonNull(os, "Operating System not defined for " + toString());
        Objects.requireNonNull(arch, "Arch not defined for " + toString());
        if (!validOS.contains(os))
            throw new RuntimeException("Illegal Operating System for " + toString() + ", should be one of " + validOS);
        if (!validArch.contains(arch))
            throw new RuntimeException("Illegal Architecture for " + toString() + ", should be one of " + validArch);
        return this;
    }

    @Override
    public String toString() {
        return "Target{" +
                "os='" + os + '\'' +
                ", arch='" + arch + '\'' +
                '}';
    }
}
