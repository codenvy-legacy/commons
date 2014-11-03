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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;
import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * TODO: provide update methods
 * Each XMLTree update should be reinvoked
 * in XPathLayer to stay in touch with real data.
 * <p/>
 * FIXME: add javadoc tags
 * What is XPathLayer?
 * One of the main requirements to XMLTree
 * is that client should have ability to use xpath syntax for querying!
 * How can it be done?
 * 1. By implementing org.w3c.dom.
 * 2. By parsing xml file with existed java parsers which support xpath.
 * <p/>
 * XPath layer is 2nd variant.
 * <p/>
 * So we delegate existed parser to select information with xpath.
 * <p/>
 * 1. Each xpath request which searches for eachText - returns eachText.
 * <p/>
 * 2. Each xpath request which searches for NodeList - returns {@code List<Element>}.
 * So we search for XMLTree.Node-s which are same to response org.w3c.don.Node-s
 * and mapping it to List of Elements.
 * <p/>
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
        this.document = parseQuietly(owner.xml);
    }

    public String singleText(String expression) {
        return evaluateQuietly(expression, STRING);
    }

    public List<String> eachText(String expression) {
        final NodeList nodeList = evaluateQuietly(expression, NODESET);
        //TODO think about list size
        final List<String> elementsText = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            elementsText.add(nodeList.item(i).getTextContent());
        }
        return elementsText;
    }

    public List<Element> elements(String expression) {
        final NodeList nodeList = evaluateQuietly(expression, NODESET);
        //TODO think about array size
        final List<Element> requested = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node current = nodeList.item(i);
            if (current.getNodeType() == ELEMENT_NODE) {
                final String path = path(current);
                //FIXME
                final NodeList nodes = evaluateQuietly(path, NODESET);

                final List<Element> variants = owner.elements.get(path);
                requested.add(variants.get(nodeIndex(nodes, current)));
            }
        }
        return requested;
    }


    /**
     * It is really bad way to do it right.
     * We searching for element index in document and trying to select
     * node with same index from our tree
     * <p/>
     * <p/>
     * FIXME
     */
    private int nodeIndex(NodeList list, Node node) {
        int idx = 0;
        for (int i = 0; i < list.getLength(); i++) {
            final Node current = list.item(i);
            if (current.getNodeType() == ELEMENT_NODE) {
                if (list.item(i).equals(node)) {
                    return idx;
                }
                idx++;
            }
        }
        throw new XMLTreeException("You should not see this message");
    }

    private int elementIndex(Element element) {
        List<Element> related = owner.elements.get(element.path());
        for (int idx = 0; idx < related.size(); idx++) {
            if (element == related.get(idx)) {
                return idx;
            }
        }
        throw new XMLTreeException("You should not see this message");
    }

    public void updateText(Element target) {
        final NodeList list = evaluateQuietly(target.path(), NODESET);
        list.item(elementIndex(target)).setTextContent(target.getText());
    }

    private String path(Node node) {
        final StringBuilder path = new StringBuilder();
        while (node.getNodeType() != DOCUMENT_NODE) {
            path.insert(0, '/')
                .insert(1, node.getNodeName());
            node = node.getParentNode();
        }
        return path.toString();
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

    private Document parseQuietly(char[] xml) {
        try {
            final DocumentBuilder db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return db.parse(newIS(xml));
        } catch (Exception ex) {
            throw XMLTreeException.wrap(ex);
        }
    }

    //TODO remove from here
    public InputStream newIS(char[] xml) {
        return new ByteArrayInputStream(new String(xml).getBytes());
    }
}
