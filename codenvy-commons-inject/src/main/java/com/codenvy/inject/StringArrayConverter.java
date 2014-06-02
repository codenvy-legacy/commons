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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;

import java.util.ArrayList;
import java.util.List;

/** @author andrew00x */
public class StringArrayConverter extends AbstractModule implements TypeConverter {
    @Override
    public Object convert(String value, TypeLiteral<?> toType) {
        return split(value, ',');
    }

    private String[] split(String raw, char ch) {
        final List<String> list = new ArrayList<>(4);
        int n = 0;
        int p;
        while ((p = raw.indexOf(ch, n)) != -1) {
            list.add(raw.substring(n, p).trim());
            n = p + 1;
        }
        list.add(raw.substring(n).trim());
        return list.toArray(new String[list.size()]);
    }

    @Override
    protected void configure() {
        convertToTypes(Matchers.only(TypeLiteral.get(String[].class)), this);
    }
}
