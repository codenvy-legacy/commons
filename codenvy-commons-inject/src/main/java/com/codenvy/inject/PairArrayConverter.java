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
package com.codenvy.inject;

import com.codenvy.commons.lang.Pair;
import com.codenvy.commons.lang.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;

/** @author andrew00x */
public class PairArrayConverter extends AbstractModule implements TypeConverter {
    @Override
    public Object convert(String value, TypeLiteral<?> toType) {
        final String[] pairs = Strings.split(value, ',');
        @SuppressWarnings("unchecked")
        final Pair<String, String>[] result = new Pair[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            result[i] = PairConverter.fromString(pairs[i]);
        }
        return result;
    }

    @Override
    protected void configure() {
        convertToTypes(Matchers.only(new TypeLiteral<Pair<String, String>[]>() {
        }), this);
    }
}
