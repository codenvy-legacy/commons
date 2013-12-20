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

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/** @author andrew00x */
public final class DestroyModule extends LifecycleModule {
    private final Class<? extends Annotation> annotationType;
    private final DestroyErrorHandler         errorHandler;

    public DestroyModule(Class<? extends Annotation> annotationType, DestroyErrorHandler errorHandler) {
        this.annotationType = annotationType;
        this.errorHandler = errorHandler;
    }

    @Override
    protected void configure() {
        final Destroyer destroyer = new Destroyer(errorHandler);
        bind(Destroyer.class).toInstance(destroyer);
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
                encounter.register(new InjectionListener<T>() {
                    @Override
                    public void afterInjection(T injectee) {
                        final Method[] methods = get(injectee.getClass(), annotationType);
                        if (methods.length > 0) {
                            // copy array when pass it outside
                            final Method[] copy = new Method[methods.length];
                            System.arraycopy(methods, 0, copy, 0, methods.length);
                            destroyer.add(injectee, copy);
                        }
                    }
                });
            }
        });
    }
}
