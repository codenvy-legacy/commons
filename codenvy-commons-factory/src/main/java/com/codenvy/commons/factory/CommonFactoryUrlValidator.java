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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenvy.commons.factory.FactoryUrlParams.*;

/**
 * Common version of <code>FactoryUrlValidator</code>.
 * This implementation suggest that factory url contain all required parameters
 */
public class CommonFactoryUrlValidator implements FactoryUrlValidator {
    private final static List<String> mandatoryParameters;

    // Required factory url parameters
    static {
        mandatoryParameters = new LinkedList<>();
        mandatoryParameters.add("v");
        mandatoryParameters.add("vcs");
        mandatoryParameters.add("vcsurl");
        mandatoryParameters.add("commitid");
        mandatoryParameters.add("pname");
        mandatoryParameters.add("wname");
    }

    @Override
    public FactoryUrlParams validate(String url) throws FactoryUrlException {
        try {
            Map<String, List<String>> params = UrlUtils.getQueryParameters(url);

            for (String paramToCheck : mandatoryParameters) {
                List<String> values = params.get(paramToCheck);
                if (values == null || values.size() != 1 || values.iterator().next() == null || values.iterator().next().isEmpty()) {
                    throw new FactoryUrlException("Factory url parameters list is illegal");
                }
            }

            FactoryUrlParams factoryUrlParams = new FactoryUrlParams();
            factoryUrlParams.put(ID_COMMIT, params.get(ID_COMMIT).iterator().next());
            factoryUrlParams.put(VERSION, params.get(VERSION).iterator().next());
            factoryUrlParams.put(VCS, params.get(VCS).iterator().next());
            factoryUrlParams.put(VCS_URL, params.get(VCS_URL).iterator().next());
            factoryUrlParams.put(PROJECT_NAME, params.get(PROJECT_NAME).iterator().next());
            factoryUrlParams.put(WORKSPACE_NAME, params.get(WORKSPACE_NAME).iterator().next());

            return factoryUrlParams;
        } catch (IOException e) {
            throw new FactoryUrlException(e.getLocalizedMessage(), e);
        }
    }
}
