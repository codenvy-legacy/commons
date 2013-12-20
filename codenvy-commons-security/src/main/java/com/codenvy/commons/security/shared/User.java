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
package com.codenvy.commons.security.shared;

/**
 * Represents an User with unique identifier. Have such interface to be able use GWT AutoBean feature. Any interface
 * that represents an User should extend this interface.
 */
public interface User {
    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getEmail();

    void setEmail(String email);
}
