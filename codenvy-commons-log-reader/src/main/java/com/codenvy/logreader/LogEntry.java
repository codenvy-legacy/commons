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
