/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
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
