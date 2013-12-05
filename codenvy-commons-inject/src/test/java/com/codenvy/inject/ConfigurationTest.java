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
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

import org.nnsoft.guice.rocoto.configuration.ConfigurationModule;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Named;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

/** @author <a href="mailto:aparfonov@codenvy.com">Andrey Parfonov</a> */
public class ConfigurationTest {

    Injector injector;

    @BeforeTest
    public void init() {
        final Properties props = new Properties();
        props.put("test_int", "123");
        props.put("test_bool", "true");
        props.put("test_uri", "file:/a/b/c");
        props.put("test_url", "http://localhost");
        props.put("test_file", "/a/b/c");
        props.put("test_strings", "a, b, c");
        injector = Guice.createInjector(new ConfigurationParameterConverter(),
                                        new ConfigurationModule() {
                                            @Override
                                            protected void bindConfigurations() {
                                                bindProperties(props);
                                            }
                                        },
                                        new MyModule());
    }

    @Test
    public void testConvertInt() {
        Assert.assertEquals(injector.getInstance(TestComponent.class).parameter_int.asInt(), 123);
    }

    @Test
    public void testConvertBoolean() {
        Assert.assertEquals(injector.getInstance(TestComponent.class).parameter_bool.asBoolean(), true);
    }

    @Test
    public void testConvertUri() {
        Assert.assertEquals(injector.getInstance(TestComponent.class).parameter_uri.asURI(), URI.create("file:/a/b/c"));
    }

    @Test
    public void testConvertUrl() throws MalformedURLException {
        Assert.assertEquals(injector.getInstance(TestComponent.class).parameter_url.asURL(), new URL("http://localhost"));
    }

    @Test
    public void testConvertFile() {
        Assert.assertEquals(injector.getInstance(TestComponent.class).parameter_file.asFile(), new java.io.File("/a/b/c"));
    }

    @Test
    public void testConvertStrings() {
        Assert.assertEquals(injector.getInstance(TestComponent.class).parameter_strings.asStrings(), Arrays.asList("a", "b", "c"));
    }

    public static class MyModule implements Module {
        @Override
        public void configure(Binder binder) {
            binder.bind(TestComponent.class);
        }
    }

    public static class TestComponent {
        @Named("test_int")
        @Inject
        ConfigurationParameter parameter_int;

        @Named("test_bool")
        @Inject
        ConfigurationParameter parameter_bool;

        @Named("test_uri")
        @Inject
        ConfigurationParameter parameter_uri;

        @Named("test_url")
        @Inject
        ConfigurationParameter parameter_url;

        @Named("test_file")
        @Inject
        ConfigurationParameter parameter_file;

        @Named("test_strings")
        @Inject
        ConfigurationParameter parameter_strings;
    }
}
