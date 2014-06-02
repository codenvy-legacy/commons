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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action which may be performed is related Condition is matched.
 *
 * @see ConfigurationItem
 * @see Condition
 */
public abstract class Action {

    /** Dummy Action. Do nothing. */
    public static final Action DUMMY = new Action() {
        @Override
        public void perform(HttpServletRequest request, HttpServletResponse response) {
        }
    };

    public abstract void perform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
