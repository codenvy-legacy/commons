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
package com.codenvy.commons.security.oauth.oauth1;

import com.codenvy.commons.security.shared.User;

/** Represents BitBucket user */
public class BitBucketUser implements User {
    private String first_name;
    private String last_name;
    // used to obtain user information, such as emails
    private String username;
    private String email;

    @Override
    public String getId() {
        // we use only email for user identification fo now
        return email;
    }

    @Override
    public void setId(String id) {
        // JSON response from BitBucket API contains key 'id' but it has different purpose.
        // Ignore calls of this method. Email address is used as user identifier.
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder();
        if (first_name != null && !first_name.isEmpty()) {
            name.append(first_name);
        }
        if (last_name != null && !last_name.isEmpty()) {
            name.append(' ');
            name.append(last_name);
        }
        return name.toString();
    }


    @Override
    public String toString() {
        return "BitBucketUser{" +
               "first_name='" + first_name + '\'' +
               ", last_name='" + last_name + '\'' +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               '}';
    }

    @Override
    public void setName(String name) {
        String [] names = name.split(" ", 2);
        this.first_name = names[0];
        if (names.length > 1) {
           this.last_name = names[1];
        }
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
