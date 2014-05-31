/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.commons.servlet;

/**
 * Item of configuration for DispatcherServlet.
 * <p/>
 * Usage example:
 * <pre>
 * public class MyDispatcherServletConfigurationFactory extends DispatcherServletConfigurationFactory {
 *     &#064Override
 *     public DispatcherServletConfiguration newDispatcherServletConfiguration() {
 *         return DispatcherServletConfiguration.create()
 *                                              .when(new Condition() {
 *                                                  &#064Override
 *                                                  public boolean matches(HttpServletRequest request, HttpServletResponse response) {
 *                                                      // check first condition
 *                                                  }
 *                                              })
 *                                              .execute(new Action() {
 *                                                  &#064Override
 *                                                  public void perform(HttpServletRequest request, HttpServletResponse response) {
 *                                                      // perform action since condition above matches
 *                                                  }
 *                                              })
 *                                              .done()
 *                                              .when(new Condition() {
 *                                                  &#064Override
 *                                                  public boolean matches(HttpServletRequest request, HttpServletResponse response) {
 *                                                      // check second condition
 *                                                  }
 *                                              })
 *                                              .execute(new Action() {
 *                                                  &#064Override
 *                                                  public void perform(HttpServletRequest request, HttpServletResponse response) {
 *                                                      // perform second action since condition above matches
 *                                                  }
 *                                              })
 *                                              // Add more conditions and actions here.
 *                                              // All action performed in specified order (if priority is not set) util first one reports
 *                                              // about matching to request criteria.
 *                                             .done();
 *     }
 * }
 * </pre>
 */
public final class ConfigurationItem implements Comparable<ConfigurationItem> {
    private DispatcherServletConfiguration all;
    private Condition condition = Condition.NOT_MATCH;
    private Action    action    = Action.DUMMY;
    private int       priority  = 0;

    ConfigurationItem(DispatcherServletConfiguration all) {
        this.all = all;
    }

    public ConfigurationItem when(Condition condition) {
        if (condition == null) {
            this.condition = Condition.NOT_MATCH;
        } else {
            this.condition = condition;
        }
        return this;
    }

    public ConfigurationItem execute(Action action) {
        if (action == null) {
            this.action = Action.DUMMY;
        } else {
            this.action = action;
        }
        return this;
    }

    /**
     * Set priority of this condition. Finally all conditions are sorted by priority before usage. Less value of priority means earlier
     * usage it in cycle of checking conditions.
     *
     * @param priority
     *         value of priority
     * @return this instance
     * @throws IllegalArgumentException
     *         if specified {@code priority} is negative
     */
    public ConfigurationItem priority(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException("negative 'priority' is not allowed");
        }
        this.priority = priority;
        return this;
    }

    public DispatcherServletConfiguration done() {
        return all;
    }

    public Condition getCondition() {
        return condition;
    }

    public Action getAction() {
        return action;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(ConfigurationItem o) {
        return priority - o.priority;
    }
}
