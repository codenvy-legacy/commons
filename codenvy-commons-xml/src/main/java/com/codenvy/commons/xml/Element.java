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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenvy.commons.xml.Util.fetchText;
import static com.codenvy.commons.xml.Util.tabulate;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * @author Eugene Voevodin
 */
public final class Element {

    private final XMLTree xmlTree;

    Segment       start;
    Segment       end;
    List<Segment> textSegments;

    String          name;
    String          text;
    String          path;
    Element         parent;
    List<Element>   children;
    List<Attribute> attributes;

    Element(XMLTree xmlTree) {
        this.xmlTree = xmlTree;
    }

    public String path() {
        if (path == null) {
            path = parent == null ? '/' + name : parent.path() + '/' + name;
        }
        return path;
    }

    public String getName() {
        return name;
    }

    public Element getParent() {
        return parent;
    }

    public Element getSingleSibling(String name) {
        Element target = null;
        for (Element sibling : parent.children) {
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
        for (Element child : children) {
            if (child.name.equals(name)) {
                if (child.hasSibling(name)) {
                    throw new XMLTreeException("Element " + name + " has more then only child with name " + name + " found");
                }
                return child;
            }
        }
        return null;
    }

    public Element getLastChild() {
        if (children != null) {
            return children.get(children.size() - 1);
        }
        return null;
    }

    public Element getFirstChild() {
        if (children != null) {
            return children.get(0);
        }
        return null;
    }

    public List<Element> getChildren() {
        if (children != null) {
            return unmodifiableList(children);
        }
        return emptyList();
    }

    public String getText() {
        if (text == null) {
            text = textSegments == null ? "" : fetchText(xmlTree.xml, textSegments);
        }
        return text;
    }

    public boolean hasSibling(String name) {
        for (Element child : parent.children) {
            if (child != this && child.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Element getPreviousSibling() {
        Element before = null;
        for (Element child : parent.children) {
            if (child == this) {
                return before;
            }
            before = child;
        }
        throw new XMLTreeException("You should not see this message");
    }

    public Element getNextSibling() {
        final Iterator<Element> siblingsIt = parent.children.iterator();
        Element next = siblingsIt.next();
        while (next != this) {
            next = siblingsIt.next();
        }
        return siblingsIt.hasNext() ? siblingsIt.next() : null;
    }

    public List<Attribute> getAttributes() {
        if (attributes != null) {
            return unmodifiableList(attributes);
        }
        return emptyList();
    }

    public List<Element> getSiblings() {
        final List<Element> siblings = new ArrayList<>(parent.children.size() - 1);
        for (Element sibling : parent.children) {
            if (sibling != this) {
                siblings.add(sibling);
            }
        }
        return unmodifiableList(siblings);
    }

    public boolean hasChild(String name) {
        for (Element child : children) {
            if (child.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Element setText(String newText) {
        requireNonNull(newText);
        if (!newText.equals(text)) {
            text = newText;
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
        parent.children.remove(this);
    }

    public void removeChildren(String name) {
        final List<Element> matching = new LinkedList<>();
        for (Element child : children) {
            if (name.equals(child.name)) {
                //we cant remove child right now cause it affects list which we
                //are iterating so ConcurrentModificationException will be thrown
                matching.add(child);
            }
        }
        for (Element element : matching) {
            element.remove();
        }
    }

    public Element addAttribute(String name, String value) {
        if (attributes == null) {
            //todo size?
            attributes = new ArrayList<>();
        }
        attributes.add(new Attribute(this, name, value));
        return this;
    }

    //TODO
    public Element removeAttribute(String name) {
        throw new XMLTreeException("Not implemented");
    }

    //TODO
    public Element appendChild(Element newElement) {
        getLastChild().insertAfter(newElement);
        return this;
    }

    public Element insertBefore(Element newElement) {
        newElement.setParent(parent);
        xmlTree.insertBefore(newElement, this);
        return this;
    }

    public Element insertAfter(Element newElement) {
        newElement.setParent(parent);
        xmlTree.insertAfter(newElement, this);
        return this;
    }

    //add support for one line elements
    public String asString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('<')
               .append(name)
               .append('>')
               .append(getText().trim());
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

    void setParent(Element parent) {
        if (parent.children == null) {
            //fixme what about size?
            parent.children = new ArrayList<>();
        }
        parent.children.add(this);
        this.parent = parent;
    }


    @Override
    public String toString() {
        return asString();
    }
}