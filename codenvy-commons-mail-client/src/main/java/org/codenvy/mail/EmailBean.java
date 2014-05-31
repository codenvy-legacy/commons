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
package org.codenvy.mail;

import java.io.Serializable;

public class EmailBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String from;

    private String to;

    private String mimeType;

    private String body;

    private String replyTo;

    private String subject;

    public EmailBean() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    /**
     * Set receivers addresses
     * If you need to send more than one copy of email,
     * then put needed receivers separated by comma.
     *
     * @param to
     *         - email receivers
     */
    public void setTo(String to) {
        this.to = to;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
