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

import com.dumbster.smtp.SimpleSmtpServer;

import org.everrest.assured.EverrestJetty;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import javax.mail.Session;
import java.io.IOException;

import static org.codenvy.mail.util.TestUtils.buildEmailBean;

@Listeners(value = {EverrestJetty.class, MockitoTestNGListener.class})
public class MailSenderTest {
    private Session mailSession;

    private MailSender mailSender;

    private SimpleSmtpServer server;

    @BeforeMethod
    public void setup() throws IOException {
        //      mailSender = new MailSender("/mail-configuration.properties");
        server = SimpleSmtpServer.start(9000);
        mailSender = new MailSender(new SessionHolder("/mail-configuration.properties"));

    }

    // TODO remove mockito if it useless
   /*@Test
   public void shouldBeAbleToSendMessage() throws Exception
   {
      Map<String, String> parametersMap = new HashMap<String, String>();
      parametersMap.put("from", "noreply@cloud-ide.com");
      parametersMap.put("to", "dev-test@cloud-ide.com");
      parametersMap.put("replyTo", "dev-test@cloud-ide.com");
      parametersMap.put("subject", "Subject");
      parametersMap.put("mimeType", "text/html");
      parametersMap.put("body", "hello user");

      given().(parametersMap).expect().statusCode(Status.OK.getStatusCode()).when().post("mail/send");

      server.stop();

      assertMail(server, parametersMap);
   }/*

   /*
   @Test
   public void shouldBeAbleToSendMessageWithFormattedFields() throws Exception
   {
      Map<String, String> parametersMap = new HashMap<String, String>();
      parametersMap.put("from", "Exo IDE <noreply@cloud-ide.com>");
      parametersMap.put("to", "dev-test@cloud-ide.com");
      parametersMap.put("replyTo", "Developers to reply <dev-test@cloud-ide.com>");
      parametersMap.put("subject", "Subject");
      parametersMap.put("mimeType", "text/html");
      parametersMap.put("body", "hello user");

      given().queryParameters(parametersMap).expect().statusCode(Status.OK.getStatusCode()).when().post("mail/send");

      server.stop();

      assertMail(server, parametersMap);
   }
   /*@Test
   public void shouldBeAbleToSendMessageToFewEmails() throws Exception
   {
      Map<String, String> parametersMap = new HashMap<String, String>();
      parametersMap.put("from", "Exo IDE <noreply@cloud-ide.com>");
      parametersMap.put("to", "dev-test@cloud-ide.com, dev-test1@cloud-ide.com, dev-test2@cloud-ide.com");
      parametersMap.put("replyTo", "Developers to reply <dev-test@cloud-ide.com>");
      parametersMap.put("subject", "Subject");
      parametersMap.put("mimeType", "text/html");
      parametersMap.put("body", "hello user");

      given().queryParameters(parametersMap).expect().statusCode(Status.OK.getStatusCode()).when().post("mail/send");

      server.stop();

      assertMail(server, parametersMap);
   }*/

   /*@Test
   public void shouldBeAbleToSendMessageWithFewReplyTo() throws Exception
   {
      Map<String, String> parametersMap = new HashMap<String, String>();
      parametersMap.put("from", "Exo IDE <noreply@cloud-ide.com>");
      parametersMap.put("to", "dev-test@cloud-ide.com");
      parametersMap
         .put("replyTo", "Developers to reply <dev-test@cloud-ide.com>, Tenant owner <tenantowner@gmail.com>");
      parametersMap.put("subject", "Subject");
      parametersMap.put("mimeType", "text/html");

      given().queryParameters(parametersMap).body("hello user").expect()
         .statusCode(Status.OK.getStatusCode()).when().post("mail/send");

      server.stop();

      // check multiple replyTo
      assertMailWithMultipleReplyTo(server, parametersMap, parametersMap.get("replyTo").split(","), "hello user");
   }*/

    // Uncomment when UTF-8 supporting was achieved
   /*@Test
   public void shouldBeAbleToSendMessageWithNonLatinCharacters() throws Exception
   {
      Map<String, String> parametersMap = new HashMap<String, String>();
      parametersMap.put("from", "Экзо ИДЕ Ъ Ґ Ї <noreply@cloud-ide.com>");
      parametersMap.put("to", "dev-test@cloud-ide.com");
      parametersMap.put("replyTo", "Экзо ИДЕ Ъ Ґ Ї <dev-test@cloud-ide.com>, Экзо ЪҐЇ <tenantowner@gmail.com>");
      parametersMap.put("subject", "Subject Экзо ИДЕ Ъ Ґ Ї");
      parametersMap.put("mimeType", "text/html; charset=utf-8");

      given().queryParameters(encodeMap(parametersMap)).body(encode("hello user Экзо ИДЕ Ъ Ґ Ї ")).expect()
         .statusCode(Status.OK.getStatusCode()).when().post("mail/send");

      server.stop();

      assertMail(server, parametersMap, "hello user");
   }*/

    // smtp server should be installed to send mails
    public void shouldBeAbleToSendRealMessage() throws Exception {
        // Used to check that messages comes to you
        mailSender.sendMail(buildEmailBean("Exo <noreply@cloud-ide.com>", "dev-test@cloud-ide.com",
                                           "Exo <dev-test@cloud-ide.com>, " + "" + "Tenant owner <tenantowner@gmail.com>", "Subject",
                                           "text/html; charset=utf-8", "hello user"));
    }

}
