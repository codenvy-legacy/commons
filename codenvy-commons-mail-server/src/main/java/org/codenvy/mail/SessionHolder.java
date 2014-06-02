package org.codenvy.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class SessionHolder {
    private static final Logger LOG = LoggerFactory.getLogger(SessionHolder.class);

    private static final String CONFIGURATION_FILE = "mail.configuration.path";

    private Session session;

    @Inject
    public SessionHolder(@Named(CONFIGURATION_FILE) String configuration) throws IOException {
        File configFile = new File(configuration);
        InputStream is = null;

        try {
            if (configFile.exists() && configFile.isFile()) {
                is = new FileInputStream(configuration);
            } else {
                is = MailSender.class.getResourceAsStream(configuration);
            }

            if (is == null) {
                File config = new File(configuration);
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

                this.session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public Session getMailSession() {
        return session;
    }
}
