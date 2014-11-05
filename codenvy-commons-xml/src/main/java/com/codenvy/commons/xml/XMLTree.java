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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static com.codenvy.commons.xml.Util.UTF_8;
import static com.codenvy.commons.xml.Util.getOnly;
import static com.codenvy.commons.xml.Util.getLevel;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * XML tool which provides abilities to modify and search
 * information in xml document without
 * affecting of existing formatting, comments.
 * <p/>
 * XMLTree uses {@link XMLStreamReader} to parse xml document
 * and {@link ListMultimap} to store parsed elements.
 * It is important to keep references on parsed elements in multimap!
 * It gives us ability to search and insert information very quick.
 *
 * @author Eugene Voevodin
 */
public final class XMLTree {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    /**
     * Creates XMLTree from input stream.
     * Doesn't close the stream
     */
    public static XMLTree from(InputStream is) throws IOException {
        return new XMLTree(ByteStreams.toByteArray(is));
    }

    /**
     * Creates XMLTree from file
     */
    public static XMLTree from(java.io.File file) throws IOException {
        return from(file.toPath());
    }

    /**
     * Creates XMLTree from path
     */
    public static XMLTree from(Path path) throws IOException {
        return new XMLTree(readAllBytes(path));
    }

    /**
     * Creates XMLTree from string
     */
    public static XMLTree from(String xml) {
        return new XMLTree(xml.getBytes(UTF_8));
    }

    private Element    root;
    private XPathLayer xPathLayer;

    byte[]                        xml;
    ListMultimap<String, Element> elements;

    private XMLTree(byte[] xml) {
        this.xml = xml;
        elements = ArrayListMultimap.create();
        constructTreeQuietly();
    }

    /**
     * Initialize and returns XPath layer for this tree.
     * You should use it only if you need xpath syntax,
     * otherwise use methods provided by tree. XPathLayer
     * going to be initialized only time, after each tree update
     * xpath layer going to be updated as well.
     *
     * @return xpath layer for this tree
     */
    public XPathLayer xpath() {
        if (xPathLayer == null) {
            xPathLayer = new XPathLayer(this);
        }
        return xPathLayer;
    }

    /**
     * Searches for element text.
     */
    public String getSingleText(String refElementPath) {
        return getOnly(elements.get(refElementPath)).getText();
    }

    /**
     * Searches for requested elements text.
     * If matched elements where not found empty list will be returned.
     *
     * @param path
     *         elements path
     * @return list of elements text or
     */
    public List<String> getText(String path) {
        final List<Element> elements = getElements(path);
        final List<String> elementsText = new ArrayList<>(elements.size());
        for (Element element : elements) {
            elementsText.add(element.getText());
        }
        return elementsText;
    }

    /**
     * Searches for elements by path
     *
     * @param path
     *         element path
     * @return list of found elements or empty list if no elements were found
     */
    public List<Element> getElements(String path) {
        return unmodifiableList(elements.get(path));
    }

    /**
     * Returns root element for tree
     */
    public Element getRootElement() {
        return root;
    }

    /**
     * Creates new element related to current tree
     * based on element name and text.
     * Newly created element will not be added to tree,
     * you should use exited elements update methods
     * or tree update methods to do so.
     *
     * @param name
     *         element name
     * @param text
     *         element text
     * @return created element
     */
    public Element newElement(String name, String text) {
        final Element newElement = new Element(this);
        newElement.text = text;
        newElement.name = name;
        return newElement;
    }

    /**
     * Creates new element related to current tree
     * based on given name and children elements.
     * Newly created element will not be added to tree,
     * you should use exited elements update methods
     * or tree update methods to do so.
     * After update each element child going to be added
     * to tree as well.
     *
     * @param name
     *         element name
     * @param children
     *         element children
     * @return created element
     */
    public Element newElement(String name, Element... children) {
        final Element newElement = new Element(this);
        newElement.name = name;
        newElement.children = new ArrayList<>(asList(children));
        return newElement;
    }

