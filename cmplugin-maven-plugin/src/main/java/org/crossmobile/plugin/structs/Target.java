/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.structs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Target {
    public enum ValidTarget {
        LINUX_X86_64("linux", "x86_64", "gnu"),
        LINUX_ARM("linux", "arm", "armhf", "arm", "gnueabihf"),
        LINUX_ARM64("linux", "arm64", "arm64", "aarch64", "gnu");

        private final String os;
        private final String arch;
        private final String dockerArch;
        private final String tripleArch;
        private final String osType;

        ValidTarget(String os, String arch, String osType) {
            this(os, arch, arch, arch, osType);
        }

        ValidTarget(String os, String arch, String dockerArch, String tripleArch, String osType) {
            this.os = os;
            this.arch = arch;
            this.dockerArch = dockerArch;
            this.tripleArch = tripleArch;
            this.osType = osType;
        }

        public String getTriple() {
            return tripleArch + "-" + os + "-" + osType;
        }

        public String getDockerName() {
            return os + "-" + dockerArch;
        }

        public boolean usesCrossCompilerPrefix() {
            return !tripleArch.equals("x86_64");
        }
    }

    private String os;
    private String arch;
    private File cc;
    private List<String> includes;
    private List<String> ccflags;
    private List<String> lflags;
    private List<File> libraries;

    // Needed by maven
    public Target() {
    }

    public Target(String os, String arch) {
        this.os = os;
        this.arch = arch;
    }

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
        return new ArrayList<>(ccflags);
    }

    public List<File> getLibraries() {
        return new ArrayList<>(libraries);
    }

    public List<String> getLflags() {
        return new ArrayList<>(lflags);
    }

    public List<String> getIncludes() {
        return new ArrayList<>(includes);
    }

    public String getName() {
        return os + "-" + arch;
    }

    static final Function<String, String> IOSLibName = l -> "lib" + l + ".a";
    static final Function<String, String> UWPLibName = l -> l + ".dll";

    public String getLibName(String baseName) {
        switch (os) {
            case "uwp":
                return baseName + ".dll";
            default:
            case "ios":
                return "lib" + baseName + ".a";
        }
    }

    public ValidTarget asValidTarget() {
        Objects.requireNonNull(os, "Operating System not defined for " + toString());
        Objects.requireNonNull(arch, "Arch not defined for " + toString());
        for (ValidTarget target : ValidTarget.values())
            if (target.arch.equals(arch) && target.os.equals(os))
                return target;
        throw new RuntimeException("Illegal combination of operating system/architecture: " + toString());
    }

    @Override
    public String toString() {
        return "os='" + os + '\'' +
                ", arch='" + arch + '\'';
    }
}
