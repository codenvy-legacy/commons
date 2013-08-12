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
package com.codenvy.commons.env;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Defines a component that holds variables of type {@link ThreadLocal}
 * whose value is required by the component to work normally and cannot be recovered.
 * This component is mainly used when we want to do a task asynchronously, in that case
 * to ensure that the task will be executed in the same conditions as if it would be
 * executed synchronously we need to transfer the thread context from the original
 * thread to the executor thread.</p>
 */
public class EnvironmentContext {
    /** Host name of current environment. */
    public final static String MASTERHOST_NAME = "com.codenvy.masterhost.name";

    /** Server port used for current environment. */
    public final static String MASTERHOST_PORT = "com.codenvy.masterhost.port";

    /** URL to master host including protocol and port */
    public final static String MASTERHOST_URL= "com.codenvy.masterhost.url";

    public final static String WORKSPACE_ID = "com.codenvy.workspace.id";

    public final static String WORKSPACE_NAME = "com.codenvy.workspace.name";

    /** URL to current workspace host including protocol and port */
    public final static String WORKSPACE_URL= "com.codenvy.workspace.url";

    public final static String VFS_ROOT_DIR = "com.codenvy.vfs.rootdir";

    public final static String VFS_INDEX_DIR = "com.codenvy.vfs.indexdir";

    public final static String TMP_DIR = "com.codenvy.tmpdir";

    /** Name of web application that gives access to source files over Git. */
    public final static String GIT_SERVER = "com.codenvy.git.server.application";

    /** ThreadLocal keeper for EnvironmentContext. */
    private static ThreadLocal<EnvironmentContext> current = new ThreadLocal<EnvironmentContext>() {
        @Override
        protected EnvironmentContext initialValue() {
            return new EnvironmentContext();
        }
    };

    public static EnvironmentContext getCurrent() {
        return current.get();
    }

    public static void setCurrent(EnvironmentContext environment) {
        current.set(environment);
    }

    public static void reset() {
        current.remove();
    }

    //

    private Map<String, Object> environment;

    public EnvironmentContext() {
        environment = new HashMap<String, Object>();
    }

    public void setVariable(String name, Object value) {
        environment.put(name, value);
    }

    public Object getVariable(String name) {
        return environment.get(name);
    }
}
