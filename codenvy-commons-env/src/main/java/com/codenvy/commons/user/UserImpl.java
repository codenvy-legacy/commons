/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.commons.user;

import java.util.*;

/**
 * Base implementation of User interface.
 *
 * @author andrew00x
 */
public class UserImpl implements User {
    private final String      name;
    private final Set<String> roles;
    private final String      token;

    public UserImpl(String name, String token, Collection<String> roles) {
        this.name = name;
        this.token = token;
        this.roles = roles == null ? Collections.<String>emptySet() : Collections.unmodifiableSet(new LinkedHashSet<>(roles));
    }

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
    public String toString() {
        return "UserImpl{" +
               "name='" + name + '\'' +
               ", roles=" + roles +
               ", token='" + token + '\'' +
               '}';
    }
}
