/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
