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

import org.testng.annotations.Test;

import static com.codenvy.commons.xml.Util.getOnly;
import static com.codenvy.commons.xml.Util.insert;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * @author Eugene Voevodin
 */
public class UtilTest {

    @Test
    public void shouldTabulateOneLineString() {
        final String src = "text here";

        assertEquals(tabulate(src, 2), "        " + src);
    }

    @Test
    public void shouldTabulateMultilineString() {
        final String src = "first line\nsecond line";

        assertEquals(tabulate(src, 1), "    first line\n    second line");
    }

    @Test
    public void shouldReturnFirstElement() {
        assertEquals(getOnly(asList("first")), "first");
    }

    @Test(expectedExceptions = XMLTreeException.class)
    public void shouldThrowExceptionWhenListContainsNotOnlyElement() {
        getOnly(asList("first", "second"));
    }

    @Test
    public void shouldInsertContentToCharArrayBetweenTwoAnchors() {
        //                        6     12
        final String src = "<name>content</name>";

        final char[] newSrc = insert(src.toCharArray(), 6, 12, "new content");

        assertEquals(new String(newSrc), "<name>new content</name>");
    }

    @Test
    public void shouldInsertContentToCharArrayInSelectedPlace() {
        final String src = "<name></name>";

        final char[] newSrc = Util.insert(src.toCharArray(), 6, "new content");

        assertEquals(new String(newSrc), "<name>new content</name>");
    }
}