    /**
     * Updates text for element with given path.
     * </p>
     * Path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#setText(String)}} instead.
     *
     * @param refElementPath
     *         path to text container
     * @param newContent
     *         new text content
     */
    public void updateText(String refElementPath, String newContent) {
        getOnly(elements.get(refElementPath)).setText(newContent);
    }

    /**
     * Adds element to the end of the list of existed children.
     * <p/>
     * Parent path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#appendChild(Element)}} instead.
     *
     * @param refParentPath
     *         path to parent element
     * @param newElement
     *         element to add
     */
    public void appendChild(String refParentPath, Element newElement) {
        getOnly(getElements(refParentPath)).appendChild(newElement);
    }

    /**
     * Inserts element before referenced one.
     * After insert all commends related to referenced element
     * going to have the same order and position as it was before insert.
     * <p/>
     * Path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#insertBefore(Element)} instead.
     *
     * @param refElementPath
     *         path to element which will go after inserted element
     * @param newElement
     *         element to insert
     */
    public void insertBefore(String refElementPath, Element newElement) {
        getOnly(getElements(refElementPath)).insertBefore(newElement);
    }

    /**
     * Inserts element after referenced one.
     * After insert, all commends related to referenced element
     * going to have the same positions as it had before insert.
     * <p/>
     * Path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#insertAfter(Element)} instead.
     *
     * @param refElementPath
     *         path to element which going to be  before inserted element
     * @param newElement
     *         element to insert
     */
    public void insertAfter(String refElementPath, Element newElement) {
        getOnly(getElements(refElementPath)).insertAfter(newElement);
    }

    /**
     * TODO
     * Removes child with given name from referenced element.
     * If more then only child with the same name exists
     * {@link XMLTreeException} will be thrown.
     * <p/>
     * Path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#removeChild(String)} instead.
     *
     * @param refParentPath
     *         path to parent element
     * @param childName
     *         name of child which should be removed
     */
    public void removeChild(String refParentPath, String childName) {
        throw new XMLTreeException("Not implemented");
    }

    /**
     * TODO
     * Removes element with given path from tree.
     * <p/>
     * Path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#remove()} instead.
     *
     * @param refElementPath
     *         path to element
     */
    public void remove(String refElementPath) {
        throw new XMLTreeException("Not implemented");
    }

