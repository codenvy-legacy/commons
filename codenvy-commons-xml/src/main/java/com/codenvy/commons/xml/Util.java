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

import static java.lang.System.arraycopy;

/**
 * TODO: cover with tests and docs
 *
 * @author Eugene Voevodin
 */
final class Util {

    static char[] insert(char[] src, int left, int right, String content) {
        char[] target = new char[src.length + content.length() + right - left];
        arraycopy(src, 0, target, 0, left);
        arraycopy(content.toCharArray(), 0, target, left, content.length());
        arraycopy(src, right + 1, target, left + content.length(), src.length - right - 1);
        return target;
    }

    static <T> T getOnly(List<T> target) {
        if (target.size() != 1) {
            throw new XMLTreeException("Required list with one element");
        }
        return target.get(0);
    }

    static String fetchText(char[] src, List<Segment> segments) {
        final StringBuilder sb = new StringBuilder();
        for (Segment segment : segments) {
            sb.append(src, segment.left, 1 + segment.right - segment.left);
        }
        return sb.toString();
    }

    private Util() {
    }
}
