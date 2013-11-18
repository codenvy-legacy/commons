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

import com.codenvy.commons.json.JsonHelper;
import com.codenvy.commons.json.JsonParseException;
import com.codenvy.commons.lang.UrlUtils;
import com.codenvy.commons.security.oauth.BeanToken;
import com.codenvy.commons.security.oauth.OAuthAuthenticationException;
import com.codenvy.commons.security.oauth.OAuthAuthenticator;
import com.codenvy.commons.security.shared.Token;
import com.codenvy.commons.security.shared.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpParser;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

//TODO add oauth access error processing

/** OAuth authenticator for BitBucket provider */
public class BitBucketOAuthAuthenticator extends OAuthAuthenticator {
    private final GoogleClientSecrets  clientSecrets;
    private final Map<Pattern, String> redirectUrisMap;
    /** contain request tokens, that should be used to retrieve access tokens */
    private Map<String, org.scribe.model.Token> requestTokens = new HashMap<>();
    private OAuth1CredentialStore credentialStore;

    public BitBucketOAuthAuthenticator(GoogleClientSecrets clientSecrets) {
        // do not use parent class to avoid NPE
        super(null, Collections.EMPTY_SET);

        this.credentialStore = new MemoryOAuth1CredentialStore();

        HashSet<String> redirectUris = new HashSet<>(clientSecrets.getDetails().getRedirectUris());
        this.redirectUrisMap = new HashMap<>(redirectUris.size());
        for (String uri : redirectUris) {
            // Redirect URI may be in form urn:ietf:wg:oauth:2.0:oob os use java.net.URI instead of java.net.URL
            this.redirectUrisMap.put(Pattern.compile("([a-z0-9\\-]+\\.)?" + URI.create(uri).getHost()), uri);
        }

        this.clientSecrets = clientSecrets;
    }

    @Override
    public String getOAuthProvider() {
        return "bitbucket";
    }

