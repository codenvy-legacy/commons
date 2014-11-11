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


import static com.codenvy.commons.xml.Util.closeTagLength;
import static com.codenvy.commons.xml.Util.indexOf;
import static com.codenvy.commons.xml.Util.single;
import static com.codenvy.commons.xml.Util.insertBetween;
import static com.codenvy.commons.xml.Util.insertInto;
import static com.codenvy.commons.xml.Util.lastIndexOf;
import static com.codenvy.commons.xml.Util.openTagLength;
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
        assertEquals(single(asList("first")), "first");
    }

    @Test(expectedExceptions = XMLTreeException.class)
    public void shouldThrowExceptionWhenListContainsNotOnlyElement() {
        single(asList("first", "second"));
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
    public void shouldBeAbleToFindLastIndexOf() {
        //                             11        20      28
        final byte[] src = "...</before>\n       <current>...".getBytes();

        assertEquals(lastIndexOf(src, '>', 20), 11);
        assertEquals(lastIndexOf(src, '>', src.length - 1), 28);
    }

    //TODO add attributes
    @Test
    public void shouldBeAbleToGetElementOpenTagLength() {
        final XMLTree tree = XMLTree.from("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project></project>");

        //<test>test</test>
        final Element newElement = tree.newElement("test", "test");

        assertEquals(openTagLength(newElement), 6);
    }

    @Test
    public void shouldBeAbleToGetElementCloseTagLength() {
        final XMLTree tree = XMLTree.from("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project></project>");

        //<test>test</test>
        final Element newElement = tree.newElement("test", "test");

        assertEquals(closeTagLength(newElement), 7);
    }

    @Test
    public void shouldBeAbleToGetIndexOf() {
        final String src = "<element attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">text</element>";
        final byte[] byteSrc = "<element attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">text</element>".getBytes();

        assertEquals(indexOf(byteSrc, "attribute1".getBytes(), 0), src.indexOf("attribute1"));
        assertEquals(indexOf(byteSrc, "attribute2".getBytes(), 0), src.indexOf("attribute2"));
        assertEquals(indexOf(byteSrc, "attribute3".getBytes(), 0), src.indexOf("attribute3"));
    }
}
