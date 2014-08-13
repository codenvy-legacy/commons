/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.env;

import com.codenvy.commons.lang.concurrent.ThreadLocalPropagateContext;
import com.codenvy.commons.user.User;

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
    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String MASTERHOST_NAME = "com.codenvy.masterhost.name";

    /** Server port used for current environment. */
    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String MASTERHOST_PORT = "com.codenvy.masterhost.port";

    /** URL to master host including protocol and port */
    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String MASTERHOST_URL = "com.codenvy.masterhost.url";

    /** URL to current workspace host including protocol and port */
    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String WORKSPACE_URL = "com.codenvy.workspace.url";

    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String VFS_ROOT_DIR = "com.codenvy.vfs.rootdir";

    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String VFS_INDEX_DIR = "com.codenvy.vfs.indexdir";

    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String TMP_DIR = "com.codenvy.tmpdir";

    /** Name of web application that gives access to source files over Git. */
    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public final static String GIT_SERVER = "com.codenvy.git.server.application";

    /** ThreadLocal keeper for EnvironmentContext. */
    private static ThreadLocal<EnvironmentContext> current = new ThreadLocal<EnvironmentContext>() {
        @Override
        protected EnvironmentContext initialValue() {
            return new EnvironmentContext();
        }
    };

    static {
        ThreadLocalPropagateContext.addThreadLocal(current);
    }

    public static EnvironmentContext getCurrent() {
        return current.get();
    }

    public static void setCurrent(EnvironmentContext environment) {
        current.set(environment);
    }

    public static void reset() {
        current.remove();
    }


    private Map<String, Object> environment;

    private User user;

    private String workspaceName;

    private String workspaceId;

    private boolean workspaceTemporary;

    private String accountId;

    public EnvironmentContext() {
        environment = new HashMap<>();
    }

    public EnvironmentContext(EnvironmentContext other) {
        environment = new HashMap<>(other.environment);
        setUser(other.getUser());
        setWorkspaceName(other.getWorkspaceName());
        setWorkspaceId(other.getWorkspaceId());
        setAccountId(other.getAccountId());
        setWorkspaceTemporary(other.isWorkspaceTemporary());
    }

    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    void setVariable(String name, Object value) {
        environment.put(name, value);
    }

    /**
     * Only for IDE2 support
     *
     * @deprecated
     */
    @Deprecated
    public Object getVariable(String name) {
        return environment.get(name);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isWorkspaceTemporary() {
        return workspaceTemporary;
    }

    public void setWorkspaceTemporary(boolean workspaceTemporary) {
        this.workspaceTemporary = workspaceTemporary;
    }
}
