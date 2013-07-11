/*
 * Copyright (C) 2013 eXo Platform SAS.
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
package com.codenvy.commons.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/** Class to parse factory url parameters and validate them */
public class FactoryUrlParser {
    private static final Logger LOG = LoggerFactory.getLogger(FactoryUrlParser.class);

    protected static final Set<FactoryUrlFormat> factoryUrlFormats = new HashSet<>();

    /** Retrieve FactoryUrlFormat implementations */
    static {
        ServiceLoader<FactoryUrlFormat> availableUrlFormats = ServiceLoader.load(FactoryUrlFormat.class);
        for (FactoryUrlFormat factoryUrlFormat : availableUrlFormats) {
            factoryUrlFormats.add(factoryUrlFormat);
            LOG.info("Used {} as FactoryUrlFormat", factoryUrlFormat.getClass());
        }
        if (factoryUrlFormats.size() == 0) {
            LOG.error("FactoryUrlFormat implementations wasn't found");
            throw new RuntimeException("FactoryUrlFormat implementations wasn't found");
        }
    }

    /**
     * Validate and parse factory url
     *
     * @param factoryUrl
     *         - factory url to parse
     * @throws FactoryUrlInvalidFormatException
     *         - there is no format to validate such url
     * @throws FactoryUrlInvalidArgumentException
     *         - if url satisfy format, but arguments is invalid
     * @throws FactoryUrlException
     *         - if other exceptions occurs
     */
    public static FactoryUrl parse(String factoryUrl) throws FactoryUrlException {
        FactoryUrl factoryUrlParams = null;
        for (FactoryUrlFormat factoryUrlFormat : factoryUrlFormats) {
            try {
                factoryUrlParams = factoryUrlFormat.parse(factoryUrl);
                if (factoryUrlParams != null) {
                    return factoryUrlParams;
                }
            } catch (FactoryUrlInvalidFormatException ignored) {
            }
        }
        throw new FactoryUrlInvalidFormatException("There is no factory url format to validate such url");
    }
}
