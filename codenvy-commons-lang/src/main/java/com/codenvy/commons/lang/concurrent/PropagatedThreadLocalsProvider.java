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
package com.codenvy.commons.lang.concurrent;

/**
 * Helps to identify classes which need propagating ThreadLocal variables with ThreadLocalPropagateContext. Useful to avoid manual
 * registration of ThreadLocal in ThreadLocalPropagateContext.
 * <pre>
 * Set&lt;PropagatedThreadLocalsProvider&gt; ps = ... // Look up all implementations of PropagatedThreadLocalsProvider
 * for (PropagatedThreadLocalsProvider p : ps) {
 *     for (ThreadLocal tl : p.getThreadLocals()) {
 *         ThreadLocalPropagateContext.addThreadLocal(tl);
 *     }
 * }
 * </pre>
 *
 * @author andrew00x
 */
public interface PropagatedThreadLocalsProvider {
    ThreadLocal<?>[] getThreadLocals();
}
