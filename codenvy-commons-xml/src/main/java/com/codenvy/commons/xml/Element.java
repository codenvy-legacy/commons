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

import com.codenvy.commons.xml.XMLTree.Segment;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenvy.commons.xml.Util.asElement;
import static com.codenvy.commons.xml.Util.asElements;
import static com.codenvy.commons.xml.Util.nextElementSibling;
import static com.codenvy.commons.xml.Util.previousElementSibling;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * XMLTree element - gives ability to fetch
 * and update related xml document.
 * TODO: cleanup
 *
 * @author Eugene Voevodin
 */
public final class Element {

    private final XMLTree xmlTree;

    //used for tree content manipulation
    Segment       start;
    Segment       end;
    List<Segment> textSegments;

    //used with new element
    String          name;
    String          text;
    String          prefix;
    List<Element>   children;
    List<Attribute> attributes;

    //used to fetch information from related node
    Node delegate;

    Element(XMLTree xmlTree) {
        this.xmlTree = xmlTree;
    }

    public String getName() {
        if (delegate != null) {
            return delegate.getNodeName();
        }
        return name;
    }

    public Element getParent() {
        return asElement(delegate.getParentNode());
    }

    public Element getSingleSibling(String name) {
        Element target = null;
        for (Element sibling : asElements(delegate.getParentNode().getChildNodes())) {
            if (this != sibling && sibling.name.equals(name)) {
                if (target != null) {
                    throw new XMLTreeException("Element " + name + " has more then only sibling with name " + name);
                }
                target = sibling;
            }
        }
        return target;
    }

    public Element getSingleChild(String name) {
        for (Element child : asElements(delegate.getChildNodes())) {
            if (name.equals(child.name)) {
                if (child.hasSibling(name)) {
                    throw new XMLTreeException("Element " + name + " has more then only child with name " + name + " found");
                }
                return child;
            }
        }
        return null;
    }

    public Element getLastChild() {
        final Node lastChild = delegate.getLastChild();
        if (lastChild != null && lastChild.getNodeType() != ELEMENT_NODE) {
            return asElement(previousElementSibling(lastChild));
        }
        return asElement(lastChild);
    }

    public Element getFirstChild() {
        final Node firstChild = delegate.getFirstChild();
        if (firstChild.getNodeType() != ELEMENT_NODE) {
            return asElement(nextElementSibling(firstChild));
        }
        return asElement(firstChild);
    }

    //FIXME
    public List<Element> getChildren() {
        return unmodifiableList(asElements(delegate.getChildNodes()));
    }

    public String getText() {
        if (delegate == null) {
            return text == null ? "" : text;
        }
        return delegate.getTextContent();
    }

    public boolean hasSibling(String name) {
        final NodeList nodes = delegate.getParentNode().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) != delegate && name.equals(nodes.item(i).getNodeName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasParent() {
        return delegate.getParentNode() != null && delegate.getParentNode().getNodeType() != DOCUMENT_NODE;
    }

    public Element getPreviousSibling() {
        return asElement(previousElementSibling(delegate));
    }

    public Element getNextSibling() {
        return asElement(nextElementSibling(delegate));
    }

    public List<Attribute> getAttributes() {
        if (delegate != null && delegate.hasAttributes()) {
            final NamedNodeMap attributes = delegate.getAttributes();
            final List<Attribute> copy = new ArrayList<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                final Node item = attributes.item(i);
                copy.add(new Attribute(this, item.getPrefix(), item.getNodeName(), item.getNodeValue()));
            }
            return unmodifiableList(copy);
        }
        return emptyList();
    }

    public List<Element> getSiblings() {
        final List<Element> siblings = asElements(delegate.getParentNode().getChildNodes());
        siblings.remove(asElement(delegate));
        return unmodifiableList(siblings);
    }

