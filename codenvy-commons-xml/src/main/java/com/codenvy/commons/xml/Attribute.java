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

/**
 * TODO
 *
 * @author Eugene Voevodin
 */
public final class Attribute {

    private final Element container;
    private final String  name;
    private final String  prefix;
    private       String  value;

    Attribute(Element container, String prefix, String name, String value) {
        this.container = container;
        this.prefix = prefix;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getValue() {
        return value;
    }

    public Element getElement() {
        return container;
    }

    public boolean hasPrefix() {
        return prefix != null && !prefix.isEmpty();
    }

    public void remove() {
        container.removeAttribute(name);
    }

    public Attribute setValue(String value) {
        this.value = value;
        container.setAttributeValue(this);
        return this;
    }

    public String asString() {
        final StringBuilder sb = new StringBuilder();
        if (hasPrefix()) {
            sb.append(prefix).append(':');
        }
        return sb.append(name)
                 .append('=')
                 .append('"')
                 .append(value)
                 .append('"')
                 .toString();
    }

    @Override
    public String toString() {
        return asString();
    }
}
