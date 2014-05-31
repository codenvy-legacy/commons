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
package org.codenvy.mail.deploy;

import com.codenvy.inject.DynaModule;
import com.google.inject.AbstractModule;

import org.codenvy.mail.MailSender;
import org.codenvy.mail.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Guice container configuration file. Replaces old REST application composers and servlet context listeners.
 *
 */
@DynaModule
public class CloudIdeMailModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(CloudIdeMailModule.class);

    @Override
    protected void configure() {
        bind(MailSender.class);
    }
}
