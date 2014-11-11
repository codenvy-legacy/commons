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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isWhitespace;
import static java.lang.Math.min;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * Utils for xml tree
 *
 * @author Eugene Voevodin
 */
public final class Util {

    public static final Charset UTF_8         = Charset.forName("utf-8");
    public static final int     SPACES_IN_TAB = 4;

    /**
     * TODO: write doc
     * <pre>
     * New content schema:
     *
     * [0 - left) + content + (right, src.elementLength)
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
     * [0 - pos) + content + [pos, src.elementLength)
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
    public static int lastIndexOf(byte[] src, char c, int fromIdx) {
        for (int i = min(fromIdx, src.length - 1); i >= 0; i--) {
            if (src[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculates how deep is the element in tree.
     * First level is tree root and it is equal to 0.
     *
     * @param element
     *         target element
     * @return how deep is the element
     */
    public static int level(Element element) {
        int level = 0;
        while (element.hasParent()) {
            element = element.getParent();
            level++;
        }
        return level;
    }


    public static int openTagLength(Element element) {
        int len = 2; // '<' + '>'
        len += element.name.length();// 'name'
        for (Attribute attribute : element.getAttributes()) {
            len += 1 + attributeLength(attribute); // ' ' + 'attribute="value"'
        }
        return element.isVoid() ? len + 1 : len;
    }

    public static int closeTagLength(Element element) {
        return 3 + element.name.length(); // '<' + '/' + 'name' + '>'
    }

    public static int attributeLength(Attribute attribute) {
        return attribute.getName().length() + attribute.getValue().length() + 3;
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

    public static Element asElement(Node node) {
        if (node == null) {
            return null;
        }
        return (Element)node.getUserData("element");
    }

    public static List<Element> asElements(NodeList list) {
        final List<Element> elements = new ArrayList<>(list.getLength());
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == ELEMENT_NODE) {
                elements.add(asElement(list.item(i)));
            }
        }
        return elements;
    }

    public static Node nextElementSibling(Node node) {
        node = node.getNextSibling();
        while (node != null && node.getNodeType() != ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return node;
    }

    public static Node previousElementSibling(Node node) {
        node = node.getPreviousSibling();
        while (node != null && node.getNodeType() != ELEMENT_NODE) {
            node = node.getPreviousSibling();
        }
        return node;
    }

    public static int indexOf(byte[] src, byte[] target, int fromIdx) {
        for (int i = fromIdx; i < src.length; i++) {
            if (src[i] == target[0]) {
                boolean equals = true;
                for (int j = 1, k = i + 1; j < target.length && equals; j++, k++) {
                    if (src[k] != target[j]) {
                        equals = false;
                    }
                }
                if (equals && isValueEnd(src[i + target.length])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean isValueEnd(byte b) {
        return isWhitespace(b) || '=' == b || '"' == b;
    }

    private Util() {}
}
