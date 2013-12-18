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
package com.codenvy.inject.lifecycle;

import java.lang.reflect.Method;

/**
 * Helps to be more flexible when need handle errors of invocation destroy-methods.
 *
 * @author andrew00x
 */
public interface DestroyErrorHandler {
    void onError(Object instance, Method method, Throwable error);

    /**
     * Implementation of DestroyErrorHandler that ignore errors, e.g. such behaviour is required for annotation {@link
     * javax.annotation.PreDestroy}.
     */
    DestroyErrorHandler DUMMY = new DestroyErrorHandler() {
        @Override
        public void onError(Object instance, Method method, Throwable error) {
        }
    };
}
