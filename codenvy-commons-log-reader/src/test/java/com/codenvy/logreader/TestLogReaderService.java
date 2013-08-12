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

import junit.framework.Assert;

import com.codenvy.logreader.LogEntry;
import com.codenvy.logreader.LogPathProvider;
import com.codenvy.logreader.LogReaderException;
import com.codenvy.logreader.SimpleLogPathProvider;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;

public class TestLogReaderService {

    @Test
    public void testLastLog() throws Exception {
        final String logDir = "target/test-classes/log/tenant";
        LogPathProvider logPath = new SimpleLogPathProvider(logDir);
        LogReaderService service = new LogReaderService(logPath);

        LogEntry result = service.getLastLog();
        String content = result.getContent();

        BufferedReader reader = new BufferedReader(new FileReader(logDir + "/2012/01/01/00/tenant-0.log"));
        StringBuilder builder = new StringBuilder();
        try {
            char[] cbuf = new char[1024];
            int length = 0;
            while (length >= 0) {
                builder.append(cbuf, 0, length);
                length = reader.read(cbuf);
            }
        } finally {
            reader.close();
        }
        String etalonContent = builder.toString();

        Assert.assertEquals(content, etalonContent);
        Assert.assertFalse(result.isHasNext());
        Assert.assertTrue(result.isHasPrevious());
    }

    @Test
    public void testPrevLog() throws Exception {
        final String logDir = "target/test-classes/log/tenant";
        LogPathProvider logPath = new SimpleLogPathProvider(logDir);
        LogReaderService service = new LogReaderService(logPath);

        String token = service.getLastLog().getLrtoken();

        for (int i = 0; i < 3; i++) {
            token = service.getPrevLog(token).getLrtoken();
        }
        LogEntry result = service.getPrevLog(token);
        String content = result.getContent();

        BufferedReader reader = new BufferedReader(new FileReader(logDir + "/2011/07/12/11/tenant-9.log"));
        StringBuilder builder = new StringBuilder();
        try {
            char[] cbuf = new char[1024];
            int length = 0;
            while (length >= 0) {
                builder.append(cbuf, 0, length);
                length = reader.read(cbuf);
            }
        } finally {
            reader.close();
        }
        String etalonContent = builder.toString();

        Assert.assertEquals(content, etalonContent);
        Assert.assertTrue(result.isHasNext());
        Assert.assertTrue(result.isHasPrevious());
    }

    @Test
    public void testNextLog() throws Exception {
        final String logDir = "target/test-classes/log/tenant";
        LogPathProvider logPath = new SimpleLogPathProvider(logDir);
        LogReaderService service = new LogReaderService(logPath);

        String token = service.getLastLog().getLrtoken();

        for (int i = 0; i < 4; i++) {
            token = service.getPrevLog(token).getLrtoken();
        }
        token = service.getNextLog(token).getLrtoken();
        LogEntry result = service.getNextLog(token);
        String content = result.getContent();

        BufferedReader reader = new BufferedReader(new FileReader(logDir + "/2011/07/12/11/tenant-11.log"));
        StringBuilder builder = new StringBuilder();
        try {
            char[] cbuf = new char[1024];
            int length = 0;
            while (length >= 0) {
                builder.append(cbuf, 0, length);
                length = reader.read(cbuf);
            }
        } finally {
            reader.close();
        }
        String etalonContent = builder.toString();

        Assert.assertEquals(content, etalonContent);
        Assert.assertTrue(result.isHasNext());
        Assert.assertTrue(result.isHasPrevious());
    }

    @Test
    public void testMultifolder() throws Exception {
        final String logDir = "target/test-classes/log/tenant";
        LogPathProvider logPath = new SimpleLogPathProvider(logDir);
        LogReaderService service = new LogReaderService(logPath);

        String token = service.getLastLog().getLrtoken();

        for (int i = 0; i < 17; i++) {
            token = service.getPrevLog(token).getLrtoken();
        }
        for (int i = 0; i < 5; i++) {
            token = service.getNextLog(token).getLrtoken();
        }
        LogEntry result = service.getNextLog(token);
        String content = result.getContent();

        BufferedReader reader = new BufferedReader(new FileReader(logDir + "/2011/07/12/11/tenant-2.log"));
        StringBuilder builder = new StringBuilder();
        try {
            char[] cbuf = new char[1024];
            int length = 0;
            while (length >= 0) {
                builder.append(cbuf, 0, length);
                length = reader.read(cbuf);
            }
        } finally {
            reader.close();
        }
        String etalonContent = builder.toString();

        Assert.assertEquals(content, etalonContent);
        Assert.assertTrue(result.isHasNext());
        Assert.assertTrue(result.isHasPrevious());
    }

    @Test(expected = LogReaderException.class)
    public void checkNextAfterLast() throws LogReaderException {
        final String logDir = "target/test-classes/log/tenant";
        SimpleLogPathProvider pathGenerator = new SimpleLogPathProvider(logDir);

        // try to get next token from last token
        LogReaderService logReaderService = new LogReaderService(pathGenerator);

        String token = logReaderService.getLastLog().getLrtoken();
        logReaderService.getNextLog(token);
    }

    @Test(expected = LogReaderException.class)
    public void checkPrevAfterFirst() throws LogReaderException {
        final String logDir = "target/test-classes/log/tenant";
        SimpleLogPathProvider pathGenerator = new SimpleLogPathProvider(logDir);

        // try to get previous token from first token
        LogReaderService logReaderService = new LogReaderService(pathGenerator);

        String token = logReaderService.getLastLog().getLrtoken();
        while (true) {
            LogEntry result = logReaderService.getPrevLog(token);
            token = result.getLrtoken();
            String content = result.getContent();
            Assert.assertFalse(content.equals("FAIL"));
            Assert.assertTrue(token.matches("/[0-9]+/[0-9]+/[0-9]+/[0-9]+/tenant-[0-9]+.log"));
        }
    }

    @Test(expected = LogReaderException.class)
    public void checkDangerTokens() throws LogReaderException {
        final String logDir = "target/test-classes/log/tenant";
        SimpleLogPathProvider pathGenerator = new SimpleLogPathProvider(logDir);

        LogReaderService logReaderService = new LogReaderService(pathGenerator);

        // try to use .. in token
        String token = "/../denant/2011/07/13/16/denant-0.log";
        logReaderService.getLog(token);
    }

}
