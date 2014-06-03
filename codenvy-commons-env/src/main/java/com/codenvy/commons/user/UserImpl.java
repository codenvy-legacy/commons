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
package com.codenvy.commons.user;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Base implementation of User interface.
 *
 * @author andrew00x
 */
public class UserImpl implements User {
    private final String      name;
    private final Set<String> roles;
    private final String      token;
    private final String      id;

    public UserImpl(String name, String id, String token, Collection<String> roles) {
        this.name = name;
        this.id = id;
        this.token = token;
        this.roles = roles == null ? Collections.<String>emptySet() : Collections.unmodifiableSet(new LinkedHashSet<>(roles));
    }

    @Deprecated
    public UserImpl(String name, String token, Collection<String> roles) {
        this(name, null, token, roles);
    }

    @Deprecated
    public UserImpl(String name) {
        this(name, null, null);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMemberOf(String role) {
        return roles.contains(role);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
               "name='" + name + '\'' +
               ", roles=" + roles +
               ", token='" + token + '\'' +
               '}';
    }
}
