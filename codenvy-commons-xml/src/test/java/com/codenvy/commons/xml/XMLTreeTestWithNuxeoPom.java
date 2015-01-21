/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.xml;


import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;

public class XMLTreeTestWithNuxeoPom {

    @Test
    public void shouldFindSingleText() throws IOException {
        final XMLTree tree = XMLTree.from(XMLTreeTestWithNuxeoPom.class.getResourceAsStream("nuxeo-pom.xml"));
        final String version = tree.getSingleText("/project/dependencyManagement/dependencies/dependency[artifactId='nuxeo-runtime-datasource']/version");
        assertEquals(version, "${nuxeo.runtime.version}");

        final String projectArtifact = tree.getSingleText("/project/artifactId");
        assertEquals(projectArtifact, "nuxeo-ecm");

        final String versionProperties = tree.getSingleText("/project/properties/nuxeo.runtime.version");
        assertEquals(versionProperties, "7.2-SNAPSHOT");
    }

    @Test
    public void shouldFindEachElementText() throws IOException {
        final XMLTree tree = XMLTree.from(XMLTreeTestWithNuxeoPom.class.getResourceAsStream("nuxeo-pom.xml"));

        final List<String> artifacts = tree.getText("/project/dependencies/dependency[scope='test']/artifactId");

        assertEquals(artifacts, asList("log4j", "junit"));
    }

    @Test
    public void testContentWithSpaces() throws IOException {
        final XMLTree tree = XMLTree.from(XMLTreeTestWithNuxeoPom.class.getResourceAsStream("nuxeo-pom.xml"));

        final String repoUrl = tree.getSingleText("/project/profiles/profile[id='qa']/repositories/repository[id='internal-releases']/url");

        assertEquals(repoUrl.trim(), "http://maven.in.nuxeo.com/nexus/content/groups/internal-releases");
    }

    @Test
    public void shouldBeAbleToGetChildren() throws IOException {
        final XMLTree tree = XMLTree.from(XMLTreeTestWithNuxeoPom.class.getResourceAsStream("nuxeo-pom.xml"));
        final Element junitDep = tree.getSingleElement("/project/dependencies/dependency[artifactId='junit']");
        assertEquals(junitDep.getChildren().size(), 3);
    }

    @Test
    public void testMavenModel() throws IOException {
        final XMLTree tree = XMLTree.from(XMLTreeTestWithNuxeoPom.class.getResourceAsStream("nuxeo-pom.xml"));
        // these calls should work without errors:

        Element root = tree.getRoot();
        assertFalse(root.hasSingleChild("parent"));

        assertTrue(root.hasSingleChild("dependencyManagement"));
        tree.getSingleElement("/project/dependencyManagement");
        tree.getElements("/project/dependencyManagement/dependencies/dependency");

        assertTrue(root.hasSingleChild("build"));
        root.getSingleChild("build");
        assertTrue(root.hasSingleChild("dependencies"));
        tree.getElements("/project/dependencies/dependency");

        assertTrue(root.hasSingleChild("modules"));
        tree.getElements("/project/modules/module");

        assertTrue(root.hasSingleChild("properties"));
        root.getSingleChild("properties");
    }
}
