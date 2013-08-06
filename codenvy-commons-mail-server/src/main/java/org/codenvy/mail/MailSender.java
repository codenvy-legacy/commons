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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Provide service of email sending. */
@Path("/mail")
public class MailSender {
    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);

    private static final String EMAIL_CONNECTION_FILE_NAME = "email-connection.properties";

    private final String configuration;

    public MailSender() {
        this(EMAIL_CONNECTION_FILE_NAME);
    }

    public MailSender(String configFile) {
        this.configuration = configFile;
    }

    /**
     * Send mail message.
     * If you need to send more than one copy of email, then write needed
     * receivers to EmailBean using setTo() method.
     *
     * @param emailBean
     *         - bean that contains all message parameters
     * @return - the Response with corresponded status (200)
     * @throws WebApplicationException
     */
    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMail(EmailBean emailBean) throws WebApplicationException {

        try {
            MimeMessage message = new MimeMessage(getMailSession());
            message.setContent(emailBean.getBody(), emailBean.getMimeType());
            message.setSubject(emailBean.getSubject(), "UTF-8");
            message.setFrom(new InternetAddress(emailBean.getFrom(), true));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(emailBean.getTo()));

            if (emailBean.getReplyTo() != null) {
                message.setReplyTo(InternetAddress.parse(emailBean.getReplyTo()));
            }
            LOG.info("Sending from {} to {} with subject {}", new Object[]{emailBean.getFrom(), emailBean.getTo(), emailBean.getSubject()});

            Transport.send(message);
            LOG.debug("Mail send");
        } catch (MessagingException e) {
            LOG.error(e.getLocalizedMessage());
            throw new WebApplicationException(e);
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            throw new WebApplicationException(e);
        }

        return Response.ok().build();
    }

    protected Session getMailSession() throws IOException {
        File configFile = new File(configuration);
        InputStream is = null;

        try {
            if (configFile.exists() && configFile.isFile()) {
                is = new FileInputStream(configuration);
            } else {
                is = MailSender.class.getResourceAsStream(configuration);
            }

            if (is == null) {
                File config = new File(new File(System.getProperty("mailsender.configuration.dir")), configuration);
                if (!config.exists() || config.isDirectory()) {
                    LOG.error("Email configuration file " + config.getAbsolutePath() + " not found or is a directory",
                              configuration);
                    throw new RuntimeException("Email configuration file " + config.getAbsolutePath() + " not found or is a directory");
                }

                is = new FileInputStream(config);
            }

            Properties props = new Properties();
            props.load(is);

            if (Boolean.parseBoolean(props.getProperty("mail.smtp.auth"))) {
                final String username = props.getProperty("mail.smtp.auth.username");
                final String password = props.getProperty("mail.smtp.auth.password");

                // remove useless properties
                props.remove("mail.smtp.auth.username");
                props.remove("mail.smtp.auth.password");

                return Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            }
            return Session.getInstance(props);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
