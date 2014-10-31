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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.ByteStreams;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.codenvy.commons.xml.Util.ensureOnly;
import static com.codenvy.commons.xml.Util.fetchText;
import static java.util.Collections.emptyList;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * TODO: add update methods
 * use batch update of src array or one update per one operation?
 *
 * @author Eugene Voevodin
 */
public final class XMLTree {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    /**
     * Creates XMLTree from input stream.
     * Doesn't close the stream.
     */
    public static XMLTree from(InputStream is) throws IOException {
        return new XMLTree(ByteStreams.toByteArray(is));
    }

    /**
     * Create XMLTree from byte array.
     */
    public static XMLTree from(byte[] xmlBytes) {
        return new XMLTree(xmlBytes);
    }

    private XPathLayer xPathLayer;

    byte[]                     xmlBytes;
    ListMultimap<String, Node> nodesMap;

    private XMLTree(byte[] xmlBytes) {
        this.xmlBytes = xmlBytes;
        nodesMap = ArrayListMultimap.create();
        constructTreeQuietly();
    }

    /**
     * Searches for element text
     */
    public String searchText(String query) {
        return text(ensureOnly(nodesMap.get(query)));
    }

    /**
     * Searches for elements
     * TODO add an example of path
     *
     * @param path
     *         element path
     * @return list of found elements or empty list if no elements were found
     */
    public List<Element> searchElements(String path) {
        final List<Node> nodes = nodesMap.get(path);
        final List<Element> elements = new ArrayList<>(nodes.size());
        for (Node node : nodes) {
            elements.add(new Element(node));
        }
        return elements;
    }

    public XPathLayer xpath() {
        if (xPathLayer == null) {
            xPathLayer = new XPathLayer(this);
        }
        return xPathLayer;
    }

    //TODO: mb move to Node!?
    public String text(Node node) {
        if (node.text == null) {
            node.text = node.textSegments.isEmpty() ? "" : fetchText(xmlBytes, node.textSegments);
        }
        return node.text;
    }

    private void constructTree() throws XMLStreamException {
        final XMLStreamReader reader = newXMLStreamReader();
        //TODO: rewrite with LinkedList to avoid synchronization
        final Stack<Node> startedNodes = new Stack<>();
        //determines end of before selected element
        //used to cover next element with segment
        int beforeInstruction = -1;
        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    final Node newNode = new Node();
                    newNode.nodeStart = new Segment(beforeInstruction + 1, offset(reader));
                    newNode.name = reader.getLocalName();
                    newNode.attributes = fetchAttrs(reader);
                    //if new node is not xml root - set up relationships
                    if (!startedNodes.isEmpty()) {
                        connect(startedNodes.peek(), newNode);
                    }
                    nodesMap.put(newNode.path(), newNode);
                    startedNodes.push(newNode);
                    break;
                case END_ELEMENT:
                    startedNodes.pop().nodeEnd = new Segment(beforeInstruction + 1, offset(reader));
                    break;
                case CHARACTERS:
                    //TODO rework with text segments
                    final Node current = startedNodes.peek();
                    if (current.textSegments == null) {
                        current.textSegments = new ArrayList<>(3);
                    }
//                    current.textSegments.add(new Segment(beforeInstruction + 1, offset(reader)));
                    //FIXME: when segments will be ready remove this
                    current.text = reader.getText();
                    break;
            }
            beforeInstruction = offset(reader);
        }
    }

    //TODO
    private List<AttrNode> fetchAttrs(XMLStreamReader reader) {
        //There is no ability to fetch attributes like nodes
        //but each START_ELEMENT event of reader gives us ability
        //to fetch basic information about attributes such as names and values
        //so we need to provide mechanism of attributes fetching.
        //It can be done by analyzing segment of node start!
        return emptyList();
    }

    private void constructTreeQuietly() {
        try {
            constructTree();
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }

    private void connect(Node parent, Node child) {
        if (parent.children == null) {
            parent.children = new ArrayList<>();
        }
        parent.children.add(child);
        child.parent = parent;
    }

    /**
     * Returns reader offset
     * TODO: why -2? study this
     */
    private int offset(XMLStreamReader reader) {
        return reader.getLocation().getCharacterOffset() - 2;
    }

    private XMLStreamReader newXMLStreamReader() {
        try {
            return XML_INPUT_FACTORY.createXMLStreamReader(new ByteArrayInputStream(xmlBytes), "utf-8");
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }

    static class Node {
        Segment       nodeStart;
        Segment       nodeEnd;
        List<Segment> textSegments;

        String         name;
        String         text;
        Node           parent;
        List<Node>     children;
        List<AttrNode> attributes;

        //TODO move from node or add such methods as text, connect here instead of XMLTree
        String path() {
            if (parent == null) {
                return '/' + name;
            }
            return parent.path() + '/' + name;
        }
    }

    static class AttrNode {
        //TODO: here should be an attribute positions
        Node   owner;
        String name;
        String value;
    }

    /**
     * Used to remember positions of xml nodes and attributes.
     */
    static class Segment {
        final int start;
        final int end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}