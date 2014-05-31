/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.logreader;

public class LogReaderException extends Exception {

    private final String message;

    private final Throwable cause;

    public LogReaderException(String message) {
        this.message = message;
        this.cause = null;
    }

    public LogReaderException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public String getLocalizedMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }

}
