package com.codenvy.commons.security.oauth.oauth1;

import java.util.HashMap;
import java.util.Map;

/**
 * Mandatory information that need to correctly sign OAuth v1.0 request.
 */
public class OAuth1UrlInfo {
    private String requestUrl;
    private String requestMethod;
    private Map<String, String> queryParameters = new HashMap<>();
    private Map<String, String> bodyParameters  = new HashMap<>();

    public OAuth1UrlInfo() {

    }

    public OAuth1UrlInfo(String requestUrl, String requestMethod, Map<String, String> queryParameters,
                         Map<String, String> bodyParameters) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.queryParameters = queryParameters;
        this.bodyParameters = bodyParameters;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public Map<String, String> getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(Map<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
    }

    @Override
    public String toString() {
        return "OAuth1UrlInfo{" +
               "requestUrl='" + requestUrl + '\'' +
               ", requestMethod='" + requestMethod + '\'' +
               ", queryParameters=" + queryParameters +
               ", bodyParameters=" + bodyParameters +
               '}';
    }
}
