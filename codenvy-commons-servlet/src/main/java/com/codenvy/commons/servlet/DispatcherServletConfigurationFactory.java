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

/**
 * Sub-classes of this class should be added in file <pre>META-INF/services/com.codenvy.commons.servlet
 * .DispatcherServletConfigurationFactory</pre>.
 * See details in docs for {@link java.util.ServiceLoader}.
 *
 * @see DispatcherServletConfiguration
 * @see ConfigurationItem
 */
public abstract class DispatcherServletConfigurationFactory {
    public abstract DispatcherServletConfiguration newDispatcherServletConfiguration();
}
