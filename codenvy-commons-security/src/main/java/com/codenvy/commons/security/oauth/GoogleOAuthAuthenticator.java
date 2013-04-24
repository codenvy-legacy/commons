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

import com.codenvy.commons.security.shared.User;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;

/** OAuth authentication for google account. */
public class GoogleOAuthAuthenticator extends OAuthAuthenticator {

    public GoogleOAuthAuthenticator(CredentialStore credentialStore, GoogleClientSecrets clientSecrets) {
        super(new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(), clientSecrets,
                                                      Collections.<String>emptyList()).setCredentialStore(credentialStore)
                                                                                      .setApprovalPrompt("auto").setAccessType
                        ("online").build(), new HashSet<String>(clientSecrets.getDetails().getRedirectUris()));
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
    public String getToken(String userId) throws IOException {
        final String token = super.getToken(userId);
        if (!(token == null || token.isEmpty())) {
            // Need to check if token which stored is valid for requests, then if valid - we returns it to caller
            URL tokenInfoUrl = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + token);
            HttpURLConnection http = null;

            try {
                http = (HttpURLConnection)tokenInfoUrl.openConnection();
                if (http.getResponseCode() != 200) {
                    return null;
                }
            } finally {
                if (http != null) {
                    http.disconnect();
                }
            }

            return token;
        }

        return null;
    }
}
