/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
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
