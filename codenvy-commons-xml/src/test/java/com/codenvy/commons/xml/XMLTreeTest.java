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

import javax.xml.stream.XMLStreamException;

import java.util.List;

import static com.codenvy.commons.xml.Util.getOnly;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Eugene Voevodin
 */
public class XMLTreeTest {

    private static final char[] XML_CONTENT = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                               "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                                               "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                               "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                                               "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                               "    <modelVersion>4.0.0</modelVersion>\n" +
                                               "    <parent>\n" +
                                               "        <artifactId>test-parent</artifactId>\n" +
                                               "        <groupId>test-parent-group-id</groupId>\n" +
                                               "        <version>test-parent-version</version>\n" +
                                               "    </parent>\n" +
                                               "    <artifactId>test-artifact</artifactId>\n" +
                                               "    <packaging>jar</packaging>\n" +
                                               "    <name>Test</name>\n" +
                                               "    <configuration>\n" +
                                               "        <items combine.children=\"append\">\n" +
                                               "            <item>parent-1</item>\n" +
                                               "            <item>parent-2</item>\n" +
                                               "            <item>child-1</item>\n" +
                                               "        </items>\n" +
                                               "        <properties combine.self=\"override\">\n" +
                                               "            <childKey>child</childKey>\n" +
                                               "        </properties>\n" +
                                               "    </configuration>\n" +
                                               "    <dependencies>\n" +
                                               "        <dependency>\n" +
                                               "            <groupId>com.google.guava</groupId>\n" +
                                               "            <artifactId>guava</artifactId>\n" +
                                               "            <version>18.0</version>\n" +
                                               "        </dependency>\n" +
                                               "        <dependency>\n" +
                                               "            <groupId>org.testng</groupId>\n" +
                                               "            <artifactId>testng</artifactId>\n" +
                                               "            <version>6.8</version>\n" +
                                               "            <scope>test</scope>\n" +
                                               "        </dependency>\n" +
                                               "        <dependency>\n" +
                                               "            <groupId>org.mockito</groupId>\n" +
                                               "            <artifactId>mockito-core</artifactId>\n" +
                                               "            <version>1.10.0</version>\n" +
                                               "            <scope>test</scope>\n" +
                                               "        </dependency>\n" +
                                               "    </dependencies>\n" +
                                               "</project>\n").toCharArray();

    @Test
    public void shouldFindSingleTextUsingElementPath() throws XMLStreamException {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String packaging = tree.singleText("/project/packaging");

        assertEquals(packaging, "jar");
    }

    @Test
    public void shouldFindSingleTextUsingXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String version = tree.xpath().singleText("/project/dependencies/dependency[groupId='org.testng']/version");

        assertEquals(version, "6.8");
    }

    //todo add more assertions
    @Test
    public void shouldFindElementsUsingElementPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> elements = tree.elements("/project/dependencies/dependency");

        assertEquals(elements.size(), 3);
    }

    @Test
    public void shouldFindElementsUsingXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> artifacts = tree.xpath().elements("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts.size(), 2);
        assertEquals(asList(artifacts.get(0).getText(), artifacts.get(1).getText()), asList("testng", "mockito-core"));
    }

    @Test
    public void shouldFindEachElementTextUsingElementsPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> artifacts = tree.eachText("/project/dependencies/dependency/artifactId");

        assertEquals(artifacts, asList("guava", "testng", "mockito-core"));
    }

    @Test
    public void shouldFindEachElementTextUsingXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> artifacts = tree.xpath().eachText("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts, asList("testng", "mockito-core"));
    }

    @Test
    public void shouldFindAttributesTextWithXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> attributes = tree.xpath().eachText("/project/configuration/properties/@combine.self");

        assertEquals(attributes, asList("override"));
    }


    //TODO add more tests with attributes

    @Test
    public void shouldBeAbleToGetElementParent() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertEquals(artifactID.getParent().getName(), "dependency");
    }

    @Test
    public void shouldBeAbleToCheckElementHasSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertTrue(artifactID.hasSibling("groupId"));
        assertTrue(artifactID.hasSibling("version"));
        assertTrue(artifactID.hasSibling("scope"));
        assertFalse(artifactID.hasSibling("artifactId"));
    }

    @Test
    public void shouldBeAbleToGetFirstSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertEquals(artifactID.getFirstSibling("groupId").getText(), "org.testng");
        assertEquals(artifactID.getFirstSibling("version").getText(), "6.8");
        assertEquals(artifactID.getFirstSibling("scope").getText(), "test");
        assertNull(artifactID.getFirstSibling("other"));
    }

    @Test
    public void shouldBeAbleToGetSiblings() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertEquals(artifactID.getSiblings().size(), 3);
    }

    @Test
    public void shouldBeAbleToCheckElementHasChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='guava']"));

        assertTrue(guavaDep.hasChild("groupId"));
        assertTrue(guavaDep.hasChild("version"));
        assertTrue(guavaDep.hasChild("artifactId"));
        assertFalse(guavaDep.hasChild("scope"));
    }

    @Test
    public void shouldBeAbleToGetFirstChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='guava']"));

        assertEquals(guavaDep.getFirstChild("groupId").getText(), "com.google.guava");
        assertEquals(guavaDep.getFirstChild("version").getText(), "18.0");
        assertEquals(guavaDep.getFirstChild("artifactId").getText(), "guava");
        assertNull(guavaDep.getFirstChild("scope"));
    }

    @Test
    public void shouldBeAbleToGetChildren() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='guava']"));

        assertEquals(guavaDep.getChildren().size(), 3);
    }

    @Test
    public void shouldBeAbleToChangeElementText() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element version = getOnly(tree.xpath().elements("/project/dependencies/dependency[artifactId='testng']/version"));
        version.setText("6.0");

        assertEquals(tree.elements("/project/dependencies/dependency/version").get(1).getText(), "6.0");
        assertEquals(tree.xpath().singleText("/project/dependencies/dependency[artifactId='testng']/version"), "6.0");
    }
}