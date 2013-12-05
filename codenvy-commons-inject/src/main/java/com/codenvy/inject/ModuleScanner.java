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

import org.scannotation.AnnotationDB;
import org.scannotation.WarUrlFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/** Utility for finding Guice modules annotated with @DynaModule */
public class ModuleScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleScanner.class);

    public static List<Module> findModules(ServletContext ctx) {

        URL classes = WarUrlFinder.findWebInfClassesPath(ctx);
        URL[] libs = WarUrlFinder.findWebInfLibClasspaths(ctx);
        AnnotationDB annotationDB = new AnnotationDB();
        List<String> skip = new ArrayList<>();
        //skip.add("org.everrest.core");
        //skip.add("javax.ws.rs");

        try {
            if (classes != null) {
                annotationDB.scanArchives(classes);
            }
            annotationDB.scanArchives(libs);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> results = annotationDB.getAnnotationIndex().get(DynaModule.class.getName());
        List<Module> modules = new ArrayList<>();

        if (results != null) {
            for (String module : results) {

                try {
                    Object mod = Class.forName(module).newInstance();
                    if (mod instanceof Module)
                        modules.add((Module)mod);
                    else
                        LOG.warn("Ignored non " + Module.class.getName() + " class annotated with " + DynaModule.class.getName());


                } catch (Exception e) {
                    LOG.error("Problem with instantiating Module {} : {}", module, e.getLocalizedMessage());
                }

            }
        }
        return modules;
    }

}
