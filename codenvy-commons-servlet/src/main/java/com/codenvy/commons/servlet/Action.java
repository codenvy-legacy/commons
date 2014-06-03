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
