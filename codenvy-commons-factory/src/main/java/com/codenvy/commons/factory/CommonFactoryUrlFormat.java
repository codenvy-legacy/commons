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

import com.codenvy.commons.lang.UrlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Common version of <code>FactoryUrlFormat</code>.
 * This implementation suggest that factory url contain all required parameters
 */
public class CommonFactoryUrlFormat implements FactoryUrlFormat {
    private static final Logger LOG = LoggerFactory.getLogger(CommonFactoryUrlFormat.class);

    private final static List<String> mandatoryParameters;

    // Required factory url parameters
    static {
        mandatoryParameters = new LinkedList<>();
        mandatoryParameters.add("v");
        mandatoryParameters.add("vcs");
        mandatoryParameters.add("vcsurl");
        mandatoryParameters.add("idcommit");
        mandatoryParameters.add("pname");
        mandatoryParameters.add("wname");
    }

    @Override
    public FactoryUrl parse(String url) throws FactoryUrlException {
        try {
            Map<String, List<String>> params = UrlUtils.getQueryParameters(url);

            // check API version first
            List<String> versionValues = params.get("v");
            if (versionValues == null) {
                throw new FactoryUrlInvalidArgumentException(
                        "We cannot locate your project. Please try again or contact support@codenvy.com.");
            } else if (!versionValues.contains("1.0")) {
                throw new FactoryUrlInvalidFormatException(
                        "We cannot locate your project. Please try again or contact support@codenvy.com.");
            }

            // check mandatory parameters
            for (String paramToCheck : mandatoryParameters) {
                List<String> values = params.get(paramToCheck);
                if (values == null) {
                    throw new FactoryUrlInvalidArgumentException(
                            "We cannot locate your project. Please try again or contact support@codenvy.com.");
                } else {
                    // throw exception if parameter quantity greater than one
                    // Also throw exception if parameter value is null or empty
                    String value = values.size() == 1 ? values.iterator().next() : null;
                    if (value == null || value.isEmpty()) {
                        throw new FactoryUrlInvalidArgumentException(
                                "We cannot locate your project. Please try again or contact support@codenvy.com.");
                    }
                }
            }

            checkRepository(params.get("vcsurl").iterator().next());

            FactoryUrl factoryUrl = new FactoryUrl();
            factoryUrl.setCommitId(params.get("idcommit").iterator().next());
            factoryUrl.setVersion(params.get("v").iterator().next());
            factoryUrl.setVcs(params.get("vcs").iterator().next());
            factoryUrl.setVcsUrl(params.get("vcsurl").iterator().next());
            factoryUrl.setProjectName(params.get("pname").iterator().next());
            factoryUrl.setWorkspaceName(params.get("wname").iterator().next());

            return factoryUrl;
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new FactoryUrlException("We cannot locate your project. Please try again or contact support@codenvy.com.");
        }
    }

    /**
     * Check git repository for a project existence and availability
     *
     * @param vcsUrl
     *         - git repository url
     * @throws FactoryUrlInvalidArgumentException
     *         - if repository isn't accessible
     * @throws FactoryUrlException
     *         - if other exceptions occurs
     */
    protected static void checkRepository(String vcsUrl) throws FactoryUrlException {
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
                throw new FactoryUrlInvalidArgumentException(
                        "We cannot clone the git repository for your project. Please try again or contact support@codenvy.com.");
            } else {
                LOG.debug("Repository check finished successfully.");
            }
        } catch (InterruptedException | IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new FactoryUrlException(e.getLocalizedMessage(), e);
        }
    }
}
