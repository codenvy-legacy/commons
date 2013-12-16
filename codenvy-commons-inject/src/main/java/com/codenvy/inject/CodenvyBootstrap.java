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

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.google.inject.util.Modules;

import org.everrest.guice.servlet.EverrestGuiceContextListener;
import org.everrest.guice.servlet.GuiceEverrestServlet;
import org.nnsoft.guice.rocoto.configuration.ConfigurationModule;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * CodenvyBootstrap is entry point of codenvy application implemented as ServletContextListener.
 * <ul>
 * <li>Initializes Guice Injector</li>
 * <li>Automatically binds all the subclasses of com.google.inject.Module annotated with &#064DynaModule</li>
 * <li>Loads configuration from .properties and .xml files located in /WEB-INF/classes/conf directory</li>
 * <li>Overrides it with external configuration located in directory pointed by CODENVY_LOCAL_CONF_DIR env variable (if any)</li>
 * <li>Binds all environment variables and system properties (visible as prefixed with "env.")</li>
 * <li>Thanks to Everrest integration injects all the properly annotated (see Everrest docs) REST Resources. Providers and ExceptionMappers
 * and inject necessary dependencies</li>
 * </ul>
 * <p/>
 * Configuration properties are bound as a &#064Named ConfigurationParameter. For example:
 * Following entry in the .property file:
 * {@code myProp=value}
 * may be injected into constructor (other options are valid too of course) as following:
 * <pre>
 * &#064Inject public MyClass(&#064Named("myProp") String my) {
 *
 * }
 * </pre>
 *
 * @author gazarenkov
 */
public class CodenvyBootstrap extends EverrestGuiceContextListener {

    private final List<Module> modules = new ArrayList<>();

    @Override
    protected List<Module> getModules() {
        // based on logic that getServletModule() is called BEFORE getModules() in the EverrestGuiceContextListener
        modules.add(new ConfigurationParameterConverter());
        modules.add(Modules.override(new WebInfConfiguration()).with(new ExtConfiguration()));
        return modules;
    }

    /** see http://google-guice.googlecode.com/git/javadoc/com/google/inject/servlet/ServletModule.html */
    @Override
    protected ServletModule getServletModule() {
        modules.addAll(ModuleScanner.findModules());
        return new CodenvyServletModule();
    }

    public static class CodenvyServletModule extends ServletModule {
        @Override
        protected void configureServlets() {
            // TODO add configuration for REST servlet mapping
            serve("/rest/*").with(GuiceEverrestServlet.class);
        }
    }

    /** ConfigurationModule binding configuration located in /WEB-INF/classes/conf directory */
    private static class WebInfConfiguration extends AbstractConfigurationModule {
        @Override
        protected void bindConfigurations() {
            URL parent = this.getClass().getClassLoader().getResource("conf");
            if (parent != null) {
                bindConf(new File(parent.getFile()));
            }
        }
    }

    /**
     * ConfigurationModule binding environment variables, system properties and configuration in directory pointed by
     * CODENVY_LOCAL_CONF_DIR Env variable.
     */
    private static class ExtConfiguration extends AbstractConfigurationModule {
        @Override
        protected void bindConfigurations() {
            // binds environment variables and system properties visible as prefixed with "env."
            bindEnvironmentVariables();
            bindSystemProperties();
            String extConfig = System.getenv().get("CODENVY_LOCAL_CONF_DIR");
            if (extConfig != null) {
                bindConf(new File(extConfig));
            }
        }
    }

    private static abstract class AbstractConfigurationModule extends ConfigurationModule {
        protected void bindConf(File conf) {
            final File[] files = conf.listFiles();
            if (files != null) {
                for (File f : files) {
                    final String ext = ext(f.getName());
                    if (!f.isDirectory()) {
                        if ("properties".equals(ext)) {
                            bindProperties(f);
                        } else if ("xml".equals(ext)) {
                            bindProperties(f).inXMLFormat();
                        }
                    }
                }
            }
        }

        private String ext(String fileName) {
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i + 1);
            }
            return extension;
        }
    }
}
