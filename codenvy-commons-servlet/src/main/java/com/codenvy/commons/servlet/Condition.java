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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Describe condition which should match to make possible perform related Action. */
public abstract class Condition {

    /** Condition which never matches. */
    public static final Condition NOT_MATCH = new Condition() {
        @Override
        public boolean matches(HttpServletRequest request, HttpServletResponse response) {
            return false;
        }
    };

    /** Condition which always matches. */
    public static final Condition MATCH = new Condition() {
        @Override
        public boolean matches(HttpServletRequest request, HttpServletResponse response) {
            return true;
        }
    };

    private static class And extends Condition {
        private final Condition[] conditions;

        private And(Condition[] conditions) {
            this.conditions = conditions;
        }

        @Override
        public boolean matches(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            for (Condition condition : conditions) {
                if (!condition.matches(request, response)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class Or extends Condition {
        private final Condition[] conditions;

        private Or(Condition[] conditions) {
            this.conditions = conditions;
        }

        @Override
        public boolean matches(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            for (Condition condition : conditions) {
                if (condition.matches(request, response)) {
                    return true;
                }
            }
            return false;
        }
    }

    /** @see #and(Condition...) */
    public static Condition and(Condition condition1, Condition condition2) {
        return new And(new Condition[]{condition1, condition2});
    }

    /** @see #and(Condition...) */
    public static Condition and(Condition condition1, Condition condition2, Condition condition3) {
        return new And(new Condition[]{condition1, condition2, condition3});
    }

    /** @see #and(Condition...) */
    public static Condition and(Condition condition1, Condition condition2, Condition condition3, Condition condition4) {
        return new And(new Condition[]{condition1, condition2, condition2, condition3, condition4});
    }

    /**
     * Create one 'AND' condition based on set of specified conditions. Method throws IllegalArgumentException if array contains less then
     * 2
     * conditions.
     *
     * @param conditions
     *         original set of conditions
     * @return 'AND' condition
     */
    public static Condition and(Condition... conditions) {
        return new And(copy(conditions));
    }

    /** @see #or(Condition...) */
    public static Condition or(Condition condition1, Condition condition2) {
        return new Or(new Condition[]{condition1, condition2});
    }

    /** @see #or(Condition...) */
    public static Condition or(Condition condition1, Condition condition2, Condition condition3) {
        return new Or(new Condition[]{condition1, condition2, condition3});
    }

    /** @see #or(Condition...) */
    public static Condition or(Condition condition1, Condition condition2, Condition condition3, Condition condition4) {
        return new Or(new Condition[]{condition1, condition2, condition2, condition3, condition4});
    }

    /**
     * Create one 'OR' condition based on set of specified conditions. Method throws IllegalArgumentException if array contains less then
     * 2
     * conditions.
     *
     * @param conditions
     *         original set of conditions
     * @return 'OR' condition
     */
    public static Condition or(Condition... conditions) {
        return new Or(copy(conditions));
    }

    private static Condition[] copy(Condition... conditions) {
        if (conditions.length < 2) {
            throw new IllegalArgumentException("Need at least two Conditions. ");
        }
        final Condition[] copy = new Condition[conditions.length];
        System.arraycopy(conditions, 0, copy, 0, copy.length);
        return copy;
    }

    public abstract boolean matches(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