    /**
     * Uses {@link XMLStreamReader} to construct xml tree.
     * Tree stores each parsed element to {@link #elements} multi map.
     * It gives us ability to fetch elements very fast.
     * Each element contains start, end segments which are described
     * element positions in source array.
     */
    private void constructTree() throws XMLStreamException {
        final XMLStreamReader reader = newXMLStreamReader();
        //TODO: rewrite with LinkedList to avoid synchronization
        final Stack<Element> startedNodes = new Stack<>();
        //determines right of before selected element
        //used to cover next element with segment
        int beforeStart = -1;
        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    final Element newElement = new Element(this);
                    newElement.start = new Segment(beforeStart + 1, offset(reader));
                    newElement.name = reader.getLocalName();
                    fetchAttrs(newElement, reader);
                    //if new node is not xml root - set up relationships
                    if (!startedNodes.isEmpty()) {
                        newElement.setParent(startedNodes.peek());
                    } else {
                        root = newElement;
                    }
                    elements.put(newElement.path(), newElement);
                    startedNodes.push(newElement);
                    break;
                case END_ELEMENT:
                    startedNodes.pop().end = new Segment(beforeStart + 1, offset(reader));
                    break;
                case CHARACTERS:
                    final Element current = startedNodes.peek();
                    if (current.textSegments == null) {
                        //TODO think about list size
                        current.textSegments = new ArrayList<>();
                    }
                    //TODO REMOVE THIS
                    current.text = reader.getText();
                    current.textSegments.add(new Segment(beforeStart + 1, offset(reader)));
                    break;
            }
            beforeStart = offset(reader);
        }
    }

    //TODO do we need this method?
    public byte[] getBytes() {
        return Arrays.copyOf(xml, xml.length);
    }

    /**
     * Puts element and all of children to tree
     */
    private void putElement(Element element) {
        elements.put(element.path(), element);
        if (element.children != null) {
            for (Element child : element.children) {
                putElement(child);
            }
        }
    }

    /**
     * Creates new stream reader instance
     */
    private XMLStreamReader newXMLStreamReader() {
        try {
            //TODO
            return XML_INPUT_FACTORY.createXMLStreamReader(new ByteArrayInputStream(xml));
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }

    /**
     * Updates text of given element.
     * It is based on element text positions.
     * TODO: describe behaviour
     */
    //FIXME should work correctly when element contains other elements
    void updateText(Element target) {
        final int left;
        final int right;
        final List<Segment> segments = target.textSegments;
        //initialize borders
        if (segments != null) {
            left = segments.get(0).left;
            right = segments.get(segments.size() - 1).right;
        } else {
            left = target.start.right + 1;
            right = target.end.left - 1;
        }
        //insert new content into existed sources
        xml = Util.insertBetween(xml, left, right, target.text);
        //if xpath layer was initialized update it
        if (xPathLayer != null) {
            xPathLayer.updateText(target);
        }
    }

    /**
     * Inserts element before referenced one
     */
    void insertBefore(Element newElement, Element refElement) {
        final Element refAfter = refElement.getPreviousSibling();
        if (refAfter != null) {
            insertAfter(newElement, refAfter);
        } else {
            //inserting after parent
            xml = Util.insertInto(xml, refElement.parent.start.right + 1, '\n' + tabulate(newElement.asString(), getLevel(refElement)));
            if (xPathLayer != null) {
                xPathLayer.insertBefore(newElement, refElement);
            }
        }
    }

    /**
     * Inserts element after referenced one
     */
    void insertAfter(Element newElement, Element refElement) {
        xml = Util.insertInto(xml, refElement.end.right + 1, '\n' + tabulate(newElement.asString(), getLevel(refElement)));
        if (xPathLayer != null) {
            xPathLayer.insertAfter(newElement, refElement);
        }
        putElement(newElement);
    }

    /**
     * Fetches attributes from reader and inserts it to given element
     */
    private void fetchAttrs(Element element, XMLStreamReader reader) {
        //There is no ability to fetch attributes like nodes
        //but each START_ELEMENT event of reader gives us ability
        //to fetch basic information about attributes such as names and values
        //so we need to provide mechanism of attributes fetching.
        //It can be done by analyzing node start segment!
        int size = reader.getAttributeCount();
        for (int i = 0; i < size; i++) {
            //TODO analyze attribute positions
            element.addAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }
    }

    /**
     * Same as {@link #constructTree()}, only difference
     * that it wraps {@link XMLStreamException} to {@link XMLTreeException}
     */
    private void constructTreeQuietly() {
        try {
            constructTree();
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }

    /**
     * TODO rewrite
     * XMLStreamReader returns offset from source array start to start
     * of next element,  for all events instead its correct, instead of CHARACTERS event.
     * For characters event reader returns +1 or +2 to original offset, it depends on
     * next event type - if next type is END_ELEMENT then reader returns +2, if
     * next type is START_ELEMENT then reader returns +1.
     * This methods calculates offset from source array
     * start to current reader element event end [0, end].
     *
     * @param reader
     *         reader which is going to be used to detect offset
     * @return the right of current reader event
     */
    private int offset(XMLStreamReader reader) {
        final int offset = reader.getLocation().getCharacterOffset();
        if (reader.getEventType() != CHARACTERS) {
            return offset - 1;
        }
        return xml[offset - 1] == '<' ? offset - 2 : offset - 3;
    }
}