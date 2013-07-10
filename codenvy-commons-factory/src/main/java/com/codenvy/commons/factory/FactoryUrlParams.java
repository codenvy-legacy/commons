/*
 * Copyright (C) 2013 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.codenvy.commons.factory;

import java.util.HashMap;

/**
 * Class holds Factory url parameters values
 */
public class FactoryUrlParams extends HashMap<String, String> {
    /** known factory url parameters */
    public static String VERSION        = "v";
    public static String VCS            = "vcs";
    public static String VCS_URL        = "vcsurl";
    public static String ID_COMMIT      = "idcommit";
    public static String PROJECT_NAME   = "pname";
    public static String WORKSPACE_NAME = "wname";
}
