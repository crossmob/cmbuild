/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.crossmobile.utils.TextUtils.TAB;
import static org.crossmobile.utils.TextUtils.endsWith;

public interface Streamer {

    Streamer append(CharSequence text) throws IOException;

    default Streamer append(char c) throws IOException {
        return append(Character.toString(c));
    }

    default Streamer append(int i) throws IOException {
        return append(Integer.toString(i));
    }

    Streamer tab();

    Streamer untab();

    static Streamer asHeader(File root, String name) {
        return new FileStreamer(new File(root, name + ".h"));
    }

    static Streamer asBody(File root, String name) {
        return new FileStreamer(new File(root, name + ".m"));
    }

    static Streamer asString() {
        return new StringStreamer(new StringBuilder());
    }

    boolean isEmpty();

    void close();
}

abstract class BaseStreamer implements Streamer {

    private boolean start = true;
    private int tabsize = 0;
    private String tab = "";

    @Override
    public Streamer tab() {
        tabsize++;
        tab = getTab();
        return this;
    }

    @Override
    public Streamer untab() {
        if (tabsize > 0) {
            tabsize--;
            tab = getTab();
        }
        return this;
    }

    CharSequence conv(CharSequence input) {
        StringBuilder out = new StringBuilder();
        if (input != null && input.length() > 0) {
            if (start)
                out.append(tab);
            start = endsWith(input, "\n");
            input = input.toString().replace("\n", "\n" + tab);
            if (start) // previously ended with \n
                input = input.subSequence(0, input.length() - tab.length() - 1) + "\n";
            out.append(input);
        }
        return out;
    }

    private String getTab() {
        if (tabsize == 0)
            return "";
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < tabsize; i++)
            t.append(TAB);
        return t.toString();
    }
}

class FileStreamer extends BaseStreamer {

    private final File out;
    private Writer writer;
    private boolean empty = true;

    FileStreamer(File out) {
        this.out = out;
    }

    @Override
    public Streamer append(CharSequence text) throws IOException {
        if (writer == null) {
            out.getParentFile().mkdirs();
            writer = new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8);
        }
        writer.append(conv(text)).flush();
        if (empty && text.length() > 0)
            empty = false;
        return this;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
        }
    }
}

class StringStreamer extends BaseStreamer {

    private final StringBuilder builder;

    StringStreamer(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Streamer append(CharSequence text) throws IOException {
        builder.append(conv(text));
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public boolean isEmpty() {
        return builder.length() == 0;
    }

    @Override
    public void close() {
    }
}
