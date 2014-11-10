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
    public void shouldFindSingleText() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final String version = tree.getSingleText("/project/dependencies/dependency[groupId='org.testng']/version");

        assertEquals(version, "6.8");
    }

    @Test
    public void shouldFindEachElementText() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> artifacts = tree.getText("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts, asList("testng", "mockito-core"));
    }

    @Test
    public void shouldFindElements() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<Element> artifacts = tree.getElements("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts.size(), 2);
        assertEquals(asList(artifacts.get(0).getText(), artifacts.get(1).getText()), asList("testng", "mockito-core"));
    }

    @Test
    public void shouldFindAttributeValues() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final List<String> attributes = tree.getText("/project/configuration/properties/@combine.self");

        assertEquals(attributes, asList("override"));
    }

    @Test
    public void shouldBeAbleToGetAttributesUsingModel() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element properties = tree.getSingleElement("/project/configuration/properties");

        assertEquals(properties.getAttributes().size(), 1);
        final Attribute attribute = properties.getAttributes().get(0);
        assertEquals(attribute.getName(), "combine.self");
        assertEquals(attribute.getValue(), "override");
    }

    @Test
    public void shouldBeAbleToGetElementParent() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactId = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']/artifactId");

        assertEquals(artifactId.getParent().getName(), "dependency");
    }

    @Test
    public void shouldBeAbleToGetOnlySibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element name = tree.getSingleElement("/project/name");

        assertEquals(name.getSingleSibling("packaging").getText(), "jar");
    }

    @Test
    public void shouldReturnNullIfSiblingWithRequestedNameDoesNotExist() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element name = tree.getSingleElement("/project/name");

        assertNull(name.getSingleSibling("developers"));
    }

    @Test(expectedExceptions = XMLTreeException.class)
    public void shouldThrowExceptionIfMoreThenOnlySiblingWereFound() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element dependency = tree.getSingleElement("/project/dependencies/dependency[artifactId='guava']");

        dependency.getSingleSibling("dependency");
    }

    @Test
    public void shouldBeAbleToGetOnlyChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        assertEquals(tree.getRoot().getSingleChild("packaging").getText(), "jar");
    }

    @Test
    public void shouldReturnNullIfChildDoesNotExist() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        assertNull(tree.getRoot().getSingleChild("developers"));
    }

    @Test(expectedExceptions = XMLTreeException.class)
    public void shouldThrowExceptionIfMoreThenOnlyChildWereFound() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element dependencies = tree.getSingleElement("/project/dependencies");

        dependencies.getSingleChild("dependency");
    }

    @Test
    public void shouldBeAbleToCheckElementHasSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactID = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']/artifactId");

        assertTrue(artifactID.hasSibling("groupId"));
        assertTrue(artifactID.hasSibling("version"));
        assertTrue(artifactID.hasSibling("scope"));
        assertFalse(artifactID.hasSibling("artifactId"));
    }

    @Test
    public void shouldBeAbleToGetSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactId = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']/artifactId");

        assertEquals(artifactId.getSingleSibling("groupId").getText(), "org.testng");
        assertEquals(artifactId.getSingleSibling("version").getText(), "6.8");
        assertEquals(artifactId.getSingleSibling("scope").getText(), "test");
        assertNull(artifactId.getSingleSibling("other"));
    }

    @Test
    public void shouldBeAbleToGetPreviousSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element scope = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']/scope");

        assertEquals(scope.getPreviousSibling().getName(), "version");
    }

    @Test
    public void shouldBeAbleToGetNextSibling() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element scope = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']/version");

        assertEquals(scope.getNextSibling().getName(), "scope");
    }

    @Test
    public void shouldBeAbleToGetRootElement() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element project = tree.getRoot();

        assertEquals(project.getName(), "project");
        assertFalse(project.hasParent());
    }

    @Test
    public void shouldBeAbleToGetSiblings() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element artifactId = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']/artifactId");

        assertEquals(artifactId.getSiblings().size(), 3);
    }

    @Test
    public void shouldBeAbleToCheckElementHasChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = tree.getSingleElement("/project/dependencies/dependency[artifactId='guava']");

        assertTrue(guavaDep.hasChild("groupId"));
        assertTrue(guavaDep.hasChild("version"));
        assertTrue(guavaDep.hasChild("artifactId"));
        assertFalse(guavaDep.hasChild("scope"));
    }

    @Test
    public void shouldBeAbleToGetFirstChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = tree.getSingleElement("/project/dependencies/dependency[artifactId='guava']");

        assertEquals(guavaDep.getSingleChild("groupId").getText(), "com.google.guava");
        assertEquals(guavaDep.getSingleChild("version").getText(), "18.0");
        assertEquals(guavaDep.getSingleChild("artifactId").getText(), "guava");
        assertNull(guavaDep.getSingleChild("scope"));
    }

    @Test
    public void shouldBeAbleToGetChildren() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = tree.getSingleElement("/project/dependencies/dependency[artifactId='guava']");

        assertEquals(guavaDep.getChildren().size(), 3);
    }

    @Test
    public void shouldBeAbleToChangeElementTextByModel() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element name = tree.getSingleElement("/project/dependencies/dependency[artifactId='guava']/version");
        name.setText("new version");

        assertEquals(tree.getSingleText("/project/dependencies/dependency[artifactId='guava']/version"), "new version");
    }

    @Test
    public void shouldBeAbleToChangeElementTextByTree() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.updateText("/project/name", "new name");

        assertEquals(tree.getSingleText("/project/name"), "new name");
    }

    @Test
    public void shouldBeAbleToAppendChildByModel() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element guavaDep = tree.getSingleElement("/project/dependencies/dependency[artifactId='guava']");
        guavaDep.appendChild(tree.newElement("scope", "compile"));

        assertTrue(guavaDep.hasChild("scope"));
        assertEquals(guavaDep.getSingleChild("scope").getText(), "compile");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[artifactId='guava']/scope"), "compile");
    }

    @Test
    public void shouldBeAbleToInsertElementAfterExisting() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element name = tree.getSingleElement("/project/name");
        name.insertAfter(tree.newElement("description", "This is test pom.xml"));

        assertTrue(name.hasSibling("description"));
        assertEquals(name.getSingleSibling("description").getText(), "This is test pom.xml");
        assertEquals(tree.getSingleText("/project/description"), "This is test pom.xml");
    }

    @Test
    public void shouldBeAbleToInsertElementBeforeExisting() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element name = tree.getSingleElement("/project/name");
        name.insertBefore(tree.newElement("description", "This is test pom.xml"));

        assertTrue(name.hasSibling("description"));
        assertEquals(name.getSingleSibling("description").getText(), "This is test pom.xml");
        assertEquals(tree.getSingleText("/project/description"), "This is test pom.xml");
    }

    @Test
    public void shouldBeAbleToInsertElementBeforeFirstExisting() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element modelVersion = tree.getSingleElement("/project/modelVersion");
        modelVersion.insertBefore(tree.newElement("description", "This is test pom.xml"));

        assertTrue(modelVersion.hasSibling("description"));
        assertEquals(modelVersion.getSingleSibling("description").getText(), "This is test pom.xml");
        assertEquals(tree.getSingleText("/project/description"), "This is test pom.xml");
    }

    @Test
    public void shouldBeAbleToRemoveElementByTree() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);
        assertTrue(tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']").hasChild("scope"));

        tree.removeElement("/project/dependencies/dependency[artifactId='testng']/scope");

        assertFalse(tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']").hasChild("scope"));
    }

    @Test
    public void shouldBeAbleToRemoveElementChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element dependency = tree.getSingleElement("/project/dependencies/dependency[artifactId='testng']");
        assertTrue(dependency.hasChild("version"));

        dependency.removeChild("version");
        assertFalse(dependency.hasChild("version"));
    }

    @Test
    public void shouldBeAbleToRemoveChildren() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element dependencies = tree.getSingleElement("/project/dependencies");
        dependencies.removeChildren("dependency");

        assertTrue(dependencies.getChildren().isEmpty());
    }

    @Test
    public void chainRemovingAndBatchRemovingShouldProduceSameTreeBytes() {
        final XMLTree tree1 = XMLTree.from(XML_CONTENT);
        final XMLTree tree2 = XMLTree.from(XML_CONTENT);

        //removing dependencies from first tree
        tree1.removeElement("/project/dependencies/dependency[3]");
        tree1.removeElement("/project/dependencies/dependency[2]");
        tree1.removeElement("/project/dependencies/dependency[1]");
        //removing dependencies from second tree
        tree2.getSingleElement("//dependencies").removeChildren("dependency");

        //use strings for assertion to quick review difference if assertion failed
        assertEquals(new String(tree1.getBytes()), new String(tree2.getBytes()));
    }

    @Test
    public void removeInsertedElementShouldProduceSameTreeBytes() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        final Element description = tree.getRoot()
                                        .getLastChild()
                                        .insertBefore(tree.newElement("description", "description"))
                                        .getSingleSibling("description");
        description.remove();

        assertEquals(new String(tree.getBytes()), XML_CONTENT);
    }

    @Test
    public void removeInsertedAfterElementWithChildrenShouldProduceSameTreeBytes() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("//dependencies")
            .getFirstChild()
            .insertAfter(tree.newElement("dependency",
                                         tree.newElement("artifactId", "test-artifact"),
                                         tree.newElement("groupId", "test-group"),
                                         tree.newElement("version", "test-version")))
            .getNextSibling()
            .remove();

        assertEquals(new String(tree.getBytes()), XML_CONTENT);
    }

    @Test
    public void removeAppendedElementWithChildrenShouldProduceSameTreeBytes() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("//dependencies")
            .appendChild(tree.newElement("dependency",
                                         tree.newElement("artifactId", "test-artifact"),
                                         tree.newElement("groupId", "test-group"),
                                         tree.newElement("version", "test-version")))
            .getLastChild()
            .remove();

        //use strings for assertion to quick review difference if assertion failed
        assertEquals(new String(tree.getBytes()), XML_CONTENT);
    }

    @Test
    public void removeInsertedBeforeElementWithChildrenShouldProduceSameTreeBytes() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("//dependencies")
            .getFirstChild()
            .insertBefore(tree.newElement("dependency",
                                          tree.newElement("artifactId", "test-artifact"),
                                          tree.newElement("groupId", "test-group"),
                                          tree.newElement("version", "test-version")))
            .getPreviousSibling()
            .remove();

        //use strings for assertion to quick review difference if assertion failed
        assertEquals(new String(tree.getBytes()), XML_CONTENT);
    }

    @Test
    public void shouldBeAbleToChangeTextOfNewlyInsertedElement() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("//dependencies")
            .getFirstChild()
            .insertBefore(tree.newElement("dependency",
                                          tree.newElement("artifactId", "test-artifact"),
                                          tree.newElement("groupId", "test-group"),
                                          tree.newElement("version", "test-version")));
        tree.updateText("//dependencies/dependency[artifactId='test-artifact']/version", "test-version");

        assertEquals(tree.getSingleText("//dependencies/dependency[artifactId='test-artifact']/version"), "test-version");
    }

    //We need to know that all elements
    //and text segments were indexed correct after insertion of
    //new element to do so we need to change any of content
    //which position follows inserted element position
    @Test
    public void shouldBeAbleToChangeInsertedElementText() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getRoot()
            .getLastChild()
            .insertBefore(tree.newElement("description", "description"));
        //all elements after description should be indexed again
        //we can check element text bounds by inserting new content
        tree.updateText("/project/dependencies/dependency[artifactId='guava']/version", "new version");

        //TODO add better assertion
        //create new tree to be sure that text was inserted in correct place
        final XMLTree tree2 = XMLTree.from(new String(tree.getBytes()));
        assertEquals(tree2.getSingleText("/project/dependencies/dependency[artifactId='guava']/version"), "new version");
    }

    @Test
    public void shouldBeAbleToAppendChildToEmptyElement() {
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
                                          "    <dependencies></dependencies>\n" +
                                          "</project>");

        tree.getSingleElement("//dependencies")
            .appendChild(tree.newElement("dependency",
                                         tree.newElement("artifactId", "test-artifact"),
                                         tree.newElement("groupId", "test-group"),
                                         tree.newElement("version", "test-version")));

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
                                                  "        </dependency></dependencies>\n" +
                                                  "</project>");
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

        final Element name = tree.getSingleElement("/project/name");
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

        tree.getRoot().appendChild(tree.newElement("dependencies", tree.newElement("dependency",
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
        assertEquals(tree.getSingleText("/project/dependencies/dependency/artifactId"), "test-artifact");
    }

    @Test
    public void shouldNotDestroyFormattingAfterRemovingElement() {
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
                                          "    <dependencies>\n" +
                                          "        <dependency>\n" +
                                          "            <artifactId>test-artifact</artifactId>\n" +
                                          "            <groupId>test-group</groupId>\n" +
                                          "            <version>test-version</version>\n" +
                                          "            <scope>compile</scope>\n" +
                                          "        </dependency>\n" +
                                          "    </dependencies>\n" +
                                          "</project>");

        tree.removeElement("/project/dependencies/dependency[1]/scope");

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
    }

    @Test
    public void shouldNotDestroyFormattingAfterRemovingElementWithChildren() {
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
                                          "    <dependencies>\n" +
                                          "        <dependency>\n" +
                                          "            <artifactId>test-artifact</artifactId>\n" +
                                          "            <groupId>test-group</groupId>\n" +
                                          "            <version>test-version</version>\n" +
                                          "            <scope>compile</scope>\n" +
                                          "        </dependency>\n" +
                                          "    </dependencies>\n" +
                                          "</project>");

        tree.removeElement("/project/dependencies");

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
                                                  "</project>");
    }

    @Test
    public void batchUpdateShouldProduceExpectedContent() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        //removing parent
        tree.removeElement("//parent");
        //removing configuration
        tree.getSingleElement("//configuration").remove();
        //adding groupId before artifactId and version after
        tree.getSingleElement("/project/artifactId")
            .insertBefore(tree.newElement("groupId", "test-group"))
            .insertAfter(tree.newElement("version", "test-version"));
        //delete all test dependencies
        for (Element element : tree.getElements("//dependency[scope='test']")) {
            element.remove();
        }
        //adding junit dependency to the end of dependencies list
        tree.getSingleElement("//dependencies")
            .appendChild(tree.newElement("dependency",
                                         tree.newElement("artifactId", "junit"),
                                         tree.newElement("groupId", "junit"),
                                         tree.newElement("version", "4.0")));
        //change junit version
        tree.updateText("//dependency[artifactId='junit']/version", "4.1");

        assertEquals(new String(tree.getBytes()), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                  "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
                                                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                                  "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
                                                  "http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                                  "    <modelVersion>4.0.0</modelVersion>\n" +
                                                  "    <groupId>test-group</groupId>\n" +
                                                  "    <artifactId>test-artifact</artifactId>\n" +
                                                  "    <version>test-version</version>\n" +
                                                  "    <packaging>jar</packaging>\n" +
                                                  "    <name>Test</name>\n" +
                                                  "    <dependencies>\n" +
                                                  "        <dependency>\n" +
                                                  "            <groupId>com.google.guava</groupId>\n" +
                                                  "            <artifactId>guava</artifactId>\n" +
                                                  "            <version>18.0</version>\n" +
                                                  "        </dependency>\n" +
                                                  "        <!-- Test dependencies -->\n" +
                                                  "        <dependency>\n" +
                                                  "            <artifactId>junit</artifactId>\n" +
                                                  "            <groupId>junit</groupId>\n" +
                                                  "            <version>4.1</version>\n" +
                                                  "        </dependency>\n" +
                                                  "    </dependencies>\n" +
                                                  "</project>\n");
    }

    @Test
    public void textBeforeElementShouldBeRemovedWithElement() {
        final XMLTree tree = XMLTree.from("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                          "<root>text-before<test>text-inside</test>text-after</root>");

        tree.removeElement("//test");

        assertEquals(new String(tree.getBytes()), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                  "<root>text-after</root>");

    }

    @Test
    public void commentBeforeElementShouldNotBeRemovedWithElement() {
        final XMLTree tree = XMLTree.from("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                          "<root><!--text-before--><test>text-inside</test>text-after</root>");

        tree.removeElement("//test");

        assertEquals(new String(tree.getBytes()), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                  "<root><!--text-before-->text-after</root>");
    }
}