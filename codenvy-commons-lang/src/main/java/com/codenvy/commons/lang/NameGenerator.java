/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.commons.lang;

import java.util.Random;

public class NameGenerator {

    private static final Random RANDOM = new Random();

    private static final char[] CHARS = new char[36];

    static {
        int i = 0;
        for (int c = 48; c <= 57; c++) {
            CHARS[i++] = (char)c;
        }
        for (int c = 97; c <= 122; c++) {
            CHARS[i++] = (char)c;
        }
    }

    public static String generate(String prefix, int length) {
        StringBuilder b;
        if (prefix == null || prefix.isEmpty()) {
            b = new StringBuilder(length);
        } else {
            b = new StringBuilder(length + prefix.length());
            b.append(prefix);
        }
        for (int i = 0; i < length; i++) {
            b.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }
        return b.toString();
    }

    private NameGenerator() {
    }
}
