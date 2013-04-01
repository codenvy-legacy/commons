/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.codenvy.mail.util;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import org.codenvy.mail.EmailBean;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class TestUtils {

    private TestUtils() {
    }

    public static void assertMail(SimpleSmtpServer server, Map<String, String> expectedHeaders) {
        assertEquals(server.getReceivedEmailSize(), 1);
        Iterator emailIter = server.getReceivedEmail();
        SmtpMessage email = (SmtpMessage)emailIter.next();

        assertEquals(email.getHeaderValue("Subject"), expectedHeaders.get("subject"));
        assertEquals(email.getHeaderValue("From"), expectedHeaders.get("from"));
        assertEquals(email.getHeaderValue("Reply-To"), expectedHeaders.get("replyTo"));
        assertEquals(email.getHeaderValue("To"), expectedHeaders.get("to"));
    }

    public static EmailBean buildEmailBean(String from, String to, String replyTo, String subject, String mimeType,
                                           String body) {
        EmailBean bean = new EmailBean();

        bean.setBody(body);
        bean.setFrom(from);
        bean.setMimeType(mimeType);
        bean.setReplyTo(replyTo);
        bean.setSubject(subject);
        bean.setTo(to);

        return bean;
    }

    public static EmailBean buildEmailBean(Map<String, String> params) {
        EmailBean bean = new EmailBean();

        bean.setBody(params.get("body"));
        bean.setFrom(params.get("from"));
        bean.setMimeType(params.get("mimeType"));
        bean.setReplyTo(params.get("replyTo"));
        bean.setSubject(params.get("subject"));
        bean.setTo(params.get("to"));

        return bean;
    }
}
