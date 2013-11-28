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
package com.codenvy.commons.security.oauth;

import com.codenvy.commons.json.JsonHelper;
import com.codenvy.commons.json.JsonParseException;
import com.codenvy.commons.lang.IoUtil;
import com.codenvy.commons.security.shared.Token;
import com.codenvy.commons.security.shared.User;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import org.everrest.core.impl.provider.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;

/** OAuth authentication for google account. */
public class GoogleOAuthAuthenticator extends OAuthAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleOAuthAuthenticator.class);

    public GoogleOAuthAuthenticator(CredentialStore credentialStore, GoogleClientSecrets clientSecrets) {
        super(new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(), clientSecrets,
                                                      Collections.<String>emptyList()).setCredentialStore(credentialStore)
                                                                                      .setApprovalPrompt("auto").setAccessType
                        ("online").build(),
              new HashSet<String>(clientSecrets.getDetails().getRedirectUris()));
    }

    @Override
    public User getUser(Token accessToken) throws OAuthAuthenticationException {
        return getJson("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken.getToken(), GoogleUser.class);
    }

    @Override
    public final String getOAuthProvider() {
        return "google";
    }

    @Override
    public Token getToken(String userId) throws IOException {
        final Token token = super.getToken(userId);
        if (!(token == null || token.getToken() == null || token.getToken().isEmpty())) {
            // Need to check if token which stored is valid for requests, then if it is valid - we return it to caller
            URL tokenInfoUrl = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + token.getToken());
            try {
                JsonValue jsonValue = doRequest(tokenInfoUrl);
                if (jsonValue == null) {
                    return null;
                }
                JsonValue scope = jsonValue.getElement("scope");
                if (scope != null)
                    token.setScope(scope.getStringValue());
            } catch (JsonParseException e) {
                LOG.error(e.getLocalizedMessage(), e);
            }
            return token;
        }
        return null;
    }

    private JsonValue doRequest(URL tokenInfoUrl) throws IOException, JsonParseException {
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)tokenInfoUrl.openConnection();
            int responseCode = http.getResponseCode();
            if (responseCode != 200) {
                LOG.warn("Can not receive google token by path: {}. Response status: {}. Error message: {}",
                         new Object[]{tokenInfoUrl.toString(), responseCode, IoUtil.readStream(http.getErrorStream())});
                return null;
            }

            InputStream input = http.getInputStream();
            JsonValue result;
            try {
                result = JsonHelper.parseJson(input);
            } finally {
                input.close();
            }
            return result;
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }
}
