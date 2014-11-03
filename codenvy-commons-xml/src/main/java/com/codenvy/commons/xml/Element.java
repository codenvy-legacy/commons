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
import java.util.Collection;
import java.util.List;

import static com.codenvy.commons.xml.Util.fetchText;
import static java.util.Collections.unmodifiableList;

/**
 * @author Eugene Voevodin
 */
public final class Element {

    private final XMLTree owner;

    Segment       nodeStart;
    Segment       nodeEnd;
    List<Segment> textSegments;

    String          name;
    String          text;
    Element         parent;
    List<Element>   children;
    List<Attribute> attributes;

    Element(XMLTree owner) {
        this.owner = owner;
    }

    void setParent(Element parent) {
        if (parent.children == null) {
            parent.children = new ArrayList<>();
        }
        parent.children.add(this);
        this.parent = parent;
    }

    public String path() {
        return parent == null ? '/' + name : parent.path() + '/' + name;
    }

    public String getName() {
        return name;
    }

    public Element getParent() {
        return parent;
    }

    public Element getFirstSibling(String name) {
        for (Element child : parent.children) {
            if (this != child && child.name.equals(name)) {
                return child;
            }
        }
        return null; //or throw exception?
    }

    public Element getFirstChild(String name) {
        for (Element child : children) {
            if (child.name.equals(name)) {
                return child;
            }
        }
        return null; //or throw exception
    }


    public List<Element> getChildren() {
        return unmodifiableList(children);
    }

    public String getText() {
        if (text == null) {
            text = textSegments == null ? "" : fetchText(owner.xml, textSegments);
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

    public Element setText(String text) {
        this.text = text;
        owner.updateText(this);
        return this;
    }

    //TODO
    public List<Attribute> getAttributes() {
        throw new XMLTreeException("Not implemented");
    }

    //TODO
    public Element addAttribute(Attribute attribute) {
        throw new XMLTreeException("Not implemented");
    }

    //TODO
    public Element removeAttribute(String name) {
        throw new XMLTreeException("Not implemented");
    }

    //TODO
    public Element addChild(Element child) {
        throw new XMLTreeException("Not implemented");
    }

    //FIXME
    @Override
    public String toString() {
        return "(name: " + name + ". text:" + text + " )";
    }
}