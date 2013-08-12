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
