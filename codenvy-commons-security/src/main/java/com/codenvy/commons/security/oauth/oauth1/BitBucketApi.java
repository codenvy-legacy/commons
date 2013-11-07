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
package com.codenvy.commons.security.oauth.oauth1;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Implementation of the OAuth 1.0a protocol for BitBucket.
 *
 * This class provides the endpoints for OAuth authentication.
 */
public class BitBucketApi extends DefaultApi10a {
    @Override
    public String getRequestTokenEndpoint() {
        return "https://bitbucket.org/api/1.0/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://bitbucket.org/api/1.0/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return "https://bitbucket.org/api/1.0/oauth/authenticate?oauth_token=" + requestToken.getToken();
    }
}
