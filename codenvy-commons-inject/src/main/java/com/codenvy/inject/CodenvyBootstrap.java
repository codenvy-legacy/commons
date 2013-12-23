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
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.google.inject.util.Modules;

import org.everrest.guice.servlet.EverrestGuiceContextListener;
import org.nnsoft.guice.rocoto.configuration.ConfigurationModule;
import org.nnsoft.guice.rocoto.converters.FileConverter;
import org.nnsoft.guice.rocoto.converters.URIConverter;
import org.nnsoft.guice.rocoto.converters.URLConverter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CodenvyBootstrap is entry point of codenvy application implemented as ServletContextListener.
 * <ul>
 * <li>Initializes Guice Injector</li>
 * <li>Automatically binds all the subclasses of com.google.inject.Module annotated with &#064DynaModule</li>
 * <li>Loads configuration from .properties and .xml files located in <i>/WEB-INF/classes/conf</i> directory</li>
 * <li>Overrides it with external configuration located in directory pointed by <i>CODENVY_LOCAL_CONF_DIR</i> env variable (if any)</li>
 * <li>Binds all environment variables and system properties (visible as prefixed with "env.")</li>
 * <li>Thanks to Everrest integration injects all the properly annotated (see Everrest docs) REST Resources. Providers and ExceptionMappers
 * and inject necessary dependencies</li>
 * </ul>
 * <p/>
 * Configuration properties are bound as a {@code &#064Named}. For example:
 * Following entry in the .property file:
 * {@code myProp=value}
 * may be injected into constructor (other options are valid too of course) as following:
 * <pre>
 * &#064Inject
 * public MyClass(&#064Named("myProp") String my) {
 * }
 * </pre>
 * <p/>
 * It's possible to use system properties or environment variables in .properties files.
 * <pre>
 * my_app.input_dir=${root_data}/input/
 * my_app.output_dir=${root_data}/output/
 * </pre>
 * NOTE: System property always takes preference on environment variable with the same name.
 * <p/>
 * <table>
 * <tr><th>Value</th><th>System property</th><th>Environment variable</th><th>Result</th></tr>
 * <tr><td>${root_data}/input/</td><td>/home/andrew/temp</td><td>&nbsp;</td><td>/home/andrew/temp/input/</td></tr>
 * <tr><td>${root_data}/input/</td><td>&nbsp;</td><td>/usr/local</td><td>/usr/local/input/</td></tr>
 * <tr><td>${root_data}/input/</td><td>/home/andrew/temp</td><td>/usr/local</td><td>/home/andrew/temp/input/</td></tr>
 * <tr><td>${root_data}/input/</td><td>&nbsp;</td><td>&nbsp;</td><td>${root_data}/input/</td></tr>
 * </table>
 *
 * @author gazarenkov
 * @author andrew00x
 */
public class CodenvyBootstrap extends EverrestGuiceContextListener {
    private final List<Module> modules = new ArrayList<>();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        final ServletContext ctx = sce.getServletContext();
        final Injector injector = getInjector(ctx);
        injector.getInstance(Destroyer.class).destroy();
        super.contextDestroyed(sce);
    }

    @Override
    protected List<Module> getModules() {
        // based on logic that getServletModule() is called BEFORE getModules() in the EverrestGuiceContextListener
        modules.add(new InitModule(PostConstruct.class));
        modules.add(new DestroyModule(PreDestroy.class, DestroyErrorHandler.DUMMY));
        modules.add(new URIConverter());
        modules.add(new URLConverter());
        modules.add(new FileConverter());
        modules.add(new StringArrayConverter());
        modules.addAll(ModuleScanner.findModules());
        modules.add(Modules.override(new WebInfConfiguration()).with(new ExtConfiguration()));
        return modules;
    }

    /** see http://google-guice.googlecode.com/git/javadoc/com/google/inject/servlet/ServletModule.html */
    @Override
    protected ServletModule getServletModule() {
        // Servlets and other web components may be configured with custom Modules.
        return null; //return new CodenvyServletModule();
    }

    /*public static class CodenvyServletModule extends ServletModule {
        @Override
        protected void configureServlets() {
            // TODO add configuration for REST servlet mapping
            serve("/rest/*").with(GuiceEverrestServlet.class);
        }
    }*/

    /** ConfigurationModule binding configuration located in <i>/WEB-INF/classes/conf</i> directory */
    static class WebInfConfiguration extends AbstractConfigurationModule {
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
     * <i>CODENVY_LOCAL_CONF_DIR</i> Env variable.
     */
    static class ExtConfiguration extends AbstractConfigurationModule {
        @Override
        protected void bindConfigurations() {
            // binds environment variables and system properties visible as prefixed with "env."
            bindEnvironmentVariables();
            bindSystemProperties();
            String extConfig = System.getenv("CODENVY_LOCAL_CONF_DIR");
            if (extConfig != null) {
                bindConf(new File(extConfig));
            }
        }
    }

    private static final Pattern PATTERN = Pattern.compile("\\$\\{[^\\}^\\$\\{]+\\}");

    static abstract class AbstractConfigurationModule extends ConfigurationModule {
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

        @Override
        protected void bindProperties(Iterator<Map.Entry<String, String>> properties) {
            StringBuilder buf = null;
            while (properties.hasNext()) {
                Map.Entry<String, String> property = properties.next();
                String value = property.getValue();
                final Matcher matcher = PATTERN.matcher(value);
                if (matcher.find()) {
                    int start = 0;
                    if (buf == null) {
                        buf = new StringBuilder();
                    } else {
                        buf.setLength(0);
                    }
                    do {
                        final int i = matcher.start();
                        final int j = matcher.end();
                        buf.append(value.substring(start, i));
                        final String template = value.substring(i, j);
                        final String name = value.substring(i + 2, j - 1);
                        String actual = System.getProperty(name);
                        if (actual == null) {
                            actual = System.getenv(name);
                        }
                        if (actual == null) {
                            actual = template;
                        }
                        buf.append(actual);
                        start = matcher.end();
                    } while (matcher.find());
                    buf.append(value.substring(start));
                    value = buf.toString();
                }
                bindProperty(property.getKey()).toValue(value);
            }
        }
    }
}
