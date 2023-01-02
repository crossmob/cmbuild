/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.exec.android;

import org.crossmobile.utils.Commander;
import org.crossmobile.utils.Log;
import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;

import java.io.*;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

import static java.io.File.separator;
import static java.lang.String.format;
import static org.crossmobile.bridge.system.BaseUtils.listFiles;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GradleLauncher {
    private static final String GRADLE_VERSION = "6.5.1";
    @SuppressWarnings("FieldCanBeLocal")
    private static final long TOTAL_SIZE = 102_204_844;

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static boolean runGradle(File currentDir, boolean release, boolean asApk) {
        if (!isInstalled())
            downloadGradle(getGradleHome());
        Commander gradle = new Commander(getGradleBin(),
                asApk ? (release ? "assembleRelease" : "assembleDebug")
                        : (release ? "bundleRelease" : "bundleDebug")
        );
        gradle.setCurrentDir(currentDir);
        gradle.setOutListener(Log::info);
        gradle.setErrListener((Consumer<String>) Log::error);
        Log.info("Executing: " + gradle);
        gradle.exec();
        gradle.waitFor();
        return gradle.exitValue() == 0;
    }

    private static boolean isInstalled() {
        return new File(getGradleHome(), "bin" + separator + "gradle").exists();
    }

    private static File getGradleHome() {
        return new File(new File(System.getProperty("user.home")), format(".cache%scrossmobile%sgradle%s%s", separator, separator, separator, GRADLE_VERSION));
    }

    private static String getGradleBin() {
        return new File(getGradleHome(), format("bin%sgradle%s", separator, IS_WINDOWS ? ".bat" : "")).getAbsolutePath();
    }

    private static String getGradleURL() {
        return "https://github.com/crossmob/cmbuild/releases/download/gradle-dependencies/gradle-" + GRADLE_VERSION + ".tar.gz";
    }

    private static void downloadGradle(File destinationFolder) {
        Log.info("Installing gradle to " + destinationFolder.getPath() + " from " + getGradleURL());
        Timer downloader = null;
        delete(destinationFolder);
        destinationFolder.mkdirs();
        byte[] buffer = new byte[16384];
        CountInputStream cis;
        try (TarInputStream tis = new TarInputStream(new GZIPInputStream(cis = new CountInputStream(new URL(getGradleURL()).openStream())))) {
            downloader = new Timer("Downloader");
            downloader.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Log.info(cis.percentage() + "% downloaded");
                }
            }, 0, 1000);
            cis.setTotal(TOTAL_SIZE);
            TarEntry tarEntry = tis.getNextEntry();
            while (tarEntry != null) {
                File newFile = new File(destinationFolder, tarEntry.getName());
                if (tarEntry.isDirectory())
                    newFile.mkdirs();
                else
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        tarEntry.getGroupName();
                        int len;
                        while ((len = tis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                    }
                setPermissions(newFile, tarEntry.getHeader().mode);
                tarEntry = tis.getNextEntry();
            }
            if (cis.countBytes != cis.total)
                error(String.format("Please try to redownload file. Size mismatch, expected %d, found %d bytes", cis.total, cis.countBytes), destinationFolder);
            Log.info("Finished downloading " + cis.countBytes + " bytes.");
        } catch (IOException e) {
            error(e.toString(), destinationFolder);
        } finally {
            if (downloader != null)
                downloader.cancel();
        }
    }

    private static void error(String message, File destinationFolder) {
        delete(destinationFolder);
        Log.error("Unable to download gradle " + GRADLE_VERSION + ": " + message);
        System.exit(1);
    }

    public static void delete(File file) {
        for (File child : listFiles(file))
            delete(child);
        file.delete();
    }

    private static void setPermissions(File file, int mask) {
        file.setExecutable((mask & 0b1) != 0, false);
        file.setExecutable((mask & 0b1_000_000) != 0, true);

        file.setWritable((mask & 0b10) != 0, false);
        file.setWritable((mask & 0b10_000_000) != 0, true);

        file.setReadable((mask & 0b100) != 0, false);
        file.setReadable((mask & 0b100_000_000) != 0, true);
    }

    public static class CountInputStream extends FilterInputStream {
        private long countBytes;
        private long total = 0;

        public CountInputStream(InputStream proxy) {
            super(proxy);
        }

        public int read() throws IOException {
            int read = super.read();
            if (read >= 0)
                countBytes++;
            return read;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public double percentage() {
            return total <= 0 ? 1 : ((int) (countBytes * 1000 / total)) / 10.0;
        }

        public int read(byte[] bts, int st, int end) throws IOException {
            int hm = super.read(bts, st, end);
            countBytes += hm;
            return hm;
        }
    }
}
