/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.schedule;

import com.codenvy.commons.schedule.executor.ThreadPullLauncher;
import com.codenvy.inject.DynaModule;
import com.codenvy.inject.lifecycle.ScheduleModule;
import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Guice deployment module.
 *
 * @author Sergii Kabashniuk
 */
@DynaModule
public class InstallModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Launcher.class).to(ThreadPullLauncher.class).asEagerSingleton();
        binder.install(new ScheduleModule());
    }
}
