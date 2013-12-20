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
package com.codenvy.inject.lifecycle;

import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** @author andrew00x */
public final class InitModule extends LifecycleModule {
    private final Class<? extends Annotation> annotationType;

    public InitModule(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
                encounter.register(new InjectionListener<T>() {
                    @Override
                    public void afterInjection(T injectee) {
                        final Method[] methods = get(injectee.getClass(), annotationType);
                        if (methods.length > 0) {
                            for (Method method : methods) {
                                try {
                                    method.invoke(injectee);
                                } catch (IllegalArgumentException e) {
                                    // method MUST NOT have any parameters
                                    throw new ProvisionException(e.getMessage(), e);
                                } catch (IllegalAccessException e) {
                                    throw new ProvisionException(String.format("Failed access to %s on %s", method, injectee), e);
                                } catch (InvocationTargetException e) {
                                    final Throwable cause = e.getTargetException();
                                    throw new ProvisionException(String.format("Invocation error of method %s on %s", method, injectee),
                                                                 cause);
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}
