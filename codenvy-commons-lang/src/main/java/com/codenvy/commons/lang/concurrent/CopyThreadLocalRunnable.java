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

/** @author andrew00x */
class CopyThreadLocalRunnable implements Runnable {
    private final Runnable                                     wrapped;
    private final ThreadLocalPropagateContext.ThreadLocalState threadLocalState;

    CopyThreadLocalRunnable(Runnable wrapped) {
        // Called from main thread. Copy the current values of all the ThreadLocal variables which registered in ThreadLocalPropagateContext.
        this.wrapped = wrapped;
        this.threadLocalState = ThreadLocalPropagateContext.currentThreadState();
    }

    @Override
    public void run() {
        try {
            threadLocalState.propagate();
            wrapped.run();
        } finally {
            threadLocalState.cleanup();
        }
    }

    public Runnable getWrapped() {
        return wrapped;
    }
}