    public boolean hasChild(String name) {
        final NodeList nodes = delegate.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (name.equals(nodes.item(i).getNodeName())) {
                return true;
            }
        }
        return false;
    }

    public Element setText(String newText) {
        requireNonNull(newText);
        if (!xmlTree.contains(this)) {
            text = newText;
        } else {
            if (!newText.equals(getText())) {
                delegate.setTextContent(newText);
                xmlTree.updateText(this);
                //fixme its not true - if element contains any child then first child left - 1 should
                //be used instead
                textSegments = singletonList(new Segment(start.right + 1, end.left - 1));
            }
        }
        return this;
    }

    //TODO fixme should be used only for update
    public Element setPrefix(String prefix) {
        checkIsNew(this);
        this.prefix = prefix;
        return this;
    }

    /**
     * Removes single element child.
     * If child does not exist nothing will be done
     *
     * @param name
     *         child name to removeElement
     */
    public void removeChild(String name) {
        if (!xmlTree.contains(this)) {
            throw new XMLTreeException("You can only use remove child on existing in tree element");
        }
        final Element child = getSingleChild(name);
        if (child != null) {
            child.remove();
        }
    }

    /**
     * Removes current element
     */
    public void remove() {
        if (!xmlTree.contains(this)) {
            throw new XMLTreeException("You can only remove existing in tree element");
        }
        xmlTree.dropElement(this);
        delegate.getParentNode().removeChild(delegate);
        delegate = null;
    }

    public void removeChildren(String name) {
        if (!xmlTree.contains(this)) {
            throw new XMLTreeException("You can only use remove children on existing in tree element");
        }
        final List<Node> matched = new LinkedList<>();
        final NodeList nodes = delegate.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (name.equals(nodes.item(i).getNodeName())) {
                matched.add(nodes.item(i));
            }
        }
        for (Node node : matched) {
            asElement(node).remove();
        }
    }

    public Element setAttribute(String name, String value) {
        final Attribute newAttribute = new Attribute(this, null, name, value);
        if (xmlTree.contains(this)) {
            xmlTree.insertAttribute(newAttribute);
        } else {
            if (attributes == null) {
                attributes = new LinkedList<>();
            }
            attributes.add(newAttribute);
        }
        return this;
    }

    public Element addAttribute(String prefix, String name, String value) {
        final Attribute newAttribute = new Attribute(this, prefix, name, value);
        if (xmlTree.contains(this)) {
            xmlTree.insertAttribute(newAttribute);
        } else {
            if (attributes == null) {
                attributes = new LinkedList<>();
            }
            attributes.add(newAttribute);
        }
        return this;
    }

    public Element removeAttribute(String name) {
        if (!xmlTree.contains(this)) {
            throw new XMLTreeException("You can only remove attribute from existing in tree element");
        }
        final Attribute attribute = getAttribute(name);
        if (attribute != null) {
            xmlTree.removeAttribute(attribute);
        }
        return this;
    }

    public boolean hasAttribute(String name) {
        return ((org.w3c.dom.Element)delegate).hasAttribute(name);
    }

    //if element doesn't have closing tag - <element attr="value"/>
    public boolean isVoid() {
        if (delegate == null) {
            return text == null && children == null;
        }
        return start.equals(end);
    }

    public Attribute getAttribute(String name) {
        if (delegate.hasAttributes()) {
            return asAttribute(getAttributeNode(name));
        }
        return null;
    }

    private Attribute asAttribute(Node node) {
        if (node == null) {
            return null;
        }
        return new Attribute(this, node.getPrefix(), node.getNodeName(), node.getNodeValue());
    }

    public Element appendChild(Element newElement) {
        checkIsRelative(newElement);
        checkIsNew(newElement);
        if (isVoid()) {
            throw new XMLTreeException("Append child is not permitted on void elements");
        }
        if (!xmlTree.contains(this)) {
            if (children == null) {
                children = new LinkedList<>();
            }
            children.add(newElement);
        } else {
            xmlTree.appendChild(newElement, this);
            dropFields();
        }
        return this;
    }

    public Element insertBefore(Element newElement) {
        checkIsRelative(newElement);
        checkIsNew(newElement);
        if (!xmlTree.contains(this)) {
            throw new XMLTreeException("You can only use inserted before existing in tree element");
        }
        xmlTree.insertBefore(newElement, this);
        dropFields();
        return this;
    }

    public Element insertAfter(Element newElement) {
        checkIsRelative(newElement);
        checkIsNew(newElement);
        if (!xmlTree.contains(this)) {
            throw new XMLTreeException("You can only use inserted after existing in tree element");
        }
        xmlTree.insertAfter(newElement, this);
        dropFields();
        return this;
    }

    /**
     * Converts element to string representation
     */
    public String asString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('<')
               .append(name);
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                builder.append(' ')
                       .append(attribute.asString());
            }
        }
        //if it is one line element such as <tag attr="value"/>
        if (isVoid()) {
            return builder.append('/')
                          .append('>')
                          .toString();
        }
        builder.append('>')
               .append(getText());
        if (children != null) {
            builder.append('\n');
            for (Element child : children) {
                builder.append(tabulate(child.asString(), 1))
                       .append('\n');
            }
        }
        builder.append('<')
               .append('/')
               .append(name)
               .append('>');
        return builder.toString();
    }

    void setAttributeValue(Attribute attribute) {
        final Node attributeNode = getAttributeNode(attribute.getName());
        xmlTree.updateAttributeValue(attribute, attributeNode.getNodeValue());
        getAttributeNode(attribute.getName()).setNodeValue(attribute.getValue());
    }

    private Node getAttributeNode(String name) {
        final NamedNodeMap attributes = delegate.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.item(i).getNodeName().equals(name)) {
                return attributes.item(i);
            }
        }
        return null;
    }

    private void checkIsRelative(Element element) {
        if (element.xmlTree != xmlTree) {
            throw new XMLTreeException("Element " + element + " should be created with same tree");
        }
    }

    /**
     * New element keeps information about
     * it name, text, children, attributes
     * and then it delegates org.w3c.dom.Node to
     * get all of this information, so after
     * element insertion references will be
     * obsolete.
     */
    private void dropFields() {
        //let gc do it work
        attributes = null;
        children = null;
        name = null;
        text = null;
    }

    private void checkIsNew(Element element) {
        if (xmlTree.contains(element)) {
            throw new XMLTreeException("Tree already contains element " + element);
        }
    }

    @Override
    public String toString() {
        return asString();
    }
}