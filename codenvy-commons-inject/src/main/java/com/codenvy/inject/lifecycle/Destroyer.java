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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/** @author andrew00x */
public final class Destroyer {
    // Don't prevent instance from being discarded by the garbage collector.
    private final WeakHashMap<Object, Method[]> map;
    private final DestroyErrorHandler           errorHandler;

    public Destroyer(DestroyErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        map = new WeakHashMap<>();
    }

    public void add(Object instance, Method[] m) {
        synchronized (map) {
            map.put(instance, m);
        }
    }

    public void destroy() {
        synchronized (map) {
            for (Map.Entry<Object, Method[]> entry : map.entrySet()) {
                final Object instance = entry.getKey();
                final Method[] methods = entry.getValue();
                for (Method method : methods) {
                    try {
                        method.invoke(instance);
                    } catch (IllegalArgumentException e) {
                        // method MUST NOT have any parameters
                        errorHandler.onError(instance, method, e);
                    } catch (IllegalAccessException e) {
                        errorHandler.onError(instance, method, e);
                    } catch (InvocationTargetException e) {
                        errorHandler.onError(instance, method, e.getTargetException());
                    }
                }
            }
        }
    }
}
