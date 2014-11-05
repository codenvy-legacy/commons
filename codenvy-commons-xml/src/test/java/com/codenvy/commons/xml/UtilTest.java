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

import static com.codenvy.commons.xml.Util.fetchText;
import static com.codenvy.commons.xml.Util.getOnly;
import static com.codenvy.commons.xml.Util.insertBetween;
import static com.codenvy.commons.xml.Util.insertInto;
import static com.codenvy.commons.xml.Util.nearestLeftIndexOf;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * Tests for {@link Util}
 *
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
    public void shouldInsertContentBetweenTwoAnchors() {
        //                        6     12
        final byte[] src = "<name>content</name>".getBytes();

        final byte[] newSrc = insertBetween(src, 6, 12, "new content");

        assertEquals(newSrc, "<name>new content</name>".getBytes());
    }

    @Test
    public void shouldInsertContentToCharArrayInSelectedPlace() {
        //                        6
        final byte[] src = "<name></name>".getBytes();

        final byte[] newSrc = insertInto(src, 6, "new content");

        assertEquals(new String(newSrc).intern(), "<name>new content</name>");
    }

    @Test
    public void shouldFetchText() {
        //                       5    10    16  20
        final byte[] src = "12345hello 12345world".getBytes();

        final String text = fetchText(src, asList(new Segment(5, 10), new Segment(16, 20)));

        assertEquals(text, "hello world");
    }

    @Test
    public void shouldBeAbleToFindNearestLeftIndexOf() {
        //                             11        20      28
        final byte[] src = "...</before>\n       <current>...".getBytes();

        assertEquals(nearestLeftIndexOf(src, '>', 20), 11);
        assertEquals(nearestLeftIndexOf(src, '>', src.length - 1), 28);
    }
}
