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