    @Override
    public final String getAuthenticateUrl(URL requestUrl, String userId, List<String> scopes) throws OAuthAuthenticationException {
        try {
            StringBuilder state = new StringBuilder();
            String query = requestUrl.getQuery();
            if (query != null) {
                state.append(query);
            }
            if (userId != null) {
                if (state.length() > 0) {
                    state.append('&');
                }
                state.append("userId=");
                state.append(userId);
            }

            UriBuilder ub =
                    UriBuilder.fromUri(findRedirectUrl(requestUrl)).queryParam("state", URLEncoder.encode(state.toString(), "UTF-8"));

            OAuthService service = getOAuthService(ub.build().toString());

            org.scribe.model.Token requestToken = service.getRequestToken();

            // store request token to be able to get access token later
            requestTokens.put(requestToken.getToken(), requestToken);

            return service.getAuthorizationUrl(requestToken);
        } catch (UnsupportedEncodingException e) {
            throw new OAuthAuthenticationException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public final String callback(URL requestUrl, List<String> scopes) throws OAuthAuthenticationException {
        try {
            Map<String, List<String>> params = UrlUtils.getQueryParameters(requestUrl);

            List<String> verifierValues = params.get("oauth_verifier");
            List<String> tokenValues = params.get("oauth_token");
            if (verifierValues == null || verifierValues.isEmpty()) {
                throw new OAuthAuthenticationException("Verifier parameter is missing.");
            }
            if (tokenValues == null || tokenValues.isEmpty()) {
                throw new OAuthAuthenticationException("Token parameter is missing.");
            }

            Verifier v = new Verifier(verifierValues.iterator().next());
            String token = tokenValues.iterator().next();

            OAuthService service = getOAuthService(null);

            org.scribe.model.Token accessToken = service.getAccessToken(requestTokens.get(token), v);
            requestTokens.remove(token);

            String userId = getUserFromUrl(params.get("state"));
            if (userId == null) {
                userId = getUser(new BeanToken(accessToken.getToken(), null, null, accessToken.getSecret(), null)).getId();
            }

            credentialStore.put(userId, accessToken);
            return userId;

        } catch (UnsupportedEncodingException e) {
            throw new OAuthAuthenticationException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public User getUser(Token accessToken) throws OAuthAuthenticationException {
        try {
            org.scribe.model.Token tokenForSigning = new org.scribe.model.Token(accessToken.getToken(), accessToken.getSecret());
            OAuthService service = getOAuthService(null);
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://bitbucket.org/api/1.0/user");
            service.signRequest(tokenForSigning, request);
            Response response = request.send();

            BitBucketUser user =
                    JsonHelper.fromJson(JsonHelper.parseJson(response.getStream()).getElement("user"), BitBucketUser.class, null);

            request = new OAuthRequest(Verb.GET, "https://bitbucket.org/api/1.0/users/" + user.getUsername() + "/emails");
            service.signRequest(tokenForSigning, request);
            response = request.send();
            BitBucketEmail[] emails = JsonHelper.fromJson(response.getStream(), BitBucketEmail[].class, null);

            BitBucketEmail activeEmail = null;
            for (BitBucketEmail email : emails) {
                if (email.isPrimary() && email.isActive()) {
                    activeEmail = email;
                    break;
                }
            }

            if (activeEmail == null || activeEmail.getEmail() == null || activeEmail.getEmail().isEmpty()) {
                throw new OAuthAuthenticationException(
                        "Sorry, we failed to find any active emails associated with your BitBucket account." +
                        " Please, activate at least one email in your BitBucket account and try to connect with BitBucket again.");

            }
            user.setEmail(activeEmail.getEmail());
            final String email = user.getEmail();
            try {
                new InternetAddress(email).validate();
            } catch (AddressException e) {
                throw new OAuthAuthenticationException(e.getMessage());
            }
            return user;
        } catch (JsonParseException e) {
            throw new OAuthAuthenticationException(e.getLocalizedMessage(), e);
        }
    }

    /** throw {@code UnsupportedOperationException} because this authenticator can't get token by userId only */
    @Override
    public Token getToken(String userId) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Token getToken(String userId, OAuth1UrlInfo urlInfo) throws IOException {
        org.scribe.model.Token token = credentialStore.get(userId);
        if (token != null && token.getToken() != null && !token.getToken().isEmpty() && token.getSecret() != null &&
            !token.getSecret().isEmpty()) {
            // Need to check if token which stored is valid for requests, then if valid - we returns it to caller
            OAuthService service = getOAuthService(null);
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://bitbucket.org/api/1.0/");
            service.signRequest(token, request);
            Response response = request.send();
            if (response.getCode() != 401) {
                request = new OAuthRequest(Verb.valueOf(urlInfo.getRequestMethod()), urlInfo.getRequestUrl());

                if (urlInfo.getBodyParameters() != null)
                    for (Map.Entry<String, String> entry : urlInfo.getBodyParameters().entrySet())
                        request.addBodyParameter(entry.getKey(), entry.getValue());

                if (urlInfo.getQueryParameters() != null)
                    for (Map.Entry<String, String> entry : urlInfo.getQueryParameters().entrySet())
                        request.addQuerystringParameter(entry.getKey(), entry.getValue());

                service.signRequest(token, request);
                return new BeanToken(token.getToken(), null, "1.0", token.getSecret(), request.getHeaders().get(OAuthConstants.HEADER));
            }
        }
        return null;
    }

    @Override
    public final boolean invalidateToken(String userId) {
        org.scribe.model.Token token = credentialStore.get(userId);
        if (token != null) {
            credentialStore.delete(userId);
            return true;
        }
        return false;
    }

    @Override
    protected void addState(StringBuilder state) {
    }

    /** throw {@code UnsupportedOperationException} to avoid throwing NPE, because parent constructor has got a null in constructor */
    protected HttpParser getParser() {
        throw new UnsupportedOperationException();
    }

    private String findRedirectUrl(URL requestUrl) throws OAuthAuthenticationException {
        final String requestHost = requestUrl.getHost();
        for (Map.Entry<Pattern, String> e : redirectUrisMap.entrySet()) {
            if (e.getKey().matcher(requestHost).matches()) {
                return e.getValue();
            }
        }
        throw new OAuthAuthenticationException("Redirect url for host " + "" + " is not found.");
    }

    /**
     * Get service to create OAuth requests
     *
     * @param callbackUrl
     *         - add to service optional callback URL
     * @return - OAuth service
     */
    private OAuthService getOAuthService(String callbackUrl) {
        ServiceBuilder sb = new ServiceBuilder()
                .provider(BitBucketApi.class)
                .apiKey(clientSecrets.getDetails().getClientId())
                .apiSecret(clientSecrets.getDetails().getClientSecret());
        if (callbackUrl != null) {
            sb.callback(callbackUrl);
        }

        return sb.build();
    }

    private String getUserFromUrl(List<String> stateValues) throws OAuthAuthenticationException {
        try {
            if (stateValues != null && !stateValues.isEmpty()) {
                String state = stateValues.iterator().next();
                String decoded = URLDecoder.decode(state, "UTF-8");

                String[] items = decoded.split("&");
                for (String str : items) {
                    if (str.startsWith("userId=")) {
                        return str.substring(7, str.length());
                    }
                }
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new OAuthAuthenticationException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Information for each email address indicating if the address
     * has been activated and if it’s the user’s primary email address for BitBucket.
     */
    public static class BitBucketEmail {
        private boolean primary;
        private boolean active;
        private String  email;

        public boolean isPrimary() {
            return primary;
        }

        public void setPrimary(boolean primary) {
            this.primary = primary;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
