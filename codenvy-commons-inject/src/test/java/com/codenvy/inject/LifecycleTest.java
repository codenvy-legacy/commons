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
package com.codenvy.inject;

import com.codenvy.inject.lifecycle.DestroyErrorHandler;
import com.codenvy.inject.lifecycle.DestroyModule;
import com.codenvy.inject.lifecycle.Destroyer;
import com.codenvy.inject.lifecycle.InitModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/** @author andrew00x */
public class LifecycleTest {
    Injector injector;

    @BeforeTest
    public void init() {
        injector = Guice.createInjector(new InitModule(PostConstruct.class),
                                        new DestroyModule(PreDestroy.class, DestroyErrorHandler.DUMMY),
                                        new MyModule());
    }

    @Test
    public void testInit() {
        TestComponent component = injector.getInstance(TestComponent.class);
        Assert.assertEquals(component.init, 1, "'init' method must be called just once");
    }

    @Test
    public void testDestroy() {
        TestComponent component = injector.getInstance(TestComponent.class);
        injector.getInstance(Destroyer.class).destroy();
        Assert.assertEquals(component.destroy, 1, "'destroy' method must be called just once");
    }

    public static class MyModule implements Module {
        @Override
        public void configure(Binder binder) {
            binder.bind(TestComponent.class);
        }
    }

    public static abstract class SuperClass {
        int init;
        int destroy;

        @PostConstruct
        public void init() {
            init++;
        }

        @PreDestroy
        public void destroy() {
            destroy++;
        }
    }

    @Singleton
    public static class TestComponent extends SuperClass {
        @Inject
        public TestComponent() {
        }

        @PostConstruct
        public void init() {
            super.init();
        }

        @PreDestroy
        public void destroy() {
            super.destroy();
        }
    }
}
