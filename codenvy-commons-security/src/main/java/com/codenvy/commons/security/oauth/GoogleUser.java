/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.codenvy.commons.security.oauth;


import com.codenvy.commons.security.shared.User;

/** Represents Google user. */
public class GoogleUser implements User {
    private String email;
    private String name;

    @Override
    public final String getId() {
        return email;
    }

    @Override
    public final void setId(String id) {
        // JSON response from Google API contains key 'id' but it has different purpose.
        // Ignore calls of this method. Email address is used as user identifier.
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        setId(email);
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GoogleUser{" +
               "id='" + getId() + '\'' +
               ", email='" + email + '\'' +
               ", name='" + name + '\'' +
               '}';
    }
}
