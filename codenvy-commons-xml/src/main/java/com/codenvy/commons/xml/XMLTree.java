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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static com.codenvy.commons.xml.Util.SPACES_IN_TAB;
import static com.codenvy.commons.xml.Util.UTF_8;
import static com.codenvy.commons.xml.Util.asElement;
import static com.codenvy.commons.xml.Util.asElements;
import static com.codenvy.commons.xml.Util.closeTagLength;
import static com.codenvy.commons.xml.Util.nextElementSibling;
import static com.codenvy.commons.xml.Util.single;
import static com.codenvy.commons.xml.Util.level;
import static com.codenvy.commons.xml.Util.insertBetween;
import static com.codenvy.commons.xml.Util.insertInto;
import static com.codenvy.commons.xml.Util.lastIndexOf;
import static com.codenvy.commons.xml.Util.openTagLength;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;
import static org.w3c.dom.Node.COMMENT_NODE;

/**
 * XML tool which provides abilities to modify and search
 * information in xml document without affecting of
 * existing formatting, comments.
 * <p/>
 * XMLTree delegates out of the box implementation of
 * org.w3c.dom and provides a lot of functionality
 * such as XPath selection.
 * How does the XMLTree lets content in required state?
 * The main idea is simple: know XML elements positions!
 * If we know elements positions and source bytes we
 * can easily manipulate content as we want.
 * So each time when client updates tree,
 * tree rewrites source bytes with new information,
 * indexes new elements, updates delegated document,
 * shifts needed existed elements positions.
 * As you may see there are a lot of data manipulations
 * when update is going, so <b>you should not use this tool for
 * parsing huge xml documents or for often complex updates.</b>
 * <p/>
 * XPath is embedded to XMLTree so each query to tree
 * is xpath query. You will be able to select/update
 * content provided with XMLTree elements or attributes
 * without working with xpath directly.
 * <p/>
 * XMLTree provides methods which do the same
 * as model methods but sometimes they are
 * more convenient, you can use tree
 * methods as well as model methods.
 * <pre>
 *     For example:
 *
 *     XMLTree tree = XMLTree.from(...)
 *
 *     //tree call
 *     tree.updateText("/project/name", "new name");
 *
 *     //model call
 *     tree.getSingleElement("/project/name")
 *         .setText("new name");
 *
 * </pre>
 * <b>NOTE: XMLTree is not thread-safe!</b>
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

    /**
     * Creates XMLTree from byte array
     */
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
     * Searches for requested element text.
     * If there are more then only element were found
     * {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search element
     * @return requested element text
     * @see Element#getText()
     */
    public String getSingleText(String expression) {
        return evaluateXPath(expression, STRING);
    }

    /**
     * Searches for requested elements text.
     * If there are no elements was found
     * empty list will be returned.
     * <p/>
     * You can use this method to request
     * not only elements text but for selecting
     * attributes values or whatever text information
     * which is able to be selected with xpath
     *
     * @param expression
     *         xpath expression to search elements
     * @return list of elements text or empty list if nothing found
     */
    public List<String> getText(String expression) {
        return retrieveText(expression);
    }

    /**
     * Searches for requested elements.
     *
     * @param expression
     *         xpath expression to search elements
     * @return list of found elements or empty list if elements were not found
     */
    public List<Element> getElements(String expression) {
        final NodeList nodes = evaluateXPath(expression, NODESET);
        return unmodifiableList(asElements(nodes));
    }

    /**
     * Returns root tree element
     */
    public Element getRoot() {
        return asElement(document.getDocumentElement());
    }

    /**
     * If there are more then only element
     * or nothing were found
     * {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search element
     * @return found element
     */
    public Element getSingleElement(String expression) {
        return single(getElements(expression));
    }

    /**
     * Creates new element related to current tree
     * based on element name and text.
     * Newly created element will not be added to tree,
     * you should use exited elements update methods
     * or tree update methods to do so.
     * <p/>
     * Created element related to tree instance,
     * so it is not able to update other tree instance
     * with it.
     *
     * @param name
     *         element name
     * @param text
     *         element text
     * @return created element with given name and text content
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
     * Each tree update method will add related to
     * element children as well as itself.
     * <p/>
     * Created element related to tree instance,
     * so it is not able to update other tree instance
     * with it.
     *
     * @param name
     *         element name
     * @param children
     *         element children
     * @return created element with given name and children
     */
    public Element newElement(String name, Element... children) {
        final Element newElement = new Element(this);
        newElement.name = name;
        newElement.children = new ArrayList<>(asList(children));
        return newElement;
    }

    /**
     * Updates requested element text.
     * XPath expression should be used only
     * for element not for attribute or something else.
     * </p>
     * If there are more then only element were found
     * {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search element
     * @param newContent
     *         new element text content
     * @see Element#setText(String)
     */
    public void updateText(String expression, String newContent) {
        getSingleElement(expression).setText(newContent);
    }

    /**
     * Adds element to the end of the list
     * of existed children or adds it as only children.
     * <p/>
     * If there are more then only parent elements
     * were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search parent
     * @param newElement
     *         new element which will be inserted.
     *         It should be created with same tree instance
     */
    public void appendChild(String expression, Element newElement) {
        single(getElements(expression)).appendChild(newElement);
    }

    /**
     * Inserts element before referenced one.
     * All comments related before referenced element
     * going to have same positions like they had before.
     * <p/>
     * If there are more then only referenced elements
     * were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search referenced element
     * @param newElement
     *         new element which will be inserted.
     *         It should be created with same tree instance
     */
    public void insertBefore(String expression, Element newElement) {
        single(getElements(expression)).insertBefore(newElement);
    }

    /**
     * Inserts element after referenced one.
     * <p/>
     * If there are more then only referenced elements
     * were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search referenced element
     * @param newElement
     *         new element which will be inserted.
     *         It should be created with same tree instance
     */
    public void insertAfter(String expression, Element newElement) {
        single(getElements(expression)).insertAfter(newElement);
    }

    /**
     * Removes requested element.
     * If there are was any <b>text</b> before removal
     * element it will be removed as well.
     * It is important when we need to keep formatting
     * pretty if it was pretty. It is really strange
     * situation when parent element contains
     * not only whitespaces but another text content.
     * <p/>
     * If there are more then only referenced element
     * were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to remove element
     */
    public void removeElement(String expression) {
        single(getElements(expression)).remove();
    }

    /**
     * Returns copy of tree bytes.
     */
    public byte[] getBytes() {
        return Arrays.copyOf(xml, xml.length);
    }

    private void shiftSegment(Segment segment, int idx, int offset) {
        if (segment.left > idx) {
            segment.left += offset;
            segment.right += offset;
        }
    }

    private void removeTextSegment(Element element, int left) {
        for (Iterator<Segment> segIt = element.textSegments.iterator(); segIt.hasNext(); ) {
            if (segIt.next().left == left) {
                segIt.remove();
                break;
            }
        }
    }

    private void shiftSegments(int fromIdx, int offset) {
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

    private List<String> retrieveText(String expression) {
        final NodeList nodeList = evaluateXPath(expression, NODESET);
        final List<String> elementsText = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            elementsText.add(nodeList.item(i).getTextContent());
        }
        return elementsText;
    }

    //TODO add attributes
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
        //TODO change this value
        int beforeStart = 38;
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
                case CHARACTERS:
                    final Element current = startedNodes.peek();
                    if (current.textSegments == null) {
                        //TODO think about list size
                        current.textSegments = new LinkedList<>();
                    }
                    //TODO REMOVE THIS
                    current.textSegments.add(new Segment(beforeStart + 1, offset(reader)));
                case COMMENT_NODE:
                    //TODO add CDATA?!
                case COMMENT:
                    node = deepNext(node, true);
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
            return XML_INPUT_FACTORY.createXMLStreamReader(new ByteArrayInputStream(xml));
        } catch (XMLStreamException xmlEx) {
            throw XMLTreeException.wrap(xmlEx);
        }
    }

    /**
     * Removes element from tree.
     * <p/>
     * It is important to save xml tree pretty view,
     * so element should be removed without of destroying
     * style of xml document.
     * <pre>
     *      e.g.
     *
     *      {@literal <level1>}
     *          {@literal <level2>} {@literal <level2>+\n}
     *          {@literal <level2>} {@literal <level2+>\n}
     *      {@literal <level1>}
     *
     *      first + is before left border
     *      last + is before right border
     *
     *      segment [first, last] - will be removed
     * </pre>
     * So after removing formatting will be the same.
     * We can't remove just element from start to end
     * because it will produce not pretty formatting for
     * good and pretty formatted before document.
     */
    void dropElement(Element element) {
        final int left = lastIndexOf(xml, '>', element.start.left) + 1;
        final int len = xml.length;
        if (left != element.start.left - 1) {
            removeTextSegment(element.getParent(), left);
        }
        xml = insertBetween(xml, left, element.end.right, "");
        elements.remove(element);
        //shift all elements which are right from element
        shiftSegments(element.end.right, xml.length - len);
    }

    /**
     * Updates text of given element.
     * It is based on element text positions.
     * If element has more then one text segment
     * only first will be used for update, other
     * will be removed. Only text segment will have the same left bound as it
     * has before and right bound equal to left bound + new text length
     */
    void updateText(Element target) {
        if (target.textSegments != null) {
            final Iterator<Segment> segIt = target.textSegments.iterator();
            final Segment first = segIt.next();
            //removing all segments instead of first
            while (segIt.hasNext()) {
                final Segment segment = segIt.next();
                segIt.remove();
                xml = insertBetween(xml, segment.left, segment.right, "");
            }
            //update new text content
            xml = insertBetween(xml, first.left, first.right, target.getText());
            first.right = first.left + target.getText().length();
        } else {
            xml = insertBetween(xml, target.start.right + 1, target.end.left - 1, target.getText());
        }
    }

    /**
     * Inserts element before referenced one.
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
            int len = xml.length;
            //inserting after parent
            final Element parent = refElement.getParent();

            xml = insertInto(xml, parent.start.right + 1, '\n' + tabulate(newElement.asString(), level(refElement)));
            index(newElement, parent.start.right + 1, level(refElement), parent);
            shiftSegments(parent.start.right, xml.length - len);

            insertBeforeNode(newElement, refElement);
        }
    }

    /**
     * Inserts element after referenced one
     */
    void insertAfter(Element newElement, Element refElement) {
        final int level = level(refElement);
        final int len = xml.length;

        xml = insertInto(xml, refElement.end.right + 1, '\n' + tabulate(newElement.asString(), level));
        //TODO: write explanation! +1 cause of \n
        index(newElement, refElement.end.right + 1, level, refElement.getParent());
        shiftSegments(refElement.end.right, xml.length - len);

        insertAfterNode(newElement, refElement);
    }

    /**
     * Adds new element to the end of children list with given parent.
     */
    void appendChild(Element newElement, Element parent) {
        final int level = level(parent) + 1;
        final int len = xml.length;
        final int pos = lastIndexOf(xml, '>', parent.end.left) + 1;

        xml = insertInto(xml, pos, '\n' + tabulate(newElement.asString(), level));
        index(newElement, pos, level, parent);
        shiftSegments(parent.end.right, xml.length - len);

        parent.delegate.appendChild(createNode(newElement));
    }

    /**
     * Creates segments for newly created element and related children
     */
    private int index(Element element, int beforeText, int level, Element parent) {
        //text length before element
        // child - element, '+' - text before element
        //        ++++++
        //<parent>\n    <child>...
        final int lineTextLength = level * SPACES_IN_TAB;

        //before element open tag pos
        //             +
        //<parent>\n    <child>...
        final int beforeStart = beforeText + lineTextLength;

        //we should add text segment which
        //is before given element to parent
        if (parent.textSegments == null) {
            parent.textSegments = new LinkedList<>();
        }
        parent.textSegments.add(new Segment(beforeText, beforeStart));

        //pos of open tag right '>'
        int startRight = beforeStart + openTagLength(element);

        element.start = new Segment(beforeStart + 1, startRight);
        //if element has children it doesn't have text instead of
        //whitespaces, so all what we need - detect element close tag segment
        //to do so we need to index element children first
        int beforeTextForChild = startRight;// 1 - \n
        if (element.children != null) {
            for (Element child : element.children) {
                beforeTextForChild = index(child, beforeTextForChild + 1, level + 1, element);
            }
            //after last element we need to add 1 more text segment
            beforeTextForChild += 1 + lineTextLength;
        } else {
            element.textSegments = new LinkedList<>();
        }
        //before element close tag pos
        //                        +
        //<parent>\n    <child>text</child>

        int beforeEnd = beforeTextForChild + element.getText().length();
        element.textSegments.add(new Segment(startRight + 1, beforeEnd));
        element.end = new Segment(beforeEnd + 1, beforeEnd + closeTagLength(element));
        return element.end.right;
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

    private void insertBeforeNode(Element newElement, Element refElement) {
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

    /**
     * Describes element, attribute or text position in
     * the source array of bytes.
     */
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