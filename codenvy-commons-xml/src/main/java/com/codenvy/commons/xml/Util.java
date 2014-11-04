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

import java.util.List;

import static java.util.Arrays.fill;

/**
 * Utils for xml tree
 *
 * @author Eugene Voevodin
 */
public final class Util {

    private static final int SPACES_IN_TAB = 4;

    /**
     * Creates new char array from given char array
     * source and string content
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
    public static char[] insert(char[] src, int left, int right, String content) {
        final String newSrc = String.valueOf(src, 0, left)
                              + content
                              + String.valueOf(src, right + 1, src.length - right - 1);
        return newSrc.toCharArray();
    }

    /**
     * Creates new char array from given char array source
     * and string content
     * <pre>
     * New content schema:
     *
     * [0 - anchor) + content + [anchor, src.length)
     * </pre>
     *
     * @param src
     *         source array
     * @param anchor
     *         start position for content insertion
     * @param content
     *         content which will be inserted from {@param anchor}
     * @return new content
     */
    public static char[] insert(char[] src, int anchor, String content) {
        final String newSrc = String.valueOf(src, 0, anchor)
                              + content
                              + String.valueOf(src, anchor, src.length - anchor);
        return newSrc.toCharArray();
    }

    /**
     * Check given list contains only element and return it.
     * If list size is not 1 exception will be thrown.
     *
     * @param target
     *         list to check
     * @return list only element
     */
    public static <T> T getOnly(List<T> target) {
        if (target.size() != 1) {
            throw new XMLTreeException("Required list with one element");
        }
        return target.get(0);
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
    public static String fetchText(char[] src, List<Segment> segments) {
        final StringBuilder sb = new StringBuilder();
        for (Segment segment : segments) {
            sb.append(src, segment.left, 1 + segment.right - segment.left);
        }
        return sb.toString();
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

    private Util() {}
}
