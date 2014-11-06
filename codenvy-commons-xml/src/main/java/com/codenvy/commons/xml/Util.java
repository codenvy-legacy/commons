/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.xml;

import java.nio.charset.Charset;
import java.util.List;

import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

/**
 * Utils for xml tree
 *
 * @author Eugene Voevodin
 */
public final class Util {

    public static final Charset UTF_8 = Charset.forName("utf-8");

    private static final int SPACES_IN_TAB = 4;

    /**
     * TODO: write doc
     * <pre>
     * New content schema:
     *
     * [0 - left) + content + (right, src.length)
     * </pre>
     *
     * @param src
     *         source array
     * @param left
     *         left anchor - not included to result
     * @param right
     *         right anchor - not included to result
     * @param content
     *         content which will be inserted between left and right
     * @return new content
     */
    public static byte[] insertBetween(byte[] src, int left, int right, String content) {
        final byte[] contentSrc = content.getBytes(UTF_8);
        final byte[] newSrc = new byte[left + src.length - right + contentSrc.length - 1];
        arraycopy(src, 0, newSrc, 0, left);
        arraycopy(contentSrc, 0, newSrc, left, contentSrc.length);
        arraycopy(src, right + 1, newSrc, left + contentSrc.length, src.length - right - 1);
        return newSrc;
    }

    /**
     * TODO write doc
     * <pre>
     * New content schema:
     *
     * [0 - pos) + content + [pos, src.length)
     * </pre>
     *
     * @param src
     *         source array
     * @param pos
     *         start position for content insertion
     * @param content
     *         content which will be inserted from {@param anchor}
     * @return new content
     */
    public static byte[] insertInto(byte[] src, int pos, String content) {
        final byte[] contentSrc = content.getBytes(UTF_8);
        final byte[] newSrc = new byte[src.length + contentSrc.length];
        arraycopy(src, 0, newSrc, 0, pos);
        arraycopy(contentSrc, 0, newSrc, pos, contentSrc.length);
        arraycopy(src, pos, newSrc, pos + contentSrc.length, src.length - pos);
        return newSrc;
    }

    /**
     * Check given list contains only element and return it.
     * If list size is not 1 {@link XMLTreeException} will be thrown.
     *
     * @param target
     *         list to check
     * @return list only element
     */
    public static <T> T single(List<T> target) {
        if (target.size() != 1) {
            throw new XMLTreeException("Required list with one element");
        }
        return target.get(0);
    }

    //TODO
    public static int nearestLeftIndexOf(byte[] src, char c, int idx) {
        while (idx > 0 && src[idx] != c) {
            idx--;
        }
        return idx;
    }

    /**
     * Fetch text from source based on given segments.
     * Uses each segment to fetch text from source array.
     * It should be used with Element#getText().
     *
     * @param src
     *         source array
     * @param segments
     *         text bounds
     * @return fetched text
     */
    public static String fetchText(byte[] src, List<Segment> segments) {
        final byte[] text = new byte[capacity(segments)];
        int copied = 0;
        for (Segment segment : segments) {
            int len = segment.right - segment.left + 1;
            arraycopy(src, segment.left, text, copied, len);
            copied += len;
        }
        //TODO should it be interned?
        return new String(text);
    }

    /**
     * Calculates how deep is the element in tree.
     * First level is tree root and it is equal to 0.
     *
     * @param element
     *         target element
     * @return how deep is the element
     */
    public static int getLevel(Element element) {
        int level = 0;
        while (element.hasParent()) {
            element = element.parent;
            level++;
        }
        return level;
    }

    /**
     * Inserts given number of tabs to each line of given source.
     *
     * @param src
     *         source which going to be tabulated
     * @param tabsCount
     *         how many tabs should be added before each line
     * @return tabulated source
     */
    public static String tabulate(String src, int tabsCount) {
        char[] tabs = new char[SPACES_IN_TAB * tabsCount];
        fill(tabs, ' ');
        final StringBuilder builder = new StringBuilder();
        final String[] lines = src.split("\n");
        for (int i = 0; i < lines.length - 1; i++) {
            builder.append(tabs)
                   .append(lines[i])
                   .append('\n');
        }
        builder.append(tabs)
               .append(lines[lines.length - 1]);
        return builder.toString();
    }

    private static int capacity(List<Segment> segments) {
        int capacity = 0;
        for (Segment segment : segments) {
            capacity += segment.right - segment.left + 1;
        }
        return capacity;
    }

    private Util() {}
}
