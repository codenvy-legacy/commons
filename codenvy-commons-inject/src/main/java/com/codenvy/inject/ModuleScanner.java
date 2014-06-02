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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Utility for finding Guice modules annotated with &#064DynaModule. */
@HandlesTypes({DynaModule.class})
public class ModuleScanner implements ServletContainerInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(ModuleScanner.class);

    private static final List<Module> modules = new ArrayList<>();

    public static List<Module> findModules() {
        return new ArrayList<>(modules);
    }

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        if (c != null) {
            for (Class<?> clazz : c) {
                if (Module.class.isAssignableFrom(clazz)) {
                    try {
                        modules.add((Module)clazz.newInstance());
                    } catch (Exception e) {
                        LOG.error("Problem with instantiating Module {} : {}", clazz, e.getMessage());
                    }
                } else {
                    LOG.warn("Ignored non {} class annotated with {}", Module.class.getName(), DynaModule.class.getName());
                }
            }
        }
    }
}
