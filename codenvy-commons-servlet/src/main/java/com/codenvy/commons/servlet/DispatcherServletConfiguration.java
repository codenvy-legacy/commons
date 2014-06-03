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
