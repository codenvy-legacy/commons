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
package com.codenvy.commons.security.oauth;

import com.codenvy.commons.json.JsonHelper;
import com.codenvy.commons.json.JsonParseException;
import com.codenvy.commons.security.shared.Token;
import com.codenvy.commons.security.shared.User;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import org.everrest.core.impl.provider.json.JsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;

/** OAuth authentication for google account. */
public class GoogleOAuthAuthenticator extends OAuthAuthenticator {

    public GoogleOAuthAuthenticator(CredentialStore credentialStore, GoogleClientSecrets clientSecrets) {
        super(new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(), clientSecrets,
                                                      Collections.<String> emptyList()).setCredentialStore(credentialStore)
                                                                                       .setApprovalPrompt("auto").setAccessType
                                                                                       ("online").build(),
              new HashSet<String>(clientSecrets.getDetails().getRedirectUris()));
    }

    @Override
    public User getUser(String accessToken) throws OAuthAuthenticationException {
        return getJson("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken, GoogleUser.class);
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
                e.printStackTrace();
            }
            return token;
        }
        return null;
    }

    private JsonValue doRequest(URL tokenInfoUrl) throws IOException, JsonParseException {
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)tokenInfoUrl.openConnection();
            if (http.getResponseCode() != 200) {
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
