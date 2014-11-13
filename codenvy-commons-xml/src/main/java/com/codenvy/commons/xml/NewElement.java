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

import static com.codenvy.commons.xml.Util.tabulate;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static java.util.Arrays.asList;

/**
 * Used to add new element to {@link XMLTree}.
 * This class is really convenient when
 * you need to make complex tree update, to
 * do it you need to make hierarchy from NewElement
 * instances which can contain NewAttribute instances as well.
 * When NewElement instance is ready tree uses {@link NewElement#asString()}
 * to get view of new element.
 *
 * Why don't we just create {@link Element} instead of using {@link NewElement} class?
 * //TODO add ul li
 * First reason for it is performance!
 * Each time when you need to insert element
 * you need to rewrite tree bytes, but with {@link NewElement}
 * we ca n do it only time.
 * Second reason is that we need to keep new element values such as
 * children, attributes etc, fom each element instance which
 * should be added to tree and after tree update we
 * need to drop this fields because element doesn't need it anymore.
 * Third reason is that each element should be related to tree,
 * so we need to make check each time when we need to execute update.
 *
 * @author Eugene Voevodin
 */
public final class NewElement extends QName {

    public static NewElement createElement(String name) {
        return new NewElement(name, null);
    }

    public static NewElement createElement(String name, String text) {
        return new NewElement(name, text);
    }

    public static NewElement createElement(String name, NewElement... children) {
        final NewElement newElement = createElement(name);
        newElement.children = new ArrayList<>(asList(children));
        return newElement;
    }

    private static final int EXPECTED_ATTRIBUTES_SIZE = 2;
    private static final int EXPECTED_CHILDREN_SIZE   = 3;

    private String             text;
    private List<NewAttribute> attributes;
    private List<NewElement>   children;

    private NewElement(String name, String text) {
        super(name);
        this.text = text;
    }

    public NewElement setText(String text) {
        this.text = text;
        return this;
    }

    public NewElement setAttributes(List<NewAttribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public NewElement setChildren(List<NewElement> children) {
        this.children = children;
        return this;
    }

    public NewElement appendChild(NewElement child) {
        getChildren().add(child);
        return this;
    }

    public NewElement setAttribute(String name, String value) {
        getAttributes().add(new NewAttribute(name, value));
        return this;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public List<NewAttribute> getAttributes() {
        if (attributes == null) {
            attributes = newArrayListWithExpectedSize(EXPECTED_ATTRIBUTES_SIZE);
        }
        return attributes;
    }

    public List<NewElement> getChildren() {
        if (children == null) {
            children = newArrayListWithExpectedSize(EXPECTED_CHILDREN_SIZE);
        }
        return children;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean isVoid() {
        return text == null && !hasChildren();
    }

    public String asString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('<')
               .append(getName());
        if (attributes != null) {
            for (NewAttribute attribute : attributes) {
                builder.append(' ')
                       .append(attribute.asString());
            }
        }
        //if it is void element such as <tag attr="value"/>
        if (isVoid()) {
            return builder.append('/')
                          .append('>')
                          .toString();
        }
        builder.append('>')
               .append(getText());
        if (hasChildren()) {
            builder.append('\n');
            for (NewElement child : children) {
                builder.append(tabulate(child.asString(), 1))
                       .append('\n');
            }
        }
        builder.append('<')
               .append('/')
               .append(getName())
               .append('>');
        return builder.toString();
    }

    @Override
    public String toString() {
        return asString();
    }
}
