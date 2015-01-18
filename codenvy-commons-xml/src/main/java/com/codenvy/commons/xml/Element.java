/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
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
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
import static org.w3c.dom.Node.DOCUMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;
import static org.w3c.dom.Node.TEXT_NODE;

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

    /**
     * Returns name of element as <i>prefix:name</i>.
     * If element doesn't have prefix only local name will be returned
     */
    public String getName() {
        checkNotRemoved();
        return delegate.getTagName();
    }

    /**
     * Returns local name of element
     */
    public String getLocalName() {
        checkNotRemoved();
        return delegate.getLocalName();
    }

    /**
     * Element prefix or {@code null} if element name is not prefixed
     */
    public String getPrefix() {
        checkNotRemoved();
        return delegate.getPrefix();
    }

    /**
     * Returns parent element for this element or
     * {@code null} if element doesn't have parent
     */
    public Element getParent() {
        checkNotRemoved();
        return asElement(delegate.getParentNode());
    }

    /**
     * Searches for element sibling with given name.
     * If more then only sibling was found throws {@link XMLTreeException}.
     * If sibling with given name doesn't exist returns {@code null}
     *
     * @param name
     *         name to search sibling
     */
    public Element getSingleSibling(String name) {
        checkNotRemoved();
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

    /**
     * Searches for element child with given name.
     * If element has more then only child with given name
     * then {@link XMLTreeException} will ne thrown.
     * If child with given name doesn't exist returns {@code null}
     *
     * @param name
     *         name to search child
     */
    public Element getSingleChild(String name) {
        checkNotRemoved();
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

    /**
     * Returns last element child or {@code null} if
     * element doesn't have children
     */
    public Element getLastChild() {
        checkNotRemoved();
        final Node lastChild = delegate.getLastChild();
        if (lastChild != null && lastChild.getNodeType() != ELEMENT_NODE) {
            return asElement(previousElementNode(lastChild));
        }
        return asElement(lastChild);
    }

    /**
     * Returns first element child or {@code null}
     * if element doesn't have children
     */
    public Element getFirstChild() {
        checkNotRemoved();
        final Node firstChild = delegate.getFirstChild();
        if (firstChild.getNodeType() != ELEMENT_NODE) {
            return asElement(nextElementNode(firstChild));
        }
        return asElement(firstChild);
    }

    /**
     * Returns element children or empty list
     * if element doesn't have children
     */
    public List<Element> getChildren() {
        checkNotRemoved();
        return asElements(delegate.getChildNodes());
    }

    /**
     * Return children mapped with given mapper or empty list
     * if element doesn't have children
     *
     * @param mapper
     *         function which will be applied on each child element
     */
    public <R> List<R> getChildren(ElementMapper<? extends R> mapper) {
        checkNotRemoved();
        return asElements(delegate.getChildNodes(), mapper);
    }

    /**
     * Returns element text content.
     * <p/>
     * Only current element text will be returned,
     * children text or "CDATA" section will be skipped.
     */
    public String getText() {
        checkNotRemoved();
        return fetchText();
    }

    /**
     * Returns {@code true} if element has sibling with
     * given name, otherwise returns {@code false}
     */
    public boolean hasSibling(String name) {
        checkNotRemoved();
        checkNotNull(name, "sibling name");
        final NodeList nodes = delegate.getParentNode().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) != delegate && name.equals(nodes.item(i).getNodeName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if element has parent,
     * otherwise returns {@code false}
     */
    public boolean hasParent() {
        checkNotRemoved();
        return delegate.getParentNode() != null && delegate.getParentNode().getNodeType() != DOCUMENT_NODE;
    }

    /**
     * Returns previous element sibling or {@code null}
     * if element doesn't have previous sibling
     */
    public Element getPreviousSibling() {
        checkNotRemoved();
        return asElement(previousElementNode(delegate));
    }

    /**
     * Returns next element sibling or {@code null}
     * if element doesn't have next sibling
     */
    public Element getNextSibling() {
        checkNotRemoved();
        return asElement(nextElementNode(delegate));
    }

    /**
     * Returns element attributes or empty list
     * if element doesn't have attributes
     */
    public List<Attribute> getAttributes() {
        checkNotRemoved();
        if (delegate != null && delegate.hasAttributes()) {
            final NamedNodeMap attributes = delegate.getAttributes();
            final List<Attribute> copy = new ArrayList<>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                final Node item = attributes.item(i);
                copy.add(asAttribute(item));
            }
            return copy;
        }
        return emptyList();
    }

    /**
     * Returns list of element sibling or
     * empty list if element doesn't have siblings
     */
    public List<Element> getSiblings() {
        checkNotRemoved();
        final List<Element> siblings = asElements(delegate.getParentNode().getChildNodes());
        siblings.remove(asElement(delegate));
        return siblings;
    }

    /**
     * Returns {@code true} if element has child with given name,
     * otherwise returns {@code false}.
     *
     * @param name
     *         child name to check
     */
    public boolean hasChild(String name) {
        checkNotRemoved();
        checkNotNull(name, "child name");
        final NodeList nodes = delegate.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (name.equals(nodes.item(i).getNodeName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if element has at least one child
     * or {@code false} if doesn't
     */
    public boolean hasChildren() {
        checkNotRemoved();
        final NodeList childNodes = delegate.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets new text content to element
     *
     * @param newText
     *         new text content
     */
    public Element setText(String newText) {
        checkNotRemoved();
        checkNotNull(newText, "new text");
        if (!newText.equals(getText())) {
            removeTextNodes();
            delegate.appendChild(document().createTextNode(newText));
            //let tree do dirty job
            xmlTree.updateText(this);
        }
        return this;
    }

    /**
     * Returns text content of child with given name.
     *
     * @param childName
     *         child name to fetch text content
     * @return child text or {@code null} if child doesn't exist or
     * element has more then only child with given {@param name}
     */
    public String getChildText(String childName) {
        return getChildTextOrDefault(childName, null);
    }

    /**
     * Returns text content of child with given name or
     * default value if child doesn't exist or it has
     * sibling with same name
     *
     * @param childName
     *         name of child
     * @param defaultValue
     *         value which will be returned if child doesn't exist
     *         or it has sibling with same name
     * @return child text
     */
    public String getChildTextOrDefault(String childName, String defaultValue) {
        checkNotRemoved();
        checkNotNull(childName, "child name");
        return hasSingleChild(childName) ? getSingleChild(childName).getText() : defaultValue;
    }

    /**
     * Returns {@code true} if element has only sibling with given name
     * or {@code false} if element has more then 1 or 0 siblings with given name
     *
     * @param childName
     *         name of sibling
     */
    public boolean hasSingleChild(String childName) {
        checkNotRemoved();
        checkNotNull(childName, "child name");
        for (Element child : asElements(delegate.getChildNodes())) {
            if (childName.equals(child.getName())) {
                return !child.hasSibling(childName);
            }
        }
        return false;
    }

    /**
     * Removes single element child.
     * If child does not exist nothing will be done
     *
     * @param name
     *         child name to removeElement
     */
    public Element removeChild(String name) {
        checkNotRemoved();
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
        checkNotRemoved();
        notPermittedOnRootElement();
        //let tree do dirty job
        xmlTree.removeElement(this);
        //remove self from document
        delegate.getParentNode().removeChild(delegate);
        //if references to 'this' element exist
        //we should disallow ability to use delegate
        delegate = null;
    }

    /**
     * Removes children which names equal to given name
     *
     * @param name
     *         name to remove children
     */
    public Element removeChildren(String name) {
        checkNotRemoved();
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
        return this;
    }

    /**
     * Sets new attribute to element.
     * If element has attribute with given name
     * attribute value will be replaced with new value
     *
     * @param name
     *         attribute name
     * @param value
     *         attribute value
     */
    public Element setAttribute(String name, String value) {
        return setAttribute(new NewAttribute(name, value));
    }

    /**
     * Sets new attribute to element.
     * If element has attribute with {@code newAttribute#name}
     * then existing attribute value will be replaced with {@code newAttribute#value}.
     */
    public Element setAttribute(NewAttribute newAttribute) {
        checkNotRemoved();
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

    /**
     * Removes attribute with given name.
     * If element doesn't have attribute with given name
     * nothing will be done.
     */
    public Element removeAttribute(String name) {
        checkNotRemoved();
        final Attribute attribute = getAttribute(name);
        if (attribute != null) {
            xmlTree.removeAttribute(attribute);
            delegate.getAttributes()
                    .removeNamedItem(name);
        }
        return this;
    }

    /**
     * Returns {@code true} if element has attribute with given name
     *
     * @param name
     *         name of attribute to check
     */
    public boolean hasAttribute(String name) {
        checkNotRemoved();
        return delegate.hasAttribute(name);
    }

    /**
     * Returns {@code true} if element doesn't have closing tag
     * i.e {@literal <element attr="value"/>}, otherwise {@code false}
     */
    public boolean isVoid() {
        return start.equals(end);
    }

    /**
     * Returns attribute with given name or {@code null}
     * if element doesn't have such attribute
     *
     * @param name
     *         name to search attribute
     */
    public Attribute getAttribute(String name) {
        checkNotRemoved();
        checkNotNull(name, "attribute name");
        if (delegate.hasAttributes()) {
            return asAttribute(getAttributeNode(name));
        }
        return null;
    }

    /**
     * Replaces this element with new one.
     *
     * @param newElement
     *         new element which is replacement for current element
     * @return newly created element
     */
    public Element replaceWith(NewElement newElement) {
        checkNotRemoved();
        notPermittedOnRootElement();
        checkNotNull(newElement, "new element");
        insertAfter(newElement);
        final Element inserted = getNextSibling();
        remove();
        return inserted;
    }

    /**
     * Appends new element to the end of children list
     *
     * @param newElement
     *         element which will be inserted to the end
     *         of children list
     */
    public Element appendChild(NewElement newElement) {
        checkNotRemoved();
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

    /**
     * Inserts new element after current
     *
     * @param newElement
     *         element which will be inserted after current
     */
    public Element insertAfter(NewElement newElement) {
        checkNotRemoved();
        notPermittedOnRootElement();
        checkNotNull(newElement, "new element");
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

    /**
     * Inserts new element before current element
     *
     * @param newElement
     *         element which will be inserted before current
     */
    public Element insertBefore(NewElement newElement) {
        checkNotRemoved();
        notPermittedOnRootElement();
        checkNotNull(newElement, "new element");
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

    /**
     * Adds new element as child to the specified by {@link XMLTreeLocation} location.
     * <p/>
     * If it is not possible to insert element in specified location
     * then {@link XMLTreeException} will be thrown
     *
     * @param child
     *         new child
     */
    public Element insertChild(NewElement child, XMLTreeLocation place) {
        place.evalInsert(this, child);
        return this;
    }

    void setAttributeValue(Attribute attribute) {
        checkNotRemoved();
        final Node attributeNode = getAttributeNode(attribute.getName());
        xmlTree.updateAttributeValue(attribute, attributeNode.getNodeValue());
        getAttributeNode(attribute.getName()).setNodeValue(attribute.getValue());
    }

    private void removeTextNodes() {
        final NodeList childNodes = delegate.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == TEXT_NODE) {
                delegate.removeChild(childNodes.item(i));
            }
        }
    }

    private Attribute asAttribute(Node node) {
        if (node == null) {
            return null;
        }
        return new Attribute(this, node.getNodeName(), node.getNodeValue());
    }

    private String fetchText() {
        final StringBuilder sb = new StringBuilder();
        final NodeList childNodes = delegate.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == TEXT_NODE) {
                sb.append(childNodes.item(i).getTextContent());
            }
        }
        return sb.toString();
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

    private void notPermittedOnRootElement() {
        if (!hasParent()) {
            throw new XMLTreeException("Operation not permitted for root element");
        }
    }

    private void checkNotRemoved() {
        if (delegate == null) {
            throw new XMLTreeException("Operation not permitted for element which was removed from XMLTree");
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