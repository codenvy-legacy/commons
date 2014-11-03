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
import com.google.common.io.CharStreams;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.codenvy.commons.xml.Util.getOnly;
import static com.codenvy.commons.xml.Util.insert;
import static java.util.Collections.unmodifiableList;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * TODO: add update methods
 * use batch update of xml array or one update per one operation?
 *
 * @author Eugene Voevodin
 */
public final class XMLTree {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    /**
     * Creates XMLTree from input stream.
     * Doesn't close the stream.
     */
    public static XMLTree from(Reader reader) throws IOException {
        //FIXME
        //use this reader directly with XMLStreamReader
        //make reader read chars only time
        //and then get it?
        return new XMLTree(CharStreams.toString(reader).toCharArray());
    }

    public static XMLTree from(char[] xml) {
        return new XMLTree(xml);
    }

    private XPathLayer xPathLayer;

    char[]                        xml;
    ListMultimap<String, Element> elements;

    private XMLTree(char[] xml) {
        this.xml = xml;
        elements = ArrayListMultimap.create();
        constructTreeQuietly();
    }

    /**
     * Searches for element eachText
     */
    public String singleText(String query) {
        return getOnly(elements.get(query)).getText();
    }

    public List<String> eachText(String query) {
        final List<Element> elements = elements(query);
        final List<String> elementsText = new ArrayList<>(elements.size());
        for (Element element : elements) {
            elementsText.add(element.getText());
        }
        return elementsText;
    }

    /**
     * Searches for elements
     * TODO add an example of path
     *
     * @param path
     *         element path
     * @return list of found elements or empty list if no elements were found
     */
    public List<Element> elements(String path) {
        return unmodifiableList(elements.get(path));
    }

    public XPathLayer xpath() {
        if (xPathLayer == null) {
            xPathLayer = new XPathLayer(this);
        }
        return xPathLayer;
    }

    //TODO make public method for update
    void updateText(Element target) {
        int left;
        int right;
        final List<Segment> segments = target.textSegments;
        if (segments != null) {
            left = segments.get(0).left;
            right = segments.get(segments.size() - 1).right;
        } else {
            left = target.nodeStart.right + 1;
            right = target.nodeEnd.left;
        }
        xml = insert(xml, left, right, target.text);
        if (xPathLayer != null) {
            xPathLayer.updateText(target);
        }
    }

    //methods based on string paths

    public void updateText(String elementPath, String newContent) {
        getOnly(elements.get(elementPath)).setText(newContent);
    }

    public void appendChild(String parentPath, Element newElement) {
        throw new XMLTreeException("Not implemented");
    }

    public void insertBefore(String beforeElementPath, Element newElement) {
        throw new XMLTreeException("Not implemented");
    }

    public void insertAfter(String afterElementPath, Element newElement) {
        throw new XMLTreeException("Not implemented");
    }

    //FIXME do we need such method?
    public void insertOrdered(String parentPath, Element newElement) {
        throw new XMLTreeException("Not implemented");
    }

    public void delete(String path) {
        throw new XMLTreeException("Not implemented");
    }

    private void constructTree() throws XMLStreamException {
        final XMLStreamReader reader = newXMLStreamReader();
        //TODO: rewrite with LinkedList to avoid synchronization
        final Stack<Element> startedNodes = new Stack<>();
        //determines right of before selected element
        //used to cover next element with segment
        int start = -1;
        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    final Element newElement = new Element(this);
                    newElement.nodeStart = new Segment(start, offset(reader));
                    newElement.name = reader.getLocalName();
                    fetchAttrs(newElement, reader);
                    //if new node is not xml root - set up relationships
                    if (!startedNodes.isEmpty()) {
                        newElement.setParent(startedNodes.peek());
                    }
                    elements.put(newElement.path(), newElement);
                    startedNodes.push(newElement);
                    break;
                case END_ELEMENT:
                    startedNodes.pop().nodeEnd = new Segment(start, offset(reader));
                    break;
                case CHARACTERS:
                    final Element current = startedNodes.peek();
                    if (current.textSegments == null) {
                        //TODO think about list size
                        current.textSegments = new ArrayList<>();
                    }
                    current.textSegments.add(new Segment(start, offset(reader) - 1));
                    break;
            }
            start = offset(reader);
        }
    }

    //TODO
    private void fetchAttrs(Element element, XMLStreamReader reader) {
        //There is no ability to fetch attributes like nodes
        //but each START_ELEMENT event of reader gives us ability
        //to fetch basic information about attributes such as names and values
        //so we need to provide mechanism of attributes fetching.
        //It can be done by analyzing segment of node left!
        int count = reader.getAttributeCount();
        final ArrayList<Attribute> attributes = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            //TODO analyze attribute positions
//            final Attribute newAttr = new Attribute();
//            newAttr.name = reader.getAttributeLocalName(i);
//            newAttr.owner = element;
//            newAttr.value = reader.getAttributeValue(i);
        }
//        element.attributes = attrNodes;
        element.attributes = attributes;
    }

    private void constructTreeQuietly() {
        try {
            constructTree();
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }

    /**
     * TODO rewrite
     * XMLStream reader returns nice offset for all events instead of CHARACTERS event.
     * For characters event reader returns +1 or +2 to original offset, it depends on
     * next event type - if next type is END_ELEMENT then reader returns +2, if
     * next type is START_ELEMENT then reader returns +1.
     *
     * @param reader
     *         reader which is going to be used to detect offset
     * @return the right of current reader event
     */
    private int offset(XMLStreamReader reader) {
        final int offset = reader.getLocation().getCharacterOffset();
        if (reader.getEventType() != CHARACTERS) {
            return offset;
        }
        return xml[offset - 1] == '<' ? offset - 1 : offset - 2;
    }

    private XMLStreamReader newXMLStreamReader() {
        try {
            //TODO
            return XML_INPUT_FACTORY.createXMLStreamReader(new CharArrayReader(xml));
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }
}