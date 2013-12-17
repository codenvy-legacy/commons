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

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

import org.nnsoft.guice.lifegycle.AfterInjectionModule;
import org.nnsoft.guice.lifegycle.DisposeModule;
import org.nnsoft.guice.lifegycle.Disposer;
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
        injector = Guice.createInjector(new AfterInjectionModule(PostConstruct.class, Matchers.any()),
                                        new DisposeModule(PreDestroy.class, Matchers.any()),
                                        new MyModule());
    }

    @Test
    public void testInit() {
        Assert.assertTrue(injector.getInstance(TestComponent.class).init);
    }

    @Test
    public void testDestroy() {
        final TestComponent component = injector.getInstance(TestComponent.class);
        injector.getInstance(Disposer.class).dispose();
        Assert.assertTrue(component.destroy);
    }

    public static class MyModule implements Module {
        @Override
        public void configure(Binder binder) {
            binder.bind(TestComponent.class);
        }
    }

    @Singleton
    public static class TestComponent {
        private boolean init;
        private boolean destroy;

        @Inject
        public TestComponent() {
        }

        @PostConstruct
        public void init() {
            init = true;
        }

        @PreDestroy
        public void destroy() {
            destroy = true;
        }
    }
}
