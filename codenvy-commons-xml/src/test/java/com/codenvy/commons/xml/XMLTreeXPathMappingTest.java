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

import static org.testng.Assert.assertEquals;

/**
 * @author Eugene Voevodin
 */
public class XMLTreeXPathMappingTest {

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
                                              "    <!-- deps --> \n" +
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
    public void testUnmodifiedTree() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        assertEquals(tree.getSingleText("/project/dependencies/dependency[1]/artifactId"), "guava");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[2]/artifactId"), "testng");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[3]/artifactId"), "mockito-core");
    }

    @Test
    public void testTreeWithExecutedAppendChild() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("//dependencies").appendChild(tree.newElement("dependency",
                                                                            tree.newElement("artifactId", "test-artifact"),
                                                                            tree.newElement("groupId", "test-group"),
                                                                            tree.newElement("version", "test-version")));

        //new child should be added to the end of list
        assertEquals(tree.getSingleText("/project/dependencies/dependency[1]/artifactId"), "guava");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[2]/artifactId"), "testng");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[3]/artifactId"), "mockito-core");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[4]/artifactId"), "test-artifact");
    }

    @Test
    public void testTreeWithExecutedInsertAfterUpdate() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("/project/dependencies")
            .getFirstChild()
            .insertAfter(tree.newElement("dependency",
                                         tree.newElement("artifactId", "test-artifact"),
                                         tree.newElement("groupId", "test-group"),
                                         tree.newElement("version", "test-version")));

        //new child should be added to the second position
        assertEquals(tree.getSingleText("/project/dependencies/dependency[1]/artifactId"), "guava");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[2]/artifactId"), "test-artifact");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[3]/artifactId"), "testng");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[4]/artifactId"), "mockito-core");
    }

    @Test
    public void testTreeWithExecutedInsertBeforeUpdate() {
        final XMLTree tree = XMLTree.from(XML_CONTENT);

        tree.getSingleElement("/project/dependencies")
            .getLastChild()
            .insertBefore(tree.newElement("dependency",
                                          tree.newElement("artifactId", "test-artifact"),
                                          tree.newElement("groupId", "test-group"),
                                          tree.newElement("version", "test-version")));

        //new child should be added to the second position
        assertEquals(tree.getSingleText("/project/dependencies/dependency[1]/artifactId"), "guava");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[2]/artifactId"), "testng");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[3]/artifactId"), "test-artifact");
        assertEquals(tree.getSingleText("/project/dependencies/dependency[4]/artifactId"), "mockito-core");
    }
    //TODO: add tests
}
