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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("{ws-name}/log-reader-service/")
public class LogReaderService {

    private final LogPathProvider logPathProvider;

    public LogReaderService() {
        this(new SystemLogPathProvider());
    }

    public LogReaderService(LogPathProvider logPathProvider) {
        this.logPathProvider = logPathProvider;
    }

    @GET
    @Path("last-log")
    @Produces(MediaType.APPLICATION_JSON)
    public LogEntry getLastLog() throws LogReaderException {
        LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
        String token = logReader.getLastToken();
        if (token != null) {
            return new LogEntry(token, logReader.getLogByToken(token), logReader.hasNextToken(token),
                                logReader.hasPrevToken(token));
        } else {
            throw new LogReaderException("No logs found.");
        }
    }

    @GET
    @Path("log")
    @Produces(MediaType.APPLICATION_JSON)
    public LogEntry getLog(@QueryParam("lrtoken") String token) throws LogReaderException {

        LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
        if (!token.equals("null")) {
            return new LogEntry(token, logReader.getLogByToken(token), logReader.hasNextToken(token),
                                logReader.hasPrevToken(token));
        } else {
            throw new LogReaderException("Token must not be null.");
        }
    }

    @GET
    @Path("prev-log")
    @Produces(MediaType.APPLICATION_JSON)
    public LogEntry getPrevLog(@QueryParam("lrtoken") String token) throws LogReaderException {

        LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
        String newToken = logReader.getPrevToken(token);
        if (newToken != null) {
            return new LogEntry(newToken, logReader.getLogByToken(newToken), logReader.hasNextToken(newToken),
                                logReader.hasPrevToken(newToken));
        } else {
            throw new LogReaderException("Previous token not found.");
        }
    }

    @GET
    @Path("next-log")
    @Produces(MediaType.APPLICATION_JSON)
    public LogEntry getNextLog(@QueryParam("lrtoken") String token) throws LogReaderException {
        LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
        String newToken = logReader.getNextToken(token);
        if (newToken != null) {
            return new LogEntry(newToken, logReader.getLogByToken(newToken), logReader.hasNextToken(newToken),
                                logReader.hasPrevToken(newToken));
        } else {
            throw new LogReaderException("Next token not found.");
        }
    }

}
