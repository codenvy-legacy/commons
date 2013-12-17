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
import com.google.inject.multibindings.Multibinder;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

/** @author andrew00x */
public class MultiBindingTest {
    Injector injector;

    @BeforeTest
    public void init() {
        injector = Guice.createInjector(new MyModule(), new ServiceModule1(), new ServiceModule2(), new ServiceModule3());
    }

    @Test
    public void testMultiInject() {
        TestComponent instance = injector.getInstance(TestComponent.class);
        Set<Service> services = instance.services;
        Set<String> names = new HashSet<>(services.size());
        for (Service service : services) {
            names.add(service.toString());
        }
        Assert.assertEquals(names.size(), 3);
        Assert.assertTrue(names.contains("Service1"));
        Assert.assertTrue(names.contains("Service2"));
        Assert.assertTrue(names.contains("Service3"));
    }

    public static class MyModule implements Module {
        @Override
        public void configure(Binder binder) {
            binder.bind(TestComponent.class);
        }
    }

    public static class ServiceModule1 implements Module {
        @Override
        public void configure(Binder binder) {
            Multibinder<Service> multiBinder = Multibinder.newSetBinder(binder, Service.class);
            multiBinder.addBinding().to(Service1.class);
        }
    }

    public static class ServiceModule2 implements Module {
        @Override
        public void configure(Binder binder) {
            Multibinder<Service> multiBinder = Multibinder.newSetBinder(binder, Service.class);
            multiBinder.addBinding().to(Service2.class);
        }
    }

    public static class ServiceModule3 implements Module {
        @Override
        public void configure(Binder binder) {
            Multibinder<Service> multiBinder = Multibinder.newSetBinder(binder, Service.class);
            multiBinder.addBinding().to(Service3.class);
        }
    }

    @Singleton
    public static class TestComponent {
        private final Set<Service> services;

        @Inject
        public TestComponent(Set<Service> services) {
            this.services = services;
        }
    }

    public static interface Service {
    }

    @Singleton
    public static class Service1 implements Service {
        public String toString() {
            return "Service1";
        }
    }

    @Singleton
    public static class Service2 implements Service {
        public String toString() {
            return "Service2";
        }
    }

    @Singleton
    public static class Service3 implements Service {
        public String toString() {
            return "Service3";
        }
    }
}
