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
 * TODO add static factory methods?
 *
 * @author Eugene Voevodin
 */
public final class NewAttribute extends PrefixedName {

    private String value;

    public NewAttribute(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String asString() {
        final StringBuilder sb = new StringBuilder();
        if (hasPrefix()) {
            sb.append(prefix).append(':');
        }
        return sb.append(getName())
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
