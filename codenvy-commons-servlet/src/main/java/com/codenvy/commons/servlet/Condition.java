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
