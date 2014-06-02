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
package com.codenvy.commons.json;

import org.everrest.core.impl.provider.json.JsonHandler;
import org.everrest.core.impl.provider.json.JsonParser;

/**
 * JSON parser that support transformation of names in JSON document.
 *
 * @see JsonNameConvention
 * @see JsonNameConventions
 */
public class NameConventionJsonParser extends JsonParser {
    public NameConventionJsonParser(JsonNameConvention nameConvention) {
        super(new NameConventionJsonHandler(nameConvention));
    }

    private static class NameConventionJsonHandler extends JsonHandler {
        private final JsonNameConvention nameConvention;

        private NameConventionJsonHandler(JsonNameConvention nameConvention) {
            this.nameConvention = nameConvention;
        }

        @Override
        public void key(String key) {
            super.key(nameConvention.toJavaName(key));
        }
    }
}
