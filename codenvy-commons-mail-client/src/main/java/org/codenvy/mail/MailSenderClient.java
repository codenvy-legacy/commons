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
package org.codenvy.mail;

import com.codenvy.commons.json.JsonHelper;
import com.codenvy.commons.lang.Deserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/** Client for MailSender service */
public class MailSenderClient {
    private static final Logger LOG = LoggerFactory.getLogger(MailSenderClient.class);

    private static final String MAILSENDER_APPLICATION_SERVER_URL = "mailsender.application.server.url";

    /** Base path */
    public static final String BASE_URL = "mail/";

    /** UTF-8 encoding name */
    public static final String UTF_8 = "UTF-8";

    /** String representation of MailSender Service application address */
    private String server;

    /**
     * Simple constructor to pass application server address
     *
     * @param server
     */
    @Inject
    public MailSenderClient(@Named(MAILSENDER_APPLICATION_SERVER_URL) String server) {
        if (server.endsWith("/")) {
            this.server = server;
        } else {
            this.server = server + "/";
        }
    }

    public void sendMail(String from, String to, String replyTo, String subject, String mimeType,
                         String template) throws IOException, MessagingException {
        sendMail(from, to, replyTo, subject, mimeType, template, null);
    }

    public void sendMail(String from, String to, String replyTo, String subject, String mimeType, String template,
                         Map<String, String> templateProperties) throws MessagingException, IOException {
        HttpURLConnection conn = null;
        OutputStream os = null;

        try {
            EmailBean emailBean = new EmailBean();
            emailBean.setFrom(from);
            emailBean.setMimeType(mimeType);
            emailBean.setReplyTo(replyTo);
            emailBean.setSubject(subject);
            emailBean.setTo(to);
            if (templateProperties != null) {
                emailBean.setBody(Deserializer.resolveVariables(template, templateProperties));
            } else {
                emailBean.setBody(template);
            }

            byte[] content = JsonHelper.toJson(emailBean).toString().getBytes(UTF_8);

            URL url = new URL(server + BASE_URL + "send");

            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod(HttpMethod.POST);
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            conn.setDoOutput(true);

            os = conn.getOutputStream();
            os.write(content);
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new MessagingException(conn.getResponseMessage());
            }
        } catch (RuntimeException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        } finally {
            if (os != null) {
                os.close();
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
