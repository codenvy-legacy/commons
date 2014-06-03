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
package com.codenvy.commons.servlet;

import com.codenvy.commons.env.EnvironmentContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<ConfigurationItem> configurations = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        super.init();
        for (DispatcherServletConfigurationFactory f : ServiceLoader.load(DispatcherServletConfigurationFactory.class)) {
            int i = 0;
            for (ConfigurationItem configuration : f.newDispatcherServletConfiguration()) {
                configurations.add(configuration);
                i++;
            }
            LOG.info("Loaded {} ConfigurationItem's from {} ", i, f.getClass().getName());
        }
        if (configurations.isEmpty()) {
            throw new ServletException("Not found configuration. ");
        }

        java.util.Collections.sort(configurations);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("request path: {}", request.getPathInfo());
        request.setAttribute("wsName", EnvironmentContext.getCurrent().getWorkspaceName());
        request.setAttribute("wsId", EnvironmentContext.getCurrent().getWorkspaceId());
        for (ConfigurationItem configuration : configurations) {
            if (configuration.getCondition().matches(request, response)) {
                configuration.getAction().perform(request, response);
                return;
            }
        }
    }

    public static String genIdeStaticResourceUrl(HttpServletRequest request, String name) {
        return request.getContextPath() + '/' + request.getAttribute("wsName") + "/_ide/" + name;
    }

    public static String genShellStaticResourceUrl(HttpServletRequest request, String name) {
        return request.getContextPath() + '/' + request.getAttribute("wsName") + "/_shell/" + name;
    }
}
