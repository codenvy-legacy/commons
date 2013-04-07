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

        String token = service.getLastLog().getToken();

        for (int i = 0; i < 3; i++) {
            token = service.getPrevLog(token).getToken();
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

        String token = service.getLastLog().getToken();

        for (int i = 0; i < 4; i++) {
            token = service.getPrevLog(token).getToken();
        }
        token = service.getNextLog(token).getToken();
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

        String token = service.getLastLog().getToken();

        for (int i = 0; i < 17; i++) {
            token = service.getPrevLog(token).getToken();
        }
        for (int i = 0; i < 5; i++) {
            token = service.getNextLog(token).getToken();
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

        String token = logReaderService.getLastLog().getToken();
        logReaderService.getNextLog(token);
    }

    @Test(expected = LogReaderException.class)
    public void checkPrevAfterFirst() throws LogReaderException {
        final String logDir = "target/test-classes/log/tenant";
        SimpleLogPathProvider pathGenerator = new SimpleLogPathProvider(logDir);

        // try to get previous token from first token
        LogReaderService logReaderService = new LogReaderService(pathGenerator);

        String token = logReaderService.getLastLog().getToken();
        while (true) {
            LogEntry result = logReaderService.getPrevLog(token);
            token = result.getToken();
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
