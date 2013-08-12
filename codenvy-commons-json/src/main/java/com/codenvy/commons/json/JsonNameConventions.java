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

/** Prepared name conversion conventions for Java &lt;-&gt; JSON transformation. */
public enum JsonNameConventions implements JsonNameConvention {
    /**
     * Default implementation of JsonNameConvention. It does not convert name.
     * <p/>
     * How to serialize with JsonHelper:
     * <pre>
     *    Foo foo = new Foo();
     *    foo.setFooBar("foo");
     *    System.out.println(JsonHelper.toJson(foo, JsonNameConventions.DEFAULT));
     * </pre>
     * Code above prints:
     * <pre>
     *    {"fooBar":"foo"}
     * </pre>
     * <p/>
     * How to deserialize with JsonHelper:
     * <pre>
     *    class Foo {
     *       private String fooBar;
     *
     *       public String getFooBar() {
     *          return fooBar;
     *       }
     *
     *       public void setFooBar(String fooBar) {
     *          this.fooBar = fooBar;
     *       }
     *    }
     *
     *    ...
     *
     *    String json = "{\"fooBar\":\"foo\"}";
     *    Foo foo = fromJson(json, Foo.class, null, JsonNameConventions.DEFAULT);
     *    System.out.println(foo.getFooBar());
     * </pre>
     * Code above prints:
     * <pre>
     *    foo
     * </pre>
     */
    DEFAULT() {
        @Override
        public String toJsonName(String javaName) {
            return javaName;
        }

        @Override
        public String toJavaName(String jsonName) {
            return jsonName;
        }
    },

    /**
     * Implementation of JsonNameConvention converts Java camel-case names to lower-case names with underscore as
     * separator, e.g 'userName' -> 'user_name'.
     * <p/>
     * How to serialize with JsonHelper:
     * <pre>
     *    Foo foo = new Foo();
     *    foo.setFooBar("foo");
     *    System.out.println(JsonHelper.toJson(foo, JsonNameConventions.CAMEL_UNDERSCORE));
     * </pre>
     * Code above prints:
     * <pre>
     *    {"foo_bar":"foo"}
     * </pre>
     * <p/>
     * How to deserialize with JsonHelper:
     * <pre>
     *    class Foo {
     *       private String fooBar;
     *
     *       public String getFooBar() {
     *          return fooBar;
     *       }
     *
     *       public void setFooBar(String fooBar) {
     *          this.fooBar = fooBar;
     *       }
     *    }
     *
     *    ...
     *
     *    String json = "{\"foo_bar\":\"foo\"}";
     *    Foo foo = fromJson(json, Foo.class, null, JsonNameConventions.CAMEL_UNDERSCORE);
     *    System.out.println(foo.getFooBar());
     * </pre>
     * Code above prints:
     * <pre>
     *    foo
     * </pre>
     */
    CAMEL_UNDERSCORE() {
        @Override
        public String toJsonName(String javaName) {
            return camelToUnderscored(javaName);
        }

        @Override
        public String toJavaName(String jsonName) {
            return separateWithToCamel(jsonName, '_');
        }
    },

    /**
     * Implementation of JsonNameConvention converts Java camel-case names to lower-case names with dash as separator,
     * e.g 'userName' -> 'user_name'.
     * <p/>
     * How to serialize with JsonHelper:
     * <pre>
     *    Foo foo = new Foo();
     *    foo.setFooBar("foo");
     *    System.out.println(JsonHelper.toJson(foo, JsonNameConventions.CAMEL_DASHES));
     * </pre>
     * Code above prints:
     * <pre>
     *    {"foo-bar":"foo"}
     * </pre>
     * <p/>
     * How to deserialize with JsonHelper:
     * <pre>
     *    class Foo {
     *       private String fooBar;
     *
     *       public String getFooBar() {
     *          return fooBar;
     *       }
     *
     *       public void setFooBar(String fooBar) {
     *          this.fooBar = fooBar;
     *       }
     *    }
     *
     *    ...
     *
     *    String json = "{\"foo-bar\":\"foo\"}";
     *    Foo foo = fromJson(json, Foo.class, null, JsonNameConventions.CAMEL_DASHES);
     *    System.out.println(foo.getFooBar());
     * </pre>
     * Code above prints:
     * <pre>
     *    foo
     * </pre>
     */
    CAMEL_DASH() {
        @Override
        public String toJsonName(String javaName) {
            return camelToDashed(javaName);
        }

        @Override
        public String toJavaName(String jsonName) {
            return separateWithToCamel(jsonName, '-');
        }
    };

    private static String camelToUnderscored(String src) {
        return camelToSeparateWith(src, '_');
    }

    private static String camelToDashed(String src) {
        return camelToSeparateWith(src, '-');
    }

    private static String camelToSeparateWith(String src, char separator) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = src.toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            if (Character.isUpperCase(chars[i])) {
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(Character.toLowerCase(chars[i]));
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    private static String separateWithToCamel(String src, char separator) {
        StringBuilder sb = new StringBuilder();
        final char[] chars = src.toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            if (chars[i] == separator) {
                if (i == 0 || i == (length - 1)) {
                    // add as is first or last character
                    sb.append(chars[i]);
                } else {
                    sb.append(Character.toUpperCase(chars[++i]));
                }
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

}
