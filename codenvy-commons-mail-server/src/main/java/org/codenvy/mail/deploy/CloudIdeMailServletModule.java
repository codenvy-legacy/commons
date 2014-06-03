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
package org.codenvy.mail.deploy;


import com.codenvy.inject.DynaModule;
import com.google.inject.servlet.ServletModule;

import org.everrest.guice.servlet.GuiceEverrestServlet;

/**
 * Servlet module composer for api war.
 *
 */

@DynaModule
public class CloudIdeMailServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/*").with(GuiceEverrestServlet.class);
    }
}