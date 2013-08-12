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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Summarizes set of {@link ConfigurationItem}.
 * <p/>
 * Example:
 * <pre>
 * DispatcherServletConfiguration.create()
 *                               .when(new Condition() {
 *                                   &#064Override
 *                                   public boolean matches(HttpServletRequest request, HttpServletResponse response) {
 *                                       // check first condition
 *                                   }
 *                               })
 *                               .execute(new Action() {
 *                                   &#064Override
 *                                   public void perform(HttpServletRequest request, HttpServletResponse response) {
 *                                       // perform action since condition above matches
 *                                   }
 *                               })
 *                               .done()
 *                               // Add more configurations if need
 * </pre>
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @see ConfigurationItem
 */
public final class DispatcherServletConfiguration implements Iterable<ConfigurationItem> {
    private final List<ConfigurationItem> configs = new ArrayList<>();

    public static DispatcherServletConfiguration create() {
        return new DispatcherServletConfiguration();
    }

    public ConfigurationItem when(Condition condition) {
        ConfigurationItem configuration = new ConfigurationItem(this);
        configuration.when(condition);
        configs.add(configuration);
        return configuration;
    }

    public ConfigurationItem execute(Action action) {
        ConfigurationItem configuration = new ConfigurationItem(this);
        configuration.execute(action);
        configs.add(configuration);
        return configuration;
    }

    public ConfigurationItem priority(int priority) {
        ConfigurationItem configuration = new ConfigurationItem(this);
        configuration.priority(priority);
        configs.add(configuration);
        return configuration;
    }

    @Override
    public Iterator<ConfigurationItem> iterator() {
        java.util.Collections.sort(configs);
        return new Iterator<ConfigurationItem>() {
            private final Iterator<ConfigurationItem> delegate = configs.iterator();

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public ConfigurationItem next() {
                return delegate.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private DispatcherServletConfiguration() {
    }
}
