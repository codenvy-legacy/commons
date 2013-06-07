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
    private final List<ConfigurationItem> configs = new ArrayList<ConfigurationItem>();

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
