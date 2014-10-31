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

import static com.codenvy.commons.xml.Util.argumentRequired;

/**
 * TODO
 *
 * @author Eugene Voevodin
 */
public final class Attribute {

    final XMLTree.AttrNode delegate;

    public Attribute(String name, String value) {
        delegate = new XMLTree.AttrNode();
        delegate.name = argumentRequired(name, "Attribute name");
        delegate.value = argumentRequired(value, "Attribute value");
    }

    Attribute(XMLTree.AttrNode delegate) {
        this.delegate = delegate;
    }

    public String getName() {
        return delegate.name;
    }

    public String getValue() {
        return delegate.value;
    }

    public void setValue() {
        //TODO
    }
}
