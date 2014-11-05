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
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;
import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * Each XMLTree update should be reinvoked
 * in XPathLayer to stay in touch with real data.
 * <p/>
 * What is XPathLayer?
 * One of the main requirements to XMLTree
 * is that client should have ability to use xpath syntax for querying!
 * How can it be done?
 * 1. By implementing org.w3c.dom.
 * 2. By parallel document parsing to {@link XMLTree} and {@link Document}
 * 3. By parsing xml file with existed java parsers which support xpath.
 * <p/>
 * XPath layer is 2nd variant.
 * <p/>
 * So we delegate existed parser to select information with xpath.
 * <p/>
 * 1. Each xpath request which searches for getText - returns getText.
 * <p/>
 * 2. Each xpath request which searches for NodeList - returns {@code List<Element>}.
 * So we search for XMLTree.Node-s which are same to response org.w3c.don.Node-s
 * just mapped to Elements.
 * <p/>
 *
 * @author Eugene Voevodin
 */
public final class XPathLayer {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private static final XPathFactory           XPATH_FACTORY            = XPathFactory.newInstance();

    private final Document document;
    private final XMLTree  xmlTree;

    XPathLayer(XMLTree xmlTree) {
        this.xmlTree = xmlTree;
        this.document = parseQuietly(xmlTree.xml);
    }

    public String getSingleText(String expression) {
        return evaluateQuietly(expression, STRING);
    }

    public List<String> getText(String expression) {
        final NodeList nodeList = evaluateQuietly(expression, NODESET);
        final List<String> elementsText = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            elementsText.add(nodeList.item(i).getTextContent());
        }
        return elementsText;
    }

    public List<Element> getElements(String expression) {
        final NodeList nodeList = evaluateQuietly(expression, NODESET);
        //TODO think about array size
        final List<Element> requested = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node current = nodeList.item(i);
            if (current.getNodeType() == ELEMENT_NODE) {
                requested.add(elementFor(current));
            }
        }
        return requested;
    }

    //FIXME don't use index ?
    private int elementIndex(Element element) {
        List<Element> related = xmlTree.elements.get(element.path());
        for (int idx = 0; idx < related.size(); idx++) {
            if (related.get(idx) == element) {
                return idx;
            }
        }
        throw new XMLTreeException("You should not see this message");
    }

    //FIXME don't use index ?
    private int nodeIndex(NodeList list, Node node) {
        int idx = 0;
        for (int i = 0; i < list.getLength(); i++) {
            final Node current = list.item(i);
            if (current.getNodeType() == ELEMENT_NODE) {
                if (list.item(i) == node) {
                    return idx;
                }
                idx++;
            }
        }
        throw new XMLTreeException("You should not see this message");
    }

    void updateText(Element target) {
        final NodeList list = evaluateQuietly(target.path(), NODESET);
        list.item(elementIndex(target)).setTextContent(target.getText());
    }

    void insertAfter(Element newElement, Element refElement) {
        final Node refNode = nodeFor(refElement);
        final Node nextSibling = nextElementSibling(refNode);
        if (nextSibling != null) {
            document.getDocumentElement().insertBefore(clone(newElement), nextSibling);
        } else {
            refNode.getParentNode().appendChild(clone(newElement));
        }
    }

    void insertBefore(Element newElement, Element refElement) {
        final Node refNode = nodeFor(refElement);
        document.getDocumentElement().insertBefore(clone(newElement), refNode);
    }

    private Node nodeFor(Element element) {
        final NodeList nodeList = evaluateQuietly(element.path(), NODESET);
        return nodeList.item(elementIndex(element));
    }

    private Element elementFor(Node node) {
        final String path = path(node);
        final List<Element> elements = xmlTree.elements.get(path);
        final NodeList nodeList = evaluateQuietly(path, NODESET);
        return elements.get(nodeIndex(nodeList, node));
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
            return db.parse(new InputSource(new CharArrayReader(xml)));
        } catch (Exception ex) {
            throw XMLTreeException.wrap(ex);
        }
    }

    private Node nextElementSibling(Node node) {
        node = node.getNextSibling();
        while (node != null && node.getNodeType() != ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return node;
    }

    private Node clone(Element element) {
        final Node newNode = document.createElement(element.getName());
        newNode.setTextContent(element.getText());
        for (Element child : element.getChildren()) {
            newNode.appendChild(clone(child));
        }
        return newNode;
    }
}
