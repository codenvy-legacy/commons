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

import com.codenvy.commons.security.oauth.oauth1.OAuth1UrlInfo;
import com.codenvy.commons.security.shared.Token;

import java.io.IOException;

/** Retrieves oAuth token with help of OAuthAuthenticatorProvider. */
public class OAuthAuthenticatorTokenProvider implements OAuthTokenProvider {
    private final OAuthAuthenticatorProvider oAuthAuthenticatorProvider;

    public OAuthAuthenticatorTokenProvider(OAuthAuthenticatorProvider oAuthAuthenticatorProvider) {
        this.oAuthAuthenticatorProvider = oAuthAuthenticatorProvider;
    }

    @Override
    public Token getToken(String oauthProviderName, String userId) throws IOException {
        OAuthAuthenticator oAuthAuthenticator = oAuthAuthenticatorProvider.getAuthenticator(oauthProviderName);
        Token token;
        if (oAuthAuthenticator != null && (token = oAuthAuthenticator.getToken(userId)) != null) {
            return token;
        }
        return null;
    }

    @Override
    public Token getToken(String oauthProviderName, String userId, OAuth1UrlInfo urlInfo) throws IOException {
        OAuthAuthenticator oAuthAuthenticator = oAuthAuthenticatorProvider.getAuthenticator(oauthProviderName);
        Token token;
        if (oAuthAuthenticator != null && (token = oAuthAuthenticator.getToken(userId, urlInfo)) != null) {
            return token;
        }
        return null;
    }
}
