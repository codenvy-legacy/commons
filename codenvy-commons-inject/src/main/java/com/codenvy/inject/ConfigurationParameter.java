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
package com.codenvy.inject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for configuration parameter that ables to convert String to other types.
 *
 * @author <a href="mailto:aparfonov@codenvy.com">Andrey Parfonov</a>
 */
public class ConfigurationParameter {
    private final String value;

    public ConfigurationParameter(String value) {
        this.value = value;
    }

    /** Get the value as String. */
    public String asString() {
        return value;
    }

    /** Get the value as {@code boolean}. */
    public boolean asBoolean() {
        return Boolean.parseBoolean(value);
    }

    /**
     * Get the value as {@code int}.
     *
     * @throws NumberFormatException
     *         if can't convert value to {@code int}.
     */
    public int asInt() {
        return Integer.parseInt(value);
    }

    /**
     * Get the value as {@code long}.
     *
     * @throws NumberFormatException
     *         if can't convert value to {@code long}.
     */
    public long asLong() {
        return Long.parseLong(value);
    }

    /**
     * Get the value as {@code float}.
     *
     * @throws NumberFormatException
     *         if can't convert value to {@code float}.
     */
    public float asFloat() {
        return Float.parseFloat(value);
    }

    /**
     * Get the value as {@code double}.
     *
     * @throws NumberFormatException
     *         if can't convert value to {@code double}.
     */
    public double asDouble() {
        return Double.parseDouble(value);
    }

    /**
     * Get the comma delimited values of the value as {code List} of {@code String}.
     * <p/>
     * Note: Trims leading and trailing whitespace on each value.
     *
     * @return value as {@code List} of {@code String}
     */
    public List<String> asStrings() {
        return split(value, ',');
    }

    private List<String> split(String raw, char ch) {
        final List<String> list = new ArrayList<>(4);
        int n = 0;
        int p;
        while ((p = raw.indexOf(ch, n)) != -1) {
            list.add(raw.substring(n, p).trim());
            n = p + 1;
        }
        list.add(raw.substring(n).trim());
        return list;
    }

    /**
     * Get the value as {@code URI}.
     *
     * @return value as {@code URI}
     * @throws IllegalArgumentException
     *         if value isn't valid URI
     */
    public URI asURI() {
        return URI.create(value);
    }

    /**
     * Get the value as {@code URL}.
     *
     * @return value as {@code URL}
     * @throws IllegalArgumentException
     *         if value isn't valid URL
     */
    public URL asURL() {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Get the value as {@code java.io.File}.
     *
     * @return value as {@code java.io.File}
     */
    public java.io.File asFile() {
        return new java.io.File(value);
    }
}