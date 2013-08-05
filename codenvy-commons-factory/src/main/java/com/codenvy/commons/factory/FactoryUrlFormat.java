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

/** Provide a way of factory url parameters validation and parsing */
public interface FactoryUrlFormat {
    /**
     * Parse factory url parameters
     *
     * @param url
     *         - factory url to parse
     * @return <code>FactoryUrl</code> with factory url parameters if url is valid
     * @throws FactoryUrlInvalidArgumentException
     *         - if url satisfy format, but arguments is invalid
     * @throws FactoryUrlInvalidFormatException
     *         - if url doesn't satisfy format
     * @throws FactoryUrlException
     *         - if other exceptions occurs
     */
    public FactoryUrl parse(String url) throws FactoryUrlException;
}