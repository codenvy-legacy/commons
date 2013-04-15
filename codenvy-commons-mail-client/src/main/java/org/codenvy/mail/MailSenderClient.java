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
package org.codenvy.mail;

import com.codenvy.commons.lang.Deserializer;

import org.everrest.core.impl.provider.json.JsonException;
import org.everrest.core.impl.provider.json.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.mail.MessagingException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/** Client for MailSender service */
public class MailSenderClient {
    private static final Logger LOG                               = LoggerFactory.getLogger(MailSenderClient.class);

    private static final String MAILSENDER_APPLICATION_SERVER_URL = "mailsender.application.server.url";

    /** Base path */
    public static final String  BASE_URL                          = "mail/";

    /** UTF-8 encoding name */
    public static final String  UTF_8                             = "UTF-8";

    /** String representation of MailSender Service application address */
    private String              server;

    public MailSenderClient() {
        this(System.getProperty(MAILSENDER_APPLICATION_SERVER_URL));
    }

    /**
     * Simple constructor to pass application server address
     * 
     * @param server
     */
    public MailSenderClient(String server) {
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

            byte[] content = JsonGenerator.createJsonObject(emailBean).toString().getBytes(UTF_8);

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
        } catch (JsonException e) {
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
