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

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author Eugene Voevodin
 */
public class XMLTreeTest {

    private static final byte[] XML_CONTENT = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                               "<bookshelf>\n" +
                                               "    <oneline attribute=\"1\"/>\n" +
                                               "    <country>Ukraine</country>\n" +
                                               "    <city>Cherkassy</city>\n" +
                                               "    <company>Codenvy</company>\n" +
                                               "    <!-- price - dollars -->\n" +
                                               "    <books>\n" +
                                               "        <book id=\"b1\">\n" +
                                               "            <author>Bruce Eckel</author>\n" +
                                               "            <title>Thinking in java</title>\n" +
                                               "            <price>42</price>\n" +
                                               "        </book>\n" +
                                               "        <book id=\"b2\">\n" +
                                               "            <author>Benjamin J Evans</author>\n" +
                                               "            <title>The Well-Grounded Java Developer</title>\n" +
                                               "            <price>32</price>\n" +
                                               "        </book>\n" +
                                               "        <book id=\"b3\">\n" +
                                               "            <author>John Resig</author>\n" +
                                               "            <title>Secrets of the JavaScript Ninja</title>\n" +
                                               "            <price>25</price>\n" +
                                               "        </book>\n" +
                                               "    </books>\n" +
                                               "</bookshelf>").getBytes();

    @Test
    public void shouldFindTextUsingElementPath() throws IOException {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String company = tree.searchText("/bookshelf/company");

        assertEquals(company, "Codenvy");
    }

    @Test
    public void shouldFindTextUsingXPath() throws IOException {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String title = tree.xpath().searchText("/bookshelf/books/book[author='Bruce Eckel']/title");

        assertEquals(title, "Thinking in java");
    }

    //todo add more assertions
    @Test
    public void shouldFindElementsUsingElementPath() throws IOException {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> elements = tree.searchElements("/bookshelf/books/book");

        assertEquals(elements.size(), 3);
    }

    //todo add more assertions
    @Test
    public void shouldFindElementsUsingXPath() throws IOException {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> books = tree.xpath().searchElements("/bookshelf/books/book[price>30]");

        assertEquals(books.size(), 2);
    }
}
