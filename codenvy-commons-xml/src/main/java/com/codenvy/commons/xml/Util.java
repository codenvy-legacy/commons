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

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.arraycopy;

/**
 * TODO: cover with tests and docs
 *
 * @author Eugene Voevodin
 */
final class Util {

    static <T> T argumentRequired(T obj, String argumentName) {
        if (obj == null) {
            throw new IllegalArgumentException(argumentName + " argument required");
        }
        return obj;
    }

    static XMLTree.Node ensureOnly(List<XMLTree.Node> target) {
        if (target.size() != 1) {
            throw new XMLTreeException("Found more then only element");
        }
        return target.get(0);
    }

    static List<Element> asElements(List<XMLTree.Node> nodes) {
        final ArrayList<Element> elements = new ArrayList<>(nodes.size());
        for (XMLTree.Node node : nodes) {
            elements.add(new Element(node));
        }
        return elements;
    }

    static String fetchText(byte[] src, List<XMLTree.Segment> segments) {
        byte[] text = new byte[capacity(segments)];
        int copied = 0;
        for (XMLTree.Segment segment : segments) {
            int length = 1 + segment.end - segment.start;
            arraycopy(src, segment.start, text, copied, length);
            copied += length;
        }
        return new String(text);
    }

    static int capacity(List<XMLTree.Segment> segments) {
        int capacity = 0;
        for (XMLTree.Segment segment : segments) {
            capacity += 1 + segment.end - segment.start;
        }
        return capacity;
    }

    private Util() {
    }
}
