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

import java.io.IOException;
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

    private static final String XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                                              "        <!-- Test dependencies -->\n" +
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
                                              "</project>\n";

    @Test
    public void shouldFindSingleTextUsingElementPath() throws XMLStreamException {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String packaging = tree.getSingleText("/project/packaging");

        assertEquals(packaging, "jar");
    }

    @Test
    public void shouldFindSingleTextUsingXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String version = tree.xpath().getSingleText("/project/dependencies/dependency[groupId='org.testng']/version");

        assertEquals(version, "6.8");
    }

    //todo add more assertions
    @Test
    public void shouldFindElementsUsingElementPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> elements = tree.getElements("/project/dependencies/dependency");

        assertEquals(elements.size(), 3);
    }

    @Test
    public void shouldFindElementsUsingXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> artifacts = tree.xpath().getElements("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts.size(), 2);
        assertEquals(asList(artifacts.get(0).getText(), artifacts.get(1).getText()), asList("testng", "mockito-core"));
    }

    @Test
    public void shouldFindEachElementTextUsingElementsPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> artifacts = tree.getText("/project/dependencies/dependency/artifactId");

        assertEquals(artifacts, asList("guava", "testng", "mockito-core"));
    }

    @Test
    public void shouldFindEachElementTextUsingXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> artifacts = tree.xpath().getText("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts, asList("testng", "mockito-core"));
    }

    @Test
    public void shouldFindAttributesValuesWithXPath() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> attributes = tree.xpath().getText("/project/configuration/properties/@combine.self");

        assertEquals(attributes, asList("override"));
    }

    @Test
    public void shouldBeAbleToGetAttributesUsingModel() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element properties = getOnly(tree.xpath().getElements("/project/configuration/properties"));

        assertEquals(properties.getAttributes().size(), 1);
        final Attribute attribute = properties.getAttributes().get(0);
        assertEquals(attribute.getName(), "combine.self");
        assertEquals(attribute.getValue(), "override");
    }

    //TODO add more tests with attributes

    @Test
    public void shouldBeAbleToGetElementParent() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertEquals(artifactID.getParent().getName(), "dependency");
    }

    @Test
    public void shouldBeAbleToCheckElementHasSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertTrue(artifactID.hasSibling("groupId"));
        assertTrue(artifactID.hasSibling("version"));
        assertTrue(artifactID.hasSibling("scope"));
        assertFalse(artifactID.hasSibling("artifactId"));
    }

    @Test
    public void shouldBeAbleToGetFirstSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertEquals(artifactID.getSibling("groupId").getText(), "org.testng");
        assertEquals(artifactID.getSibling("version").getText(), "6.8");
        assertEquals(artifactID.getSibling("scope").getText(), "test");
        assertNull(artifactID.getSibling("other"));
    }

    @Test
    public void shouldBeAbleToGetSiblings() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='testng']/artifactId"));

        assertEquals(artifactID.getSiblings().size(), 3);
    }

    @Test
    public void shouldBeAbleToCheckElementHasChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='guava']"));

        assertTrue(guavaDep.hasChild("groupId"));
        assertTrue(guavaDep.hasChild("version"));
        assertTrue(guavaDep.hasChild("artifactId"));
        assertFalse(guavaDep.hasChild("scope"));
    }

    @Test
    public void shouldBeAbleToGetFirstChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='guava']"));

        assertEquals(guavaDep.getChild("groupId").getText(), "com.google.guava");
        assertEquals(guavaDep.getChild("version").getText(), "18.0");
        assertEquals(guavaDep.getChild("artifactId").getText(), "guava");
        assertNull(guavaDep.getChild("scope"));
    }

    @Test
    public void shouldBeAbleToGetChildren() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='guava']"));

        assertEquals(guavaDep.getChildren().size(), 3);
    }

    @Test
    public void shouldBeAbleToChangeElementTextByModel() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);
        tree.xpath();

        final Element name = getOnly(tree.getElements("/project/name"));
        name.setText("new name");

        assertEquals(tree.getSingleText("/project/name"), "new name");
        assertEquals(tree.xpath().getSingleText("/project/name"), "new name");
    }

    @Test
    public void shouldBeAbleToChangeElementTextByTree() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);
        tree.xpath();

        tree.updateText("/project/name", "new name");

        assertEquals(tree.getSingleText("/project/name"), "new name");
        assertEquals(tree.xpath().getSingleText("/project/name"), "new name");
    }

    @Test
    public void shouldBeAbleToAppendChildByModel() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);
        tree.xpath();

        final Element guavaDep = getOnly(tree.xpath().getElements("/project/dependencies/dependency[artifactId='guava']"));
        guavaDep.appendChild(tree.newElement("scope", "compile"));

        assertTrue(guavaDep.hasChild("scope"));
        assertEquals(guavaDep.getChild("scope").getText(), "compile");
        assertEquals(tree.xpath().getSingleText("/project/dependencies/dependency[artifactId='guava']/scope"), "compile");
    }

    @Test
    public void shouldBeAbleToInsertElementAfterExisting() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);
        tree.xpath();

        final Element name = getOnly(tree.getElements("/project/name"));
        name.insertAfter(tree.newElement("description", "This is test pom.xml"));

        assertTrue(name.hasSibling("description"));
        assertEquals(name.getSibling("description").getText(), "This is test pom.xml");
        assertEquals(tree.xpath().getSingleText("/project/description"), "This is test pom.xml");
    }

    @Test
    public void shouldBeAbleToInsertElementBeforeExisting() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element name = getOnly(tree.getElements("/project/name"));
        name.insertBefore(tree.newElement("description", "This is test pom.xml"));

        assertTrue(name.hasSibling("description"));
        assertEquals(name.getSibling("description").getText(), "This is test pom.xml");
        assertEquals(tree.xpath().getSingleText("/project/description"), "This is test pom.xml");
    }

    @Test
    public void shouldBeAbleToInsertElementBeforeFirstExisting() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);
        tree.xpath();

        final Element modelVersion = getOnly(tree.getElements("/project/modelVersion"));
        modelVersion.insertBefore(tree.newElement("description", "This is test pom.xml"));

        assertTrue(modelVersion.hasSibling("description"));
        assertEquals(modelVersion.getSibling("description").getText(), "This is test pom.xml");
        assertEquals(tree.xpath().getSingleText("/project/description"), "This is test pom.xml");
    }

    @Test
    public void shouldNotDestroyFormattingAfterSimpleElementInsertion() {
        final XMLTree tree = XMLTree.from("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                          "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                                          "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                          "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                                          "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                          "    <modelVersion>4.0.0</modelVersion>\n" +
                                          "    <artifactId>test-artifact</artifactId>\n" +
                                          "    <packaging>jar</packaging>\n" +
                                          "    <!-- project name -->\n" +
                                          "    <name>Test</name>\n" +
                                          "</project>");

        final Element name = getOnly(tree.getElements("/project/name"));
        name.insertAfter(tree.newElement("description", "This is test pom.xml"));

        assertEquals(new String(tree.getBytes()), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                  "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                                                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                                  "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                                                  "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                                  "    <modelVersion>4.0.0</modelVersion>\n" +
                                                  "    <artifactId>test-artifact</artifactId>\n" +
                                                  "    <packaging>jar</packaging>\n" +
                                                  "    <!-- project name -->\n" +
                                                  "    <name>Test</name>\n" +
                                                  "    <description>This is test pom.xml</description>\n" +
                                                  "</project>");
    }

    @Test
    public void shouldNotDestroyFormattingAfterComplexElementInsertion() throws IOException {
        final XMLTree tree = XMLTree.from("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                          "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                                          "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                          "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                                          "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                          "    <modelVersion>4.0.0</modelVersion>\n" +
                                          "    <artifactId>test-artifact</artifactId>\n" +
                                          "    <packaging>jar</packaging>\n" +
                                          "    <!-- project name -->\n" +
                                          "    <name>Test</name>\n" +
                                          "</project>");
        tree.xpath();

        tree.getRootElement().appendChild(tree.newElement("dependencies", tree.newElement("dependency",
                                                                                          tree.newElement("artifactId", "test-artifact"),
                                                                                          tree.newElement("groupId", "test-group"),
                                                                                          tree.newElement("version", "test-version"))));

        assertEquals(new String(tree.getBytes()), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                  "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                                                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                                  "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                                                  "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                                  "    <modelVersion>4.0.0</modelVersion>\n" +
                                                  "    <artifactId>test-artifact</artifactId>\n" +
                                                  "    <packaging>jar</packaging>\n" +
                                                  "    <!-- project name -->\n" +
                                                  "    <name>Test</name>\n" +
                                                  "    <dependencies>\n" +
                                                  "        <dependency>\n" +
                                                  "            <artifactId>test-artifact</artifactId>\n" +
                                                  "            <groupId>test-group</groupId>\n" +
                                                  "            <version>test-version</version>\n" +
                                                  "        </dependency>\n" +
                                                  "    </dependencies>\n" +
                                                  "</project>");
        assertEquals(tree.xpath().getSingleText("/project/dependencies/dependency/artifactId"), "test-artifact");
    }
}