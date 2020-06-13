/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package org.crossmobile.build.exec.android;

import org.crossmobile.utils.Commander;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectedAndroidDispatcher {

    private final String adb;
    private AListener listener;
    private List<AndroidDevice> previously = null;
    private ScheduledExecutorService executor;

    public ConnectedAndroidDispatcher(String adb) {
        if (adb == null)
            throw new NullPointerException("ADB should not be null");
        this.adb = adb;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(() -> {
            List<AndroidDevice> currently = new ArrayList<>();
            for (String id : getAndroidIDs()) {
                AndroidDevice dev = getAndroidDevice(id);
                if (dev != null)
                    currently.add(dev);
            }
            Collections.sort(currently);
            if (listener != null && ((previously == null ? -1 : previously.size()) != currently.size() || !previously.containsAll(currently)))
                listener.onDeviceStateChange(currently);
            previously = currently;
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    public void setListener(AListener listener) {
        this.listener = listener;
        if (previously != null)
            this.listener.onDeviceStateChange(previously);
    }

    private List<String> getAndroidIDs() {
        Commander cmd = new Commander(adb, "devices");
        List<String> ids = new ArrayList<>();
        cmd.setOutListener(data -> {
            data = data.trim();
            if (!data.startsWith("*") && !data.toLowerCase().startsWith("list of")) {
                String[] dev = data.split("\\s");
                if (dev.length > 1)
                    ids.add(dev[0].trim());
            }
        });
        cmd.exec();
        cmd.waitFor();
        return ids;
    }

    private AndroidDevice getAndroidDevice(String id) {
        Commander cmd = new Commander(adb, "-s", id, "shell", "getprop");
        AndroidDevice dev = new AndroidDevice(id);
        cmd.setOutListener(dev::addPropertyLine);
        cmd.exec();
        cmd.waitFor();
        return dev.getName() != null ? dev : null;
    }

    public interface AListener {

        void onDeviceStateChange(List<AndroidDevice> devices);
    }
}
