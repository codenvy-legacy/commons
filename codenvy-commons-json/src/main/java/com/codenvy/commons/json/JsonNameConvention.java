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
package com.codenvy.commons.json;

/**
 * Abstraction to provide name transformation between JSON and Java names. It helps correct translate JSON names to
 * correct name of Java fields or methods, e.g translate Java camel-case name to lowercase JSON names with '-' or '_'
 * separator. Pass implementation of this interface to methods of {@link JsonHelper} to get required behaviour whe
 * serialize or deserialize objects to|from JSON.
 *
 * @see JsonNameConventions
 * @see NameConventionJsonParser
 * @see NameConventionJsonWriter
 */
public interface JsonNameConvention {
    /**
     * Translate Java field name to JSON name, e.g. 'userName' -> 'user_name'
     *
     * @param javaName
     *         Java field name
     * @return JSON name
     */
    String toJsonName(String javaName);

    /**
     * Translate JSON name to Java field name, e.g. 'user_name' -> 'userName'
     *
     * @param jsonName
     *         JSON name
     * @return Java field name
     */
    String toJavaName(String jsonName);
}
