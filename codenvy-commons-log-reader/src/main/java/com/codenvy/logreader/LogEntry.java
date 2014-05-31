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

/** Been for log entry (represent one log file on file system) */
public class LogEntry {

    private String lrtoken;

    private String content;

    private boolean hasNext;

    private boolean hasPrevious;

    public LogEntry() {
    }

    public LogEntry(String lrtoken, String content, boolean hasNext, boolean hasPrevious) {
        super();
        this.lrtoken = lrtoken;
        this.content = content;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    /** @return the token */
    public String getLrtoken() {
        return lrtoken;
    }

    /**
     * @param lrtoken
     *         the token to set
     */
    public void setLrtoken(String lrtoken) {
        this.lrtoken = lrtoken;
    }

    /** @return the content */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *         the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /** @return the hasNext */
    public boolean isHasNext() {
        return hasNext;
    }

    /**
     * @param hasNext
     *         the hasNext to set
     */
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    /** @return the hasPrevious */
    public boolean isHasPrevious() {
        return hasPrevious;
    }

    /**
     * @param hasPrevious
     *         the hasPrevious to set
     */
    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

}
