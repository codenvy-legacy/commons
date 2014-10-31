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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * TODO: provide update methods
 * Each XMLTree update should be reinvoked
 * in XPathLayer to stay in touch with real data.
 *
 * FIXME: add javadoc tags
 * What is XPathLayer?
 * One of the main requirements to XMLTree
 * is that client should have ability to use xpath syntax for querying!
 * How can it be done?
 * 1. By implementing org.w3c.dom.
 * 2. By parsing xml file with existed java parsers which support xpath.
 *
 * XPath layer is 2nd variant.
 *
 * So we delegate existed parser to select information with xpath.
 *
 * 1. Each xpath request which searches for text - returns text.
 *
 * 2. Each xpath request which searches for NodeList - returns {@code List<Element>}.
 * So we search for XMLTree.Node-s which are same to response org.w3c.don.Node-s
 * and mapping it to List of Elements.
 *
 * 3. Each xpath request which searches for NodeList as Attributes - returns {@code List<Attribute>}
 * We do the same like with elements.
 *
 * @author Eugene Voevodin
 */
public final class XPathLayer {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private static final XPathFactory           XPATH_FACTORY            = XPathFactory.newInstance();

    private final Document document;
    private final XMLTree  owner;

    XPathLayer(XMLTree owner) {
        this.owner = owner;
        //TODO: do not parse tree in constructor
        //why not?
        //we need to have one xpath layer instance per one tree
        //and when user doesn't need to use xpath
        //we parsing document, but we don't need to do it
        this.document = parseQuietly(owner.xmlBytes);
    }

    public String searchText(String expression) {
        return evaluateQuietly(expression, STRING);
    }

    public List<Element> searchElements(String expression) {
        final NodeList nodeList = evaluateQuietly(expression, NODESET);
        final List<Element> requested = new ArrayList<>();
        //we should filter and map taken nodesMap
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == ELEMENT_NODE) {
                //TODO: do not map org.w3c.dom.Node to Element
                //we need to search for this node in our tree
                //and then map found to element
                requested.add(new Element(currentNode.getNodeName()).setText(currentNode.getTextContent()));
            }
        }
        return requested;
    }

    //TODO
    public List<Attribute> searchAttributes(String expression) {
        throw new XMLTreeException("Not implemented");
    }

    @SuppressWarnings("unchecked")
    private <T> T evaluateQuietly(String expression, QName returnType) {
        final XPath xpath = XPATH_FACTORY.newXPath();
        try {
            return (T)xpath.evaluate(expression, document, returnType);
        } catch (XPathExpressionException xpathEx) {
            throw XMLTreeException.wrap(xpathEx);
        }
    }

    private Document parseQuietly(byte[] xml) {
        try {
            final DocumentBuilder db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return db.parse(new ByteArrayInputStream(xml));
        } catch (Exception ex) {
            throw XMLTreeException.wrap(ex);
        }
    }
}
