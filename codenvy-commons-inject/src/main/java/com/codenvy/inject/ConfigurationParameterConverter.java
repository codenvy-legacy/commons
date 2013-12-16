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

import com.google.inject.TypeLiteral;

import org.nnsoft.guice.rocoto.converters.AbstractConverter;

/**
 * Converts String to ConfigurationParameter.
 *
 * @author andrew00x
 */
public final class ConfigurationParameterConverter extends AbstractConverter<ConfigurationParameter> {
    @Override
    public Object convert(String value, TypeLiteral<?> toType) {
        return new ConfigurationParameter(value);
    }
}
