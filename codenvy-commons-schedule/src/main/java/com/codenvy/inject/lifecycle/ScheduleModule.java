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
package com.codenvy.inject.lifecycle;

import com.codenvy.commons.schedule.Launcher;
import com.codenvy.commons.schedule.ScheduleCron;
import com.codenvy.commons.schedule.ScheduleDelay;
import com.codenvy.commons.schedule.ScheduleRate;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Launch  method marked with @ScheduleCron @ScheduleDelay and @ScheduleRate annotations using  Launcher
 *
 * @author Sergii Kabashniuk
 */
public class ScheduleModule extends LifecycleModule {

    @Override
    protected void configure() {
        final Provider<Launcher> launcher = getProvider(Launcher.class);
        final Provider<Injector> injector = getProvider(Injector.class);
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <T> void hear(final TypeLiteral<T> type, final TypeEncounter<T> encounter) {
                encounter.register(new ScheduleInjectionListener<T>(launcher, injector));
            }
        });
    }


}
