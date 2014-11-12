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
 * @author Eugene Voevodin
 */
public class PrefixedName {

    protected String prefix;
    protected String name;

    public PrefixedName(String name) {
        applyName(name);
    }

    public boolean hasPrefix() {
        return prefix != null && !prefix.isEmpty();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void applyName(String newName) {
        final int separator = newName.indexOf(':');
        if (separator != -1) {
            name = newName.substring(separator + 1);
            prefix = newName.substring(0, separator);
        } else {
            name = newName;
        }
    }
}
