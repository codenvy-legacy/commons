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

import com.codenvy.commons.security.SignatureDSA;
import com.codenvy.commons.security.shared.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.*;

import static java.net.URLEncoder.encode;

/** RESTful wrapper for OAuthAuthenticator. */
@Path("ide/oauth")
public class OAuthAuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(OAuthAuthenticationService.class);

    private final OAuthAuthenticatorProvider providers;



    public OAuthAuthenticationService(OAuthAuthenticatorProvider providers) {
        this.providers = providers;
    }

    /**
     * Redirect request to OAuth provider site for authentication|authorization. Client request must contains set of
     * required query parameters:
     * <table>
     * <tr><th>Name</th><th>Description</th><th>Mandatory</th><th>Default value</th></tr>
     * <tr><td>oauth_provider</td><td>Name of OAuth provider. At the moment <tt>google</tt> and <tt>github</tt>
     * supported</td><td>yes</td><td>none</td></tr>
     * <tr><td>scope</td><td>Specify exactly what type of access needed. List of scopes dependents to OAuth provider.
     * Requested scopes displayed at user authorization page at OAuth provider site. Check docs about scopes supported by
     * suitable OAuth provider.</td><td>no</td><td>Empty list</td></tr>
     * <tr><td>mode</td><td>Authentication mode. May be <tt>federated_login</tt> or <tt>token</tt>. If <tt>mode</tt> set
     * as <tt>federated_login</tt> that parameters 'username' and 'password' added to redirect URL after successful user
     * authentication. (see next parameter) In this case 'password' is temporary generated password. This password will
     * be validated by FederatedLoginModule.</td><td>no</td><td>token</td></tr>
     * <tr><td>redirect_after_login</td><td>URL for user redirection after successful
     * authentication</td><td>yes</td><td>none</td></tr>
     * </table>
     *
     * @param uriInfo
     *         UriInfo
     * @return typically Response that redirect user for OAuth provider site
     */
    @GET
    @Path("authenticate")
    public Response authenticate(@Context UriInfo uriInfo) {
        OAuthAuthenticator oauth = getAuthenticator(uriInfo.getQueryParameters().getFirst("oauth_provider"));
        final URL requestUrl = getRequestUrl(uriInfo);
        final List<String> scopes = uriInfo.getQueryParameters().get("scope");
        String userId = uriInfo.getQueryParameters().getFirst("userId");

        final String authUrl = oauth.getAuthenticateUrl(requestUrl, userId,
                                                        scopes == null ? Collections.<String>emptyList() : scopes);
        return Response.temporaryRedirect(URI.create(authUrl)).build();
    }

    @GET
    @Path("callback")
    public Response callback(@Context UriInfo uriInfo) throws OAuthAuthenticationException {
        URL requestUrl = getRequestUrl(uriInfo);
        Map<String, List<String>> params = getRequestParameters(getState(requestUrl));
        List<String> errorValues = uriInfo.getQueryParameters().get("error");
        // TODO redirect to previous page instead of login page
        if (errorValues != null && errorValues.contains("access_denied")) {
            return Response.temporaryRedirect(
                    uriInfo.getRequestUriBuilder().replacePath("/login").replaceQuery(null).build()).build();
        }
        final String providerName = getParameter(params, "oauth_provider");
        OAuthAuthenticator oauth = getAuthenticator(providerName);
        final List<String> scopes = params.get("scope");
        final String userId = oauth.callback(requestUrl, scopes == null ? Collections.<String>emptyList() : scopes);
        final String redirectAfterLogin = getParameter(params, "redirect_after_login");
        final String mode = getParameter(params, "mode");
        if ("federated_login".equals(mode)) {
            try {
                return Response.temporaryRedirect(UriBuilder.fromUri(URLDecoder.decode(redirectAfterLogin, "UTF-8")).queryParam("username",
                                                                                                    userId)
                                                            .queryParam("signature", encode(SignatureDSA.getBase64Signature(userId),
                                                                                            "UTF-8"))
                                                            .queryParam("oauth_provider", providerName).build()).build();
            } catch (Exception e) {
                throw new OAuthAuthenticationException(e.getLocalizedMessage(), e);
            }

        }
        return Response.temporaryRedirect(URI.create(redirectAfterLogin)).build();
    }

    private URL getRequestUrl(UriInfo uriInfo) {
        try {
            return uriInfo.getRequestUri().toURL();
        } catch (MalformedURLException e) {
            // should never happen
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * OAuth 2.0 support pass query parameters 'state' to OAuth authorization server. Authorization server sends it back
     * to callback URL. Here restore all parameters specified in initial request to {@link
     * #authenticate(javax.ws.rs.core.UriInfo, javax.ws.rs.core.SecurityContext)} .
     *
     * @param state
     *         query parameter state
     * @return map contains request parameters to method {@link #authenticate(javax.ws.rs.core.UriInfo,
     *         javax.ws.rs.core.SecurityContext)}
     */
    private Map<String, List<String>> getRequestParameters(String state) {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        if (!(state == null || state.isEmpty())) {
            String decodedState;
            try {
                decodedState = URLDecoder.decode(state, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // should never happen, UTF-8 supported.
                throw new RuntimeException(e.getMessage(), e);
            }

            for (String pair : decodedState.split("&")) {
                if (!pair.isEmpty()) {
                    String name;
                    String value;
                    int eq = pair.indexOf('=');
                    if (eq < 0) {
                        name = pair;
                        value = "";
                    } else {
                        name = pair.substring(0, eq);
                        value = pair.substring(eq + 1);
                    }

                    List<String> l = params.get(name);
                    if (l == null) {
                        l = new ArrayList<String>();
                        params.put(name, l);
                    }
                    l.add(value);
                }
            }
        }
        return params;
    }

    private String getState(URL requestUrl) {
        final String query = requestUrl.getQuery();
        if (!(query == null || query.isEmpty())) {
            int start = query.indexOf("state=");
            if (start < 0) {
                return null;
            }
            int end = query.indexOf('&', start);
            if (end < 0) {
                end = query.length();
            }
            return query.substring(start + 6, end);
        }
        return null;
    }

    private String getParameter(Map<String, List<String>> params, String name) {
        List<String> l = params.get(name);
        if (!(l == null || l.isEmpty())) {
            return l.get(0);
        }
        return null;
    }

    @GET
    @Path("invalidate")
    public Response invalidate(@Context UriInfo uriInfo, @Context SecurityContext security) {
        final Principal principal = security.getUserPrincipal();
        OAuthAuthenticator oauth = getAuthenticator(uriInfo.getQueryParameters().getFirst("oauth_provider"));
        if (principal != null && oauth.invalidateToken(principal.getName())) {
            return Response.ok().build();
        }
        return Response.status(404).entity("Not found OAuth token for " + (principal != null ? principal.getName() :
                                                                           null)).type(MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("token")
    public Token token(@QueryParam("oauth_provider") String oauth_provider, @QueryParam("userId") String userId,
                        @QueryParam("signature") String signature) throws Exception {
        LOG.debug("oauth_provider='{}'  userId='{}' signature='{}'", new String[]{oauth_provider, userId, signature});
        if (!(userId == null || userId.isEmpty() || signature == null || signature.isEmpty())) {

            if (SignatureDSA.isSignatureValid(userId, signature)) {
                LOG.debug("Signature verification for {} successful ", userId);
                OAuthAuthenticator provider = providers.getAuthenticator(oauth_provider);
                return provider.getToken(userId);
            } else {
                LOG.error("Signature verification for {} failed ", userId);
            }
        }
        return null;
    }

    private OAuthAuthenticator getAuthenticator(String oauthProviderName) {
        OAuthAuthenticator oauth = providers.getAuthenticator(oauthProviderName);
        if (oauth == null) {
            LOG.error("Unsupported OAuth provider {} ", oauthProviderName);
            throw new WebApplicationException(Response.status(400).entity("Unsupported OAuth provider " +
                                                                          oauthProviderName).type(MediaType.TEXT_PLAIN).build());
        }
        return oauth;
    }

}
