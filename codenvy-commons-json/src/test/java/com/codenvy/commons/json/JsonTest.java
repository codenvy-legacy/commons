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


import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class JsonTest {
    public static class Foo {
        private String fooBar;

        public String getFooBar() {
            return fooBar;
        }

        public void setFooBar(String fooBar) {
            this.fooBar = fooBar;
        }
    }

    @Test
    public void testSerializeDefault() throws Exception {
        String expectedJson = "{\"fooBar\":\"test\"}";
        Foo foo = new Foo();
        foo.setFooBar("test");
        assertEquals(expectedJson, JsonHelper.toJson(foo));
    }

    @Test
    public void testSerializeUnderscore() throws Exception {
        String expectedJson = "{\"foo_bar\":\"test\"}";
        Foo foo = new Foo();
        foo.setFooBar("test");
        assertEquals(expectedJson, JsonHelper.toJson(foo, JsonNameConventions.CAMEL_UNDERSCORE));
    }

    @Test
    public void testSerializeDash() throws Exception {
        String expectedJson = "{\"foo-bar\":\"test\"}";
        Foo foo = new Foo();
        foo.setFooBar("test");
        assertEquals(expectedJson, JsonHelper.toJson(foo, JsonNameConventions.CAMEL_DASH));
    }

    @Test
    public void testDeserializeDefault() throws Exception {
        String json = "{\"fooBar\":\"test\"}";
        Foo foo = JsonHelper.fromJson(json, Foo.class, null);
        assertEquals("test", foo.getFooBar());
    }

    @Test
    public void testDeserializeUnderscore() throws Exception {
        String json = "{\"foo_bar\":\"test\"}";
        Foo foo = JsonHelper.fromJson(json, Foo.class, null, JsonNameConventions.CAMEL_UNDERSCORE);
        assertEquals("test", foo.getFooBar());
    }

    @Test
    public void testDeserializeDash() throws Exception {
        String json = "{\"foo-bar\":\"test\"}";
        Foo foo = JsonHelper.fromJson(json, Foo.class, null, JsonNameConventions.CAMEL_DASH);
        assertEquals("test", foo.getFooBar());
    }
}
