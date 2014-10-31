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
import java.util.List;

import static com.codenvy.commons.xml.Util.argumentRequired;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * TODO
 *
 * @author Eugene Voevodin
 */
public final class Element {

    private XMLTree.Node delegate;

    Element(XMLTree.Node delegate) {
        this.delegate = delegate;
    }

    public Element(String name) {
        delegate = new XMLTree.Node();
        delegate.name = argumentRequired(name, "Element name");
    }

    public Element getParent() {
        if (delegate.parent != null) {
            return new Element(delegate.parent);
        }
        //TODO: mb return element holder? and provide method hasParent?
        return null;
    }

    //FIXME: should it be first sibling? or list of siblings?
    public Element getSibling(String name) {
        for (XMLTree.Node child : delegate.parent.children) {
            if (child != delegate && child.name.equals(name)) {
                return new Element(child);
            }
        }
        throw new XMLTreeException("Sibling " + name + " doesn't exist");
    }

    public List<Element> getChildren() {
        if (delegate.children == null) {
            return emptyList();
        }
        final List<Element> elements = new ArrayList<>(delegate.children.size());
        for (XMLTree.Node child : delegate.children) {
            elements.add(new Element(child));
        }
        return unmodifiableList(elements);
    }

    public String getText() {
        return delegate.text;
    }

    public boolean hasSibling(String name) {
        for (XMLTree.Node child : delegate.parent.children) {
            if (child != delegate && child.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasChild(String name) {
        for (XMLTree.Node child : delegate.children) {
            if (child.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    //TODO: hasParent()?

    //TODO
    public List<Attribute> getAttributes() {
        throw new XMLTreeException("Not implemented");
    }

    //TODO
    public Element addAttribute(Attribute attribute) {
        throw new XMLTreeException("Not implemented");
    }

    //TODO rewrite src with this update
    public Element setText(String text) {
        delegate.text = text;
        return this;
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
        return "(name: " + delegate.name + ". text: " + delegate.text.trim() + ")";
    }
}