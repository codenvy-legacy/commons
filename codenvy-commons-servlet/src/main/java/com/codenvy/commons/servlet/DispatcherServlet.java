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
package com.codenvy.commons.servlet;


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
        String requestPath = request.getPathInfo();
        LOG.debug("request path: {}", requestPath);

        String workspace = null;
        final int length = requestPath.length();
        int p = 1;
        int n = requestPath.indexOf('/', p);
        if (n < 0) {
            n = length;
        }
        final String tmp = requestPath.substring(p, n);
        if (!tmp.isEmpty()) {
            workspace = tmp;
        }

        request.setAttribute("ws", workspace);
        LOG.debug("workspace: {}", workspace);

        for (ConfigurationItem configuration : configurations) {
            if (configuration.getCondition().matches(request, response)) {
                configuration.getAction().perform(request, response);
                return;
            }
        }
    }

    public static String genIdeStaticResourceUrl(HttpServletRequest request, String name) {
        return request.getContextPath() + '/' + request.getAttribute("ws") + "/_ide/" + name;
    }

    public static String genShellStaticResourceUrl(HttpServletRequest request, String name) {
        return request.getContextPath() + '/' + request.getAttribute("ws") + "/_shell/" + name;
    }
}
