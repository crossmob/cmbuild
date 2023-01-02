/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build;

import com.panayotis.appenh.EnhancerManager;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.utils.Log;
import org.crossmobile.utils.UIUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static org.crossmobile.utils.ParamsCommon.CM_SCALE_FACTOR;
import static org.crossmobile.utils.ParamsCommon.MAIN_CLASS;

@Mojo(name = "execswing", defaultPhase = LifecyclePhase.INSTALL)
public class ExecSwingMojo extends ExecGenericMojo {

    private static final int DaemonTimeout = 3000;

    @Override
    public void exec() {
        if (!isRunnable())
            return;

        // Load local properties
        Properties localProperties = new Properties();
        File localFile = new File(getProject().getBasedir(), "local.properties");
        if (localFile.isFile()) {
            try {
                localProperties.load(new FileReader(localFile));
            } catch (IOException ignored) {
            }
        }

        File baseJar = new File(getProject().getProperties().getProperty("cm.launch.swing"));
        if (!baseJar.isFile())
            BaseUtils.throwException(new IOException("Unable to locate JAR file " + baseJar.getAbsolutePath()));

        String mainClass = getProject().getProperties().getProperty(MAIN_CLASS.tag().name);
        if (mainClass == null)
            throw new NullPointerException("Unable to find main class, using property 'main.class'");

        String scale = String.valueOf(UIUtils.getScaleBasedOnDPI(EnhancerManager.getDefault().getDPI()));
        System.getProperties().setProperty("user.arg.scale", localProperties.getProperty(CM_SCALE_FACTOR.tag().name, scale));

        DesktopThreadGroup threadGroup = new DesktopThreadGroup();
        DesktopThread baseThread = new DesktopThread(threadGroup, mainClass);
        baseThread.setFileClassLoader(baseJar);
        baseThread.start();
        threadGroup.waitFor();
        baseThread.checkForErrors();
        threadGroup.kill();
    }

    private static final class DesktopThreadGroup extends ThreadGroup {
        private DesktopThreadGroup() {
            super("cm.thread.group");
        }

        public void waitFor() {
            boolean found;
            do {
                found = false;
                for (Thread thread : getActiveThreads()) {
                    found = true;
                    join(thread);
                }
            }
            while (found);
        }

        private Collection<Thread> getActiveThreads() {
            return getThreadsImpl(false);
        }

        private Collection<Thread> getAllThreads() {
            return getThreadsImpl(true);
        }

        private Collection<Thread> getThreadsImpl(boolean alsoDaemons) {
            Thread[] threads = new Thread[activeCount()];
            Collection<Thread> result = new ArrayList<>(enumerate(threads));
            for (Thread thread : threads)
                if (thread != null && (alsoDaemons || !thread.isDaemon()))
                    result.add(thread);
            return result;
        }

        private void join(Thread thread) {
            join(thread, 0);
        }

        private void join(Thread thread, long baseTime) {
            try {
                if (thread.isDaemon()) {
                    long timeout = (baseTime + DaemonTimeout) - System.currentTimeMillis();
                    if (timeout > 0)
                        thread.join(timeout);
                } else
                    thread.join(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void kill() {
            long baseTime = System.currentTimeMillis();
            for (Thread thread : getAllThreads())
                thread.interrupt();
            for (Thread thread : getAllThreads())
                join(thread, baseTime);
            for (Thread thread : getAllThreads())
                if (thread.isAlive())
                    Log.error("Thread still alive: " + thread.getName());
            destroy();
        }
    }

    private static final class DesktopThread extends Thread {
        private final String mainClass;
        private Throwable error = null;

        public DesktopThread(DesktopThreadGroup threadGroup, String mainClass) {
            super(threadGroup, "DesktopLaunch");
            this.mainClass = mainClass;
        }

        @Override
        public void run() {
            try {
                Class<?> bootClass = Thread.currentThread().getContextClassLoader().loadClass(mainClass);
                bootClass.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
            } catch (Throwable throwable) {
                Log.error(throwable);
                error = throwable instanceof InvocationTargetException ? throwable.getCause() : throwable;
            }
        }

        public void setFileClassLoader(File file) {
            URL fileUrl = null;
            try {
                fileUrl = file.toURI().toURL();
            } catch (MalformedURLException e) {
                BaseUtils.throwException(e);
            }
            setContextClassLoader(new java.net.URLClassLoader(new URL[]{fileUrl}));
        }

        public void checkForErrors() {
            if (error != null)
                BaseUtils.throwException(error);
        }
    }
}
