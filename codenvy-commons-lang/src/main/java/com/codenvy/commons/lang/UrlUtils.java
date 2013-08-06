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
package com.codenvy.commons.lang;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO replace this class with URLEncodedUtils
 */
public class UrlUtils {
    /**
     * Retrieve query parameters map from String representation of url
     *
     * @param url
     * @return - <code>Map</code> with parameters names as map keys and parameters values as map values
     * @throws MalformedURLException
     *         - if url is invalid
     * @throws UnsupportedEncodingException
     */
    public static Map<String, List<String>> getQueryParameters(String url) throws MalformedURLException, UnsupportedEncodingException {
        Map<String, List<String>> params = new HashMap<>();
        URL verifiedUrl = new URL(url);
        String query = verifiedUrl.getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = null;
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                List<String> values = params.get(key);
                if (values == null) {
                    values = new ArrayList<>();
                    params.put(key, values);
                }
                values.add(value);
            }
        }
        return params;
    }

    private UrlUtils() {
    }
}
