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
import java.util.Iterator;
import java.util.ServiceLoader;

/** Class to parse factory url parameters and validate them */
public class FactoryUrlParser {
    private static final Logger LOG = LoggerFactory.getLogger(FactoryUrlParser.class);

    protected static final FactoryUrlValidator factoryUrlValidator;

    /** Retrieve FactoryUrlValidator implementation */
    static {
        ServiceLoader<FactoryUrlValidator> availableUrkValidators = ServiceLoader.load(FactoryUrlValidator.class);
        Iterator<FactoryUrlValidator> it = availableUrkValidators.iterator();
        if (it.hasNext()) {
            factoryUrlValidator = it.next();
            if (it.hasNext()) {
                LOG.error("Multiple FactoryUrlValidator implementation found. Please, put into service loader one implementation only");
                throw new RuntimeException(
                        "Multiple FactoryUrlValidator implementation found. Please, put into service loader one implementation only");
            }
        } else {
            factoryUrlValidator = new CommonFactoryUrlValidator();
        }
        LOG.info("Used {} as FactoryUrlValidator", factoryUrlValidator.getClass());
    }

    /**
     * Validate and parse factory url
     *
     * @param factoryUrl
     *         - factory url to parse
     * @throws FactoryUrlException
     *         - if factoryUrl is invalid
     */
    public static FactoryUrlParams parse(String factoryUrl) throws FactoryUrlException {
        FactoryUrlParams factoryUrlParams = factoryUrlValidator.validate(factoryUrl);
        String vcsUrl = factoryUrlParams.get(FactoryUrlParams.VCS_URL);
        try {
            checkRepository(vcsUrl);
        } catch (IOException e) {
            LOG.error("Checking Repository " + vcsUrl + " failed. Cause: " + e.getLocalizedMessage(), e);
            throw new FactoryUrlException("Checking Repository " + vcsUrl + " failed.", e);
        }
        return factoryUrlParams;
    }

    /**
     * Check git repository for a project existence and availability
     * @param vcsUrl - git repository url
     * @throws IOException
     */
    protected static void checkRepository(String vcsUrl) throws IOException {
        try {
            // To avoid repository cloning use git ls-remote util for repository check
            // Call git ls-remote is much faster than cloning
            Process process = Runtime.getRuntime().exec("/usr/bin/git ls-remote " + vcsUrl);

            // check return value of process.
            if (process.waitFor() != 0) {
                LOG.error("Can't check repository {}. Exit value is {}", new Object[][]{{vcsUrl, process.exitValue()}});
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    LOG.error(line);
                }
                throw new FactoryUrlException("Checking Repository " + vcsUrl + " failed");
            } else {
                LOG.debug("Repository check finished successfully");
            }
        } catch (InterruptedException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new FactoryUrlException(e.getLocalizedMessage(), e);
        }
    }
}
