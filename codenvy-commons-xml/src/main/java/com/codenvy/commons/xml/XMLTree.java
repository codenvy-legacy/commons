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


import com.google.common.io.ByteStreams;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static com.codenvy.commons.xml.Util.SPACES_IN_TAB;
import static com.codenvy.commons.xml.Util.UTF_8;
import static com.codenvy.commons.xml.Util.asElement;
import static com.codenvy.commons.xml.Util.closeTagLength;
import static com.codenvy.commons.xml.Util.nextElementSibling;
import static com.codenvy.commons.xml.Util.single;
import static com.codenvy.commons.xml.Util.getLevel;
import static com.codenvy.commons.xml.Util.insertBetween;
import static com.codenvy.commons.xml.Util.insertInto;
import static com.codenvy.commons.xml.Util.nearestLeftIndexOf;
import static com.codenvy.commons.xml.Util.openTagLength;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;
import static org.w3c.dom.Node.COMMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * TODO:
 * add support for attributes
 * add support for one line elements
 * add support for deep indexing newly added elements
 * add checks e.g. "should not be able to remove root"
 * text update should use only segment and remove others
 * test segments positions after batch updates
 * <p/>
 * XML tool which provides abilities to modify and search
 * information in xml document without
 * affecting of existing formatting, comments.
 * <p/>
 *
 * @author Eugene Voevodin
 */
public final class XMLTree {

    private static final XMLInputFactory        XML_INPUT_FACTORY        = XMLInputFactory.newFactory();
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private static final XPathFactory           XPATH_FACTORY            = XPathFactory.newInstance();

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

    public static XMLTree from(byte[] xml) {
        return new XMLTree(xml);
    }

    private Document     document;
    private Set<Element> elements;
    private byte[]       xml;

    private XMLTree(byte[] xml) {
        this.xml = xml;
        elements = new HashSet<>();
        document = parseQuietly(xml);
        constructTreeQuietly();
    }

    /**
     * Searches for element text.
     */
    public String getSingleText(String expression) {
        return evaluateXPath(expression, STRING);
    }

    /**
     * Searches for requested elements text.
     * If matched elements where not found empty list will be returned.
     *
     * @param expression
     *         elements path
     * @return list of elements text or
     */
    public List<String> getText(String expression) {
        return retrieveText(expression);
    }


    /**
     * Searches for elements by path
     *
     * @param expression
     *         element path
     * @return list of found elements or empty list if no elements were found
     */
    public List<Element> getElements(String expression) {
        return unmodifiableList(retrieveElements(expression));
    }

    /**
     * Returns root element for tree
     */
    public Element getRoot() {
        return asElement(document.getDocumentElement());
    }

    public Element getSingleElement(String expression) {
        return single(getElements(expression));
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
     * @param expression
     *         path to text container
     * @param newContent
     *         new text content
     */
    public void updateText(String expression, String newContent) {
        getSingleElement(expression).setText(newContent);
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
        single(getElements(refParentPath)).appendChild(newElement);
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
        single(getElements(refElementPath)).insertBefore(newElement);
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
        single(getElements(refElementPath)).insertAfter(newElement);
    }

    /**
     * TODO
     * <p/>
     * Path should be unique, if is not so {@link XMLTreeException} will
     * be thrown. If wanted element hasn't unique path you
     * should use {@link Element#remove()} instead.
     *
     * @param expression
     *         path to element
     */
    public void removeElement(String expression) {
        single(getElements(expression)).remove();
    }

    public byte[] getBytes() {
        return Arrays.copyOf(xml, xml.length);
    }

    void dropElement(Element element) {
        final int left = nearestLeftIndexOf(xml, '>', element.start.left) + 1;
        final int len = xml.length;
        if (left != element.start.left - 1) {
            removeTextSegment(element.getParent(), left);
        }
        xml = insertBetween(xml, left, element.end.right, "");
        dropNodeFromDocument(element);
        elements.remove(element);
        //shift all elements which are right from element
        shiftElements(element.end.right, -len + xml.length);
    }

    private void dropNodeFromDocument(Element element) {
        element.delegate.getParentNode().removeChild(element.delegate);
    }

    private void removeTextSegment(Element element, int left) {
        for (Iterator<Segment> segIt = element.textSegments.iterator(); segIt.hasNext(); ) {
            if (segIt.next().left == left) {
                segIt.remove();
                break;
            }
        }
    }

    private void shiftElements(int fromIdx, int offset) {
        for (Element element : elements) {
            if (element.end.left > fromIdx) {
                shiftSegment(element.start, fromIdx, offset);
                shiftSegment(element.end, fromIdx, offset);
                if (element.textSegments != null) {
                    for (Segment textSegment : element.textSegments) {
                        shiftSegment(textSegment, fromIdx, offset);
                    }
                }
                //TODO shift attributes
            }
        }
    }

    void shiftSegment(Segment segment, int idx, int offset) {
        if (segment.left > idx) {
            segment.left += offset;
            segment.right += offset;
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
        xml = insertBetween(xml, left, right, target.getText());
    }

    @SuppressWarnings("unchecked")
    private <T> T evaluateXPath(String expression, QName returnType) {
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

    //TODO find better name
    private List<String> retrieveText(String expression) {
        final NodeList nodeList = evaluateXPath(expression, NODESET);
        final List<String> elementsText = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            elementsText.add(nodeList.item(i).getTextContent());
        }
        return elementsText;
    }

    private List<Element> retrieveElements(String expression) {
        final NodeList nodeList = evaluateXPath(expression, NODESET);
        final List<Element> requested = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node current = nodeList.item(i);
            if (current.getNodeType() == ELEMENT_NODE) {
                requested.add((Element)current.getUserData("element"));
            }
        }
        return requested;
    }

    private Node createNode(Element element) {
        final Node newNode = document.createElement(element.getName());
        newNode.setTextContent(element.getText());
        newNode.setUserData("element", element, null);
        element.delegate = newNode;
        if (element.children != null) {
            for (Element child : element.children) {
                newNode.appendChild(createNode(child));
            }
        }
        return newNode;
    }

    private void constructTree() throws XMLStreamException {
        Node node = document.getDocumentElement();

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
                        node = deepNext(node, true);
                    }
                    startedNodes.push(newElement);
                    node.setUserData("element", newElement, null);
                    newElement.delegate = node;
                    break;
                case END_ELEMENT:
                    final Element element = startedNodes.pop();
                    element.end = new Segment(beforeStart + 1, offset(reader));
                    elements.add(element);
                    break;
                case COMMENT_NODE:
                case COMMENT:
                    node = deepNext(node, true);
                    break;
                case CHARACTERS:
                    node = deepNext(node, true);
                    final Element current = startedNodes.peek();
                    if (current.textSegments == null) {
                        //TODO think about list size
                        current.textSegments = new ArrayList<>();
                    }
                    //TODO REMOVE THIS
                    current.textSegments.add(new Segment(beforeStart + 1, offset(reader)));
                    break;
            }
            beforeStart = offset(reader);
        }
    }

    private Node deepNext(Node node, boolean deep) {
        if (deep && node.getChildNodes().getLength() != 0) {
            return node.getFirstChild();
        }
        final Node next = node.getNextSibling();
        if (next != null) {
            return next;
        } else if (node == document.getDocumentElement()) {
            return node;
        }
        return deepNext(node.getParentNode(), false);
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
     * It is important to let all related to
     * {@param refElement} comments on their places,
     * so to avoid deformation we inserting new element after
     * previous sibling or after element parent
     * if element doesn't have previous sibling
     */
    void insertBefore(Element newElement, Element refElement) {
        final Element refPrevious = refElement.getPreviousSibling();
        if (refPrevious != null) {
            insertAfter(newElement, refPrevious);
        } else {
            //inserting after parent
            xml = insertInto(xml, refElement.getParent().start.right + 1, '\n' + tabulate(newElement.asString(), getLevel(refElement)));
            documentInsertBefore(newElement, refElement);
        }
    }

    /**
     * Inserts element after referenced one
     */
    void insertAfter(Element newElement, Element refElement) {
        int level = getLevel(refElement);
        int len = xml.length;
        xml = insertInto(xml, refElement.end.right + 1, '\n' + tabulate(newElement.asString(), level));

        index(newElement, refElement.end.right, level, refElement.getParent().textSegments);
        shiftElements(refElement.end.right, xml.length - len);

        insertAfterNode(newElement, refElement);
    }

    /**
     * TODO for children
     */
    void index(Element element, int startIdx, int level, List<Segment> textAccumulator) {
        int textLength = level + SPACES_IN_TAB;
        int beforeStart = startIdx + textLength;
        int right = beforeStart + openTagLength(element);
        element.start = new Segment(beforeStart + 1, right);
        if (element.children != null) {
            //TODO
        } else {
            int beforeEnd = right + element.getText().length();
            element.textSegments = singletonList(new Segment(right + 1, beforeEnd));
            element.end = new Segment(beforeEnd + 1, beforeEnd + closeTagLength(element));
        }
    }

    /**
     * Inserting new node before next sibling
     * appending new child if refElement doesn't have
     */
    private void insertAfterNode(Element newElement, Element refElement) {
        final Node nextSibling = nextElementSibling(refElement.delegate);
        if (nextSibling != null) {
            nextSibling.getParentNode().insertBefore(createNode(newElement), nextSibling);
        } else {
            refElement.delegate.getParentNode().appendChild(createNode(newElement));
        }
    }

    private void documentInsertBefore(Element newElement, Element refElement) {
        final Node refNode = refElement.delegate;
        refNode.getParentNode().insertBefore(createNode(newElement), refNode);
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

    static class Segment {
        int left;
        int right;

        Segment(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Segment)) {
                return false;
            }
            final Segment other = (Segment)obj;
            return other.left == left && other.right == right;
        }

        @Override
        public int hashCode() {
            return 31 * left ^ 31 * right;
        }

        @Override
        public String toString() {
            return "left: " + left + ", right: " + right;
        }
    }
}