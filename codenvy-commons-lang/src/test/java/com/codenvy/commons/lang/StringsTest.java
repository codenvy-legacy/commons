/*
 * Copyright (C) 2013 eXo Platform SAS.
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
