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

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** Provide service of email sending. */
@Path("/mail")
public class MailSender {
    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);

    private SessionHolder sessionHolder;

    @Inject
    public MailSender(SessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
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
            MimeMessage message = new MimeMessage(sessionHolder.getMailSession());
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
        }

        return Response.ok().build();
    }
}
