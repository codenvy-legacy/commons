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

    private final String  name;
    private final String  value;
    private final Element container;

    Segment valueSegment;
    Segment nameSegment;

    Attribute(Element container, String name, String value) {
        this.container = container;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        throw new XMLTreeException("Not implemented");
    }

    public void setValue(String value) {
        throw new XMLTreeException("Not implemented");
    }
}
