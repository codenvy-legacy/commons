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
import static com.codenvy.commons.xml.Util.attributeLength;
import static com.codenvy.commons.xml.Util.nextElementSibling;
import static com.codenvy.commons.xml.Util.previousElementSibling;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.util.Collections.emptyList;
import static java.util.Collections.max;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * TODO cleanup here
 *
 * @author Eugene Voevodin
 */
public final class Element {

    private final XMLTree xmlTree;

    Segment       start;
    Segment       end;
    List<Segment> textSegments;

    //TODO remove used for update
    String          name;
    String          text;
    List<Element>   children;
    List<Attribute> attributes;

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
                copy.add(new Attribute(this, attributes.item(i).getNodeName(), attributes.item(i).getNodeValue()));
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
        if (!newText.equals(getText())) {
            delegate.setTextContent(newText);
            xmlTree.updateText(this);
            //fixme its not true - if element contains any child then first child left - 1 should
            //be used instead
            textSegments = singletonList(new Segment(start.right + 1, end.left - 1));
        }
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
        final Element child = getSingleChild(name);
        if (child != null) {
            child.remove();
        }
    }

    /**
     * Removes current element
     */
    public void remove() {
        //TODO throw exception if its newly created element
        xmlTree.dropElement(this);
        delegate.getParentNode().removeChild(delegate);
    }

    public void removeChildren(String name) {
        final List<Node> matched = new LinkedList<>();
        final NodeList nodes = this.delegate.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (name.equals(nodes.item(i).getNodeName())) {
                matched.add(nodes.item(i));
            }
        }
        for (Node node : matched) {
            asElement(node).remove();
        }
    }

    //FIXME
//    public Element addAttribute(Attribute attribute) {
//        if (attributes == null) {
//            attributes = new LinkedList<>();
//        }
//        attributes.add(attribute);
//        return this;
//    }

    public Element removeAttribute(String name) {
        final Attribute attribute = getAttribute(name);
        if (attribute != null) {
            xmlTree.removeAttribute(attribute);
        }
        return this;
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
        return new Attribute(this, node.getNodeName(), node.getNodeValue());
    }

    public Element appendChild(Element newElement) {
        xmlTree.appendChild(newElement, this);
        return this;
    }

    public Element insertBefore(Element newElement) {
        xmlTree.insertBefore(newElement, this);
        return this;
    }

    public Element insertAfter(Element newElement) {
        xmlTree.insertAfter(newElement, this);
        return this;
    }

    void setAttributeValue(Attribute attribute) {
        final Node attributeNode = getAttributeNode(attribute.getName());
        xmlTree.updateAttribute(attribute, attributeNode.getNodeValue());
        getAttributeNode(attribute.getName()).setNodeValue(attribute.getValue());
    }

    //TODO add support for one line tags <option attr="value" />
    public String asString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('<')
               .append(name)
               .append('>')
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

    @Override
    public String toString() {
        return asString();
    }
}