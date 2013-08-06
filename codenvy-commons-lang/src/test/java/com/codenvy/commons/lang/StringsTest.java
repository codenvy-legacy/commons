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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class StringsTest {

    @Test
    public void testNullToEmpty() {
        assertEquals(Strings.nullToEmpty(null), "");
        assertEquals(Strings.nullToEmpty(""), "");
        assertEquals(Strings.nullToEmpty("a"), "a");
    }

    @Test
    public void testEmptyToNull() {
        assertNull(Strings.emptyToNull(null));
        assertNull(Strings.emptyToNull(""));
        assertEquals(Strings.emptyToNull("a"), "a");
    }

    @Test
    public void testIsNullOrEmpty() {
        assertTrue(Strings.isNullOrEmpty(null));
        assertTrue(Strings.isNullOrEmpty(""));
        assertFalse(Strings.isNullOrEmpty("a"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testJoinShouldThrowIllegalArgumentExceptionIfDelimiterIsNull() {
        assertEquals(Strings.join(null, "1", "2"), "");
    }

    @Test
    public void testJoin() {
        assertEquals(Strings.join(",", "1", "2"), "1,2");
        assertEquals(Strings.join("1", "1", "1"), "111");
        assertEquals(Strings.join(",", "2"), "2");
        assertEquals(Strings.join(","), "");
    }

    @Test
    public void shouldReturnEmptyStringOnEmptyParameters_longestCommonPrefix(){
        assertEquals("", Strings.longestCommonPrefix());

    }

    @Test
    public void shouldReturnSameStringIf1Parameter_longestCommonPrefix(){
        assertEquals("param", Strings.longestCommonPrefix("param"));

    }

    @Test
    public void shouldReturnEmptyIfNoCommonPrefix_longestCommonPrefix(){
        assertEquals("", Strings.longestCommonPrefix("dff","blafa"));

    }

    @Test
    public void shouldFindCommonPrefix_longestCommonPrefix(){
        assertEquals("bla", Strings.longestCommonPrefix("blafoijqoweir","blafa", "blamirfjo"));

    }
}
