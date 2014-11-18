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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenvy.commons.xml.XMLTreeUtil.asElement;
import static com.codenvy.commons.xml.XMLTreeUtil.asElements;
import static com.codenvy.commons.xml.XMLTreeUtil.checkNotNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * XMLTree element which provides abilities to
 * fetch and update xml document data.
 * <p/>
 * Delegates for related {@link org.w3c.dom.Element}
 *
 * @author Eugene Voevodin
 */
public final class Element {

    private final XMLTree xmlTree;

    Segment       start;
    Segment       end;
    List<Segment> text;

    org.w3c.dom.Element delegate;

    Element(XMLTree xmlTree) {
        this.xmlTree = xmlTree;
    }

    public String getName() {
        return delegate.getTagName();
    }

    public String getLocalName() {
        return delegate.getLocalName();
    }

    public String getPrefix() {
        return delegate.getPrefix();
    }

    public Element getParent() {
        return asElement(delegate.getParentNode());
    }

    public Element getSingleSibling(String name) {
        checkNotNull(name, "sibling name");
        Element target = null;
        for (Element sibling : asElements(delegate.getParentNode().getChildNodes())) {
            if (this != sibling && sibling.getName().equals(name)) {
                if (target != null) {
                    throw new XMLTreeException("Element " + name + " has more then only sibling with name " + name);
                }
                target = sibling;
            }
        }
        return target;
    }

    public Element getSingleChild(String name) {
        checkNotNull(name, "child name");
        for (Element child : asElements(delegate.getChildNodes())) {
            if (name.equals(child.getName())) {
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
            return asElement(previousElementNode(lastChild));
        }
        return asElement(lastChild);
    }

    public Element getFirstChild() {
        final Node firstChild = delegate.getFirstChild();
        if (firstChild.getNodeType() != ELEMENT_NODE) {
            return asElement(nextElementNode(firstChild));
        }
        return asElement(firstChild);
    }

    public List<Element> getChildren() {
        return unmodifiableList(asElements(delegate.getChildNodes()));
    }

    public String getText() {
        return delegate.getTextContent();
    }

    public boolean hasSibling(String name) {
        checkNotNull(name, "sibling name");
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
        return asElement(previousElementNode(delegate));
    }

    public Element getNextSibling() {
        return asElement(nextElementNode(delegate));
    }

    public List<Attribute> getAttributes() {
        if (delegate != null && delegate.hasAttributes()) {
            final NamedNodeMap attributes = delegate.getAttributes();
            final List<Attribute> copy = new ArrayList<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                final Node item = attributes.item(i);
                copy.add(asAttribute(item));
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
        checkNotNull(name, "child name");
        final NodeList nodes = delegate.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (name.equals(nodes.item(i).getNodeName())) {
                return true;
            }
        }
        return false;
    }

    public Element setText(String newText) {
        checkNotNull(newText, "new text");
        if (!newText.equals(getText())) {
            delegate.setTextContent(newText);
            xmlTree.updateText(this);
            //fixme its not true - if element contains any child then first child left - 1 should
            //be used instead
            text = singletonList(new Segment(start.right + 1, end.left - 1));
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
    public Element removeChild(String name) {
        final Element child = getSingleChild(name);
        if (child != null) {
            child.remove();
        }
        return this;
    }

    /**
     * Removes current element
     */
    public void remove() {
        notPermittedForRoot();
        //let tree do dirty job
        xmlTree.removeElement(this);
        //remove self from document
        delegate.getParentNode().removeChild(delegate);
        //if references to 'this' element exist
        //we should disallow ability to use delegate
        delegate = null;
    }

    public void removeChildren(String name) {
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
        return setAttribute(new NewAttribute(name, value));
    }

    public Element setAttribute(NewAttribute newAttribute) {
        //if tree already contains element replace value
        if (hasAttribute(newAttribute.getName())) {
            final Attribute attr = getAttribute(newAttribute.getName());
            attr.setValue(newAttribute.getValue());
            return this;
        }
        //
        if (newAttribute.hasPrefix()) {
            delegate.setAttributeNodeNS(createAttrNSNode(newAttribute));
        } else {
            delegate.setAttributeNode(createAttrNode(newAttribute));
        }
        //let tree do dirty job
        xmlTree.insertAttribute(newAttribute, this);
        return this;
    }

    private Attr createAttrNode(NewAttribute newAttribute) {
        final Attr attr = document().createAttribute(newAttribute.getName());
        attr.setValue(newAttribute.getValue());
        return attr;
    }

    private Attr createAttrNSNode(NewAttribute attribute) {
        if (attribute.getPrefix().equals(XMLNS_ATTRIBUTE)) {
            final Attr attr = document().createAttributeNS(XMLNS_ATTRIBUTE_NS_URI, attribute.getName());
            attr.setValue(attribute.getValue());
            //save uri
            xmlTree.putNamespace(attribute.getLocalName(), attribute.getValue());
            return attr;
        } else {
            //retrieve namespace
            final String uri = xmlTree.getNamespaceUri(attribute.getPrefix());
            final Attr attr = document().createAttributeNS(uri, attribute.getName());
            attr.setValue(attribute.getValue());
            return attr;
        }
    }

    public Element removeAttribute(String name) {
        final Attribute attribute = getAttribute(name);
        if (attribute != null) {
            xmlTree.removeAttribute(attribute);
            delegate.getAttributes()
                    .removeNamedItem(name);
        }
        return this;
    }

    public boolean hasAttribute(String name) {
        return delegate.hasAttribute(name);
    }

    //if element doesn't have closing tag - <element attr="value"/>
    public boolean isVoid() {
        return start.equals(end);
    }

    public Attribute getAttribute(String name) {
        checkNotNull(name, "attribute name");
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

    public Element appendChild(NewElement newElement) {
        checkNotNull(newElement, "new element");
        if (isVoid()) {
            throw new XMLTreeException("Append child is not permitted on void elements");
        }
        final Node newNode = createNode(newElement);
        final Element element = createElement(newNode);
        //append new node into document
        delegate.appendChild(newNode);
        //let tree do dirty job
        xmlTree.appendChild(newElement, element, this);
        return this;
    }

    public Element insertAfter(NewElement newElement) {
        checkNotNull(newElement, "new element");
        notPermittedForRoot();
        final Node newNode = createNode(newElement);
        final Element element = createElement(newNode);
        //if element has next sibling append child to parent
        //else insert before next sibling
        final Node nextNode = nextElementNode(delegate);
        if (nextNode != null) {
            delegate.getParentNode().insertBefore(newNode, nextNode);
        } else {
            delegate.getParentNode().appendChild(newNode);
        }
        //let tree do dirty job
        xmlTree.insertAfter(newElement, element, this);
        return this;
    }

    public Element insertBefore(NewElement newElement) {
        checkNotNull(newElement, "new element");
        notPermittedForRoot();
        //if element has previous sibling insert new element after it
        //inserting before this element to let existing comments
        //or whatever over referenced element
        if (previousElementNode(delegate) != null) {
            getPreviousSibling().insertAfter(newElement);
            return this;
        }
        final Node newNode = createNode(newElement);
        final Element element = createElement(newNode);
        delegate.getParentNode().insertBefore(newNode, delegate);
        //let tree do dirty job
        xmlTree.insertAfterParent(newElement, element, getParent());
        return this;
    }

    void setAttributeValue(Attribute attribute) {
        final Node attributeNode = getAttributeNode(attribute.getName());
        xmlTree.updateAttributeValue(attribute, attributeNode.getNodeValue());
        getAttributeNode(attribute.getName()).setNodeValue(attribute.getValue());
    }

    private Node nextElementNode(Node node) {
        node = node.getNextSibling();
        while (node != null && node.getNodeType() != ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return node;
    }

    private Node previousElementNode(Node node) {
        node = node.getPreviousSibling();
        while (node != null && node.getNodeType() != ELEMENT_NODE) {
            node = node.getPreviousSibling();
        }
        return node;
    }

    private void notPermittedForRoot() {
        if (!hasParent()) {
            throw new XMLTreeException("Operation not permitted for root element");
        }
    }

    private Element createElement(Node node) {
        final Element element = new Element(xmlTree);
        element.delegate = (org.w3c.dom.Element)node;
        node.setUserData("element", element, null);
        if (node.hasChildNodes()) {
            final NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() == ELEMENT_NODE) {
                    createElement(children.item(i));
                }
            }
        }
        return element;
    }

    private Node createNode(NewElement newElement) {
        final org.w3c.dom.Element newNode;
        if (newElement.hasPrefix()) {
            final String uri = xmlTree.getNamespaceUri(newElement.getPrefix());
            newNode = document().createElementNS(uri, newElement.getName());
        } else {
            newNode = document().createElement(newElement.getLocalName());
        }
        newNode.setTextContent(newElement.getText());
        //creating all related children
        for (NewElement child : newElement.getChildren()) {
            newNode.appendChild(createNode(child));
        }
        //creating all related attributes
        for (NewAttribute attribute : newElement.getAttributes()) {
            if (attribute.hasPrefix()) {
                newNode.setAttributeNodeNS(createAttrNSNode(attribute));
            } else {
                newNode.setAttributeNode(createAttrNode(attribute));
            }
        }
        return newNode;
    }

    private Document document() {
        return delegate.getOwnerDocument();
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
}