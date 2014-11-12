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


import com.google.common.collect.Sets;
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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
import static com.codenvy.commons.xml.Util.indexOf;
import static com.codenvy.commons.xml.Util.single;
import static com.codenvy.commons.xml.Util.level;
import static com.codenvy.commons.xml.Util.insertBetween;
import static com.codenvy.commons.xml.Util.insertInto;
import static com.codenvy.commons.xml.Util.lastIndexOf;
import static com.codenvy.commons.xml.Util.openTagLength;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.lang.Math.abs;
import static java.nio.file.Files.readAllBytes;
import static java.util.Collections.unmodifiableList;
import static javax.xml.stream.XMLStreamConstants.CDATA;
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
 * How does the XMLTree let content in required state?
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
 * as model methods but sometimes they are more convenient,
 * you can use tree methods as well as model methods.
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
    private static final String                 ROOT_TEMPLATE            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<%s>\n</%s>";

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


    /**
     * Creates XMLTree with given root element
     */
    public static XMLTree create(String rootName) {
        return from(String.format(ROOT_TEMPLATE, rootName, rootName));
    }

    private Document     document;
    private Set<Element> elements;
    private byte[]       xml;

    private XMLTree(byte[] xml) {
        this.xml = xml;
        elements = Sets.newIdentityHashSet();
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
     * If there are no elements were found
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
     * or nothing were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search element
     * @return found element
     */
    public Element getSingleElement(String expression) {
        return single(getElements(expression));
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
     * of existed children or adds it as only child.
     * <p/>
     * If there are more then only parent element
     * were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search parent
     * @param newElement
     *         new element which will be inserted.
     *         It should be created with same tree instance
     */
    public void appendChild(String expression, NewElement newElement) {
        single(getElements(expression)).appendChild(newElement);
    }

    /**
     * Inserts element before referenced one.
     * All comments related before referenced element
     * going to have same positions like they had before.
     * <p/>
     * If there are more then only referenced element
     * were found {@link XMLTreeException} will be thrown
     *
     * @param expression
     *         xpath expression to search referenced element
     * @param newElement
     *         new element which will be inserted.
     *         It should be created with same tree instance
     */
    public void insertBefore(String expression, NewElement newElement) {
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
    public void insertAfter(String expression, NewElement newElement) {
        single(getElements(expression)).insertAfter(newElement);
    }

    /**
     * Removes requested element.
     * If there are was any <b>text</b> before removal
     * element it will be removed as well.
     * It is important when we need to keep formatting
     * pretty - if it was pretty. It is really strange
     * when parent element contains not only whitespaces
     * but another text content.
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
     * Returns copy of source bytes.
     */
    public byte[] getBytes() {
        return Arrays.copyOf(xml, xml.length);
    }

    /**
     * Writes copy of source bytes to output stream.
     * Doesn't close the stream
     */
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(getBytes());
    }

    /**
     * Writes source bytes to path
     */
    public void writeTo(Path path) throws IOException {
        Files.write(path, xml);
    }

    /**
     * Writes source bytes to file
     */
    public void writeTo(java.io.File file) throws IOException {
        Files.write(file.toPath(), xml);
    }

    private void shiftSegment(Segment segment, int idx, int offset) {
        if (segment.left > idx) {
            segment.left += offset;
            segment.right += offset;
        }
    }

    private void removeTextSegment(Element element, int left) {
        for (Iterator<Segment> segIt = element.text.iterator(); segIt.hasNext(); ) {
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
                if (element.text != null) {
                    for (Segment textSegment : element.text) {
                        shiftSegment(textSegment, fromIdx, offset);
                    }
                }
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

    private void constructTree() throws XMLStreamException {
        Node node = document.getDocumentElement();

        final XMLStreamReader reader = newXMLStreamReader();
        //TODO: rewrite with LinkedList to avoid synchronization
        final Stack<Element> startedElements = new Stack<>();
        //determines right of before selected element
        //used to cover next element with segment
        //TODO change this value
        int beforeStart = 38;
        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    final Element newElement = new Element(this);
                    newElement.start = new Segment(beforeStart + 1, offset(reader));
                    //if new node is not xml root - set up relationships
                    if (!startedElements.isEmpty()) {
                        node = deepNext(node, true);
                    }
                    startedElements.push(newElement);
                    node.setUserData("element", newElement, null);
                    newElement.delegate = node;
                    break;
                case END_ELEMENT:
                    final Element element = startedElements.pop();
                    element.end = new Segment(beforeStart + 1, offset(reader));
                    elements.add(element);
                    break;
                case CHARACTERS:
                    final Element current = startedElements.peek();
                    if (current.text == null) {
                        //TODO think about list size
                        current.text = new LinkedList<>();
                    }
                    current.text.add(new Segment(beforeStart + 1, offset(reader)));
                case COMMENT_NODE:
                case CDATA:
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
     * TODO write explanation
     * <p/>
     * Updates text of given element.
     * It is based on element text positions.
     * If element has more then one text segment
     * only first will be used for update, other
     * will be removed. Only text segment will have the same left bound as it
     * has before and right bound equal to left bound + new text length
     */
    void updateText(Element target) {
        final int len = xml.length;
        if (target.text != null) {
            final Iterator<Segment> segIt = target.text.iterator();
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
            target.text = new LinkedList<>();
            target.text.add(new Segment(target.start.right + 1, target.end.left - 1));
        }
        shiftSegments(target.start.right, abs(len - xml.length));
    }

    void updateAttributeValue(Attribute attribute, String oldValue) {
        final int len = xml.length;
        final Segment segment = valueSegment(attribute, oldValue);
        //replacing attribute value content with new content
        xml = insertBetween(xml,
                            segment.left,
                            segment.right,
                            attribute.getValue());
        //shift existing segments which are after owner start left
        //TODO fix offset
        shiftSegments(attribute.getElement().start.left, abs(len - xml.length));
    }

    /**
     * Adds new element to the end of children list with given parent.
     */
    void appendChild(NewElement newElement, Element relatedToNew, Element parent) {
        final int level = level(parent) + 1;
        final int lengthBefore = xml.length;
        final int insertHere = lastIndexOf(xml, '>', parent.end.left) + 1;
        //inserting new element bytes to tree bytes
        xml = insertInto(xml,
                         insertHere,
                         '\n' + tabulate(newElement.asString(), level));
        //shift existing segments which are after parent start
        shiftSegments(parent.start.right, xml.length - lengthBefore);
        //create and set up start, end, text segments to created element
        applySegments(newElement, relatedToNew, insertHere, level, parent);
        //let tree know about added element
        registerElement(relatedToNew);
    }

    /**
     * Inserts element after referenced one
     */
    void insertAfter(NewElement newElement, Element relatedToNew, Element refElement) {
        final int level = level(refElement);
        final int lengthBefore = xml.length;
        //inserting new element bytes to tree bytes
        xml = insertInto(xml,
                         refElement.end.right + 1,
                         '\n' + tabulate(newElement.asString(), level));
        //shift existing segments which are after parent start
        shiftSegments(refElement.end.right, xml.length - lengthBefore);
        //create and set up start, end, text segments to created element
        //+1 because of \n
        applySegments(newElement, relatedToNew, refElement.end.right + 1, level, refElement.getParent());
        //let tree know about inserted element
        registerElement(relatedToNew);
    }

    /**
     * Inserts element before referenced one.
     * It is important to let all related to
     * {@param refElement} comments on their places,
     * so to avoid deformation we inserting new element after
     * previous sibling or after element parent
     * if element doesn't have previous sibling
     */
    void insertAfterParent(NewElement newElement, Element relatedToNew, Element parent) {
        final int level = level(parent) + 1;
        final int lengthBefore = xml.length;
        //inserting after parent
        xml = insertInto(xml,
                         parent.start.right + 1,
                         '\n' + tabulate(newElement.asString(), level));
        //shift existing segments which are after parent start
        shiftSegments(parent.start.right, xml.length - lengthBefore);
        //create and set up start, end, text segments to created element
        applySegments(newElement, relatedToNew, parent.start.right + 1, level, parent);
        //let tree know about inserted element
        registerElement(relatedToNew);
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
    void removeElement(Element element) {
        final int leftBound = lastIndexOf(xml, '>', element.start.left) + 1;
        final int lengthBefore = xml.length;
        //if text segment before removal element
        //exists it should go to hell with removal
        if (leftBound != element.start.left - 1) {
            removeTextSegment(element.getParent(), leftBound);
        }
        //replacing content with nothing
        xml = insertBetween(xml,
                            leftBound,
                            element.end.right,
                            "");
        //shift all elements which are right from removed element
        shiftSegments(element.end.right, xml.length - lengthBefore);
        //let tree know that element is not a family member
        unregisterElement(element);
    }

    void insertAttribute(NewAttribute attribute, Element owner) {
        final int len = xml.length;
        //inserting new attribute content
        xml = insertInto(xml,
                         owner.start.right,
                         ' ' + attribute.asString());
        //shift all elements which are right from removed element
        shiftSegments(owner.start.left - 1, xml.length - len);
    }

    void removeAttribute(Attribute attribute) {
        final Element element = attribute.getElement();
        final int len = xml.length;
        final Segment segment = attributeSegment(attribute);
        //replacing attribute segment with nothing
        xml = insertBetween(xml,
                            segment.left - 1,
                            segment.right,
                            "");
        //shift all elements which are left from owner left
        shiftSegments(element.start.left, len - xml.length);
    }

    private void registerElement(Element element) {
        elements.add(element);
        for (Element child : element.getChildren()) {
            registerElement(child);
        }
    }

    private void unregisterElement(Element element) {
        elements.remove(element);
        for (Element child : element.getChildren()) {
            unregisterElement(child);
        }
    }

    private Segment attributeSegment(Attribute attribute) {
        final Element owner = attribute.getElement();

        final byte[] name = attribute.getName().getBytes();
        final byte[] value = attribute.getValue().getBytes();

        final int start = indexOf(xml, name, owner.start.left + owner.getName().length());
        final int valueStart = indexOf(xml, value, start + name.length);

        return new Segment(start, valueStart + value.length);
    }

    private Segment valueSegment(Attribute attribute, String oldValue) {
        final Element owner = attribute.getElement();

        final byte[] name = attribute.getName().getBytes();
        final byte[] value = oldValue.getBytes();

        final int start = indexOf(xml, name, owner.start.left + owner.getName().length());
        final int valueStart = indexOf(xml, value, start + name.length);

        return new Segment(valueStart, valueStart + value.length - 1);
    }

    /**
     * Creates segments for newly created element and related children
     */
    int applySegments(NewElement newElement, Element relatedToNew, int beforeText, int level, Element parent) {
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
        if (parent.text == null) {
            parent.text = new LinkedList<>();
        }
        parent.text.add(new Segment(beforeText, beforeStart));

        //pos of open tag right '>'
        int startRight = beforeStart + openTagLength(newElement);

        relatedToNew.start = new Segment(beforeStart + 1, startRight);
        //if element is void it doesn't have children and text
        //and it has same start and end so we can initialize
        //only start and end segments
        if (relatedToNew.isVoid()) {
            relatedToNew.end = relatedToNew.start;
            return startRight;
        }
        //if element has children it doesn't have text instead of
        //whitespaces, so all what we need - detect element close tag segment
        //to do so we need to apply segments for all children first
        int beforeTextForChild = startRight;// 1 - \n
        if (newElement.hasChildren()) {

            final Iterator<NewElement> newChIt = newElement.getChildren().iterator();
            final Iterator<Element> chIt = relatedToNew.getChildren().iterator();

            while (newChIt.hasNext()) {
                beforeTextForChild = applySegments(newChIt.next(),
                                                   chIt.next(),
                                                   beforeTextForChild + 1,
                                                   level + 1,
                                                   relatedToNew);
            }
            //after last element we need to add 1 more text segment
            beforeTextForChild += 1 + lineTextLength;
        } else {
            relatedToNew.text = new LinkedList<>();
        }
        //before element close tag pos
        //                        +
        //<parent>\n    <child>text</child>
        final int beforeEnd;
        if (newElement.hasChildren()) {
            beforeEnd = beforeTextForChild;
        } else {
            beforeEnd = beforeTextForChild + relatedToNew.getText().length();
        }
        relatedToNew.text.add(new Segment(startRight + 1, beforeEnd));
        relatedToNew.end = new Segment(beforeEnd + 1, beforeEnd + closeTagLength(newElement));
        return relatedToNew.end.right;
    }

    /**
     * XMLStreamReader returns offset from source array start to start
     * of next element,  for all events instead its correct, instead of CHARACTERS event.
     * For characters event reader returns +2 or +3 to original offset, it depends on
     * next event type - if next type is END_ELEMENT then reader returns +3, if
     * next type is START_ELEMENT then reader returns +2.
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

    @Override
    public String toString() {
        return new String(xml, UTF_8);
    }
}