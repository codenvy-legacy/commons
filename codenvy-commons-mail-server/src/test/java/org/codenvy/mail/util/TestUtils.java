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
