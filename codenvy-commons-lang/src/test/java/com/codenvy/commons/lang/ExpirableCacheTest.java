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

/** Test of ExpirableCache class */
public class ExpirableCacheTest {
    @Test
    public void shouldBeAbleToGetValueAfterPut() throws Exception {
        //given
        ExpirableCache<String, String> cache = new ExpirableCache<String, String>(500000, 100);
        cache.put("k1", "v1");
        //when
        String value = cache.get("k1");
        //then
        assertEquals(value, "v1");
        assertEquals(cache.getCacheSize(), 1);
    }

    @Test
    public void shouldNoBeAbleToGetValueMoreThenSize() throws Exception {
        //given
        ExpirableCache<String, String> cache = new ExpirableCache<String, String>(500000, 1);
        cache.put("k1", "v1");
        cache.put("k2", "v2");
        //when
        String value = cache.get("k1");
        //then
        assertNull(value);
        assertEquals(cache.getCacheSize(), 1);
    }


    @Test
    public void shouldNoBeAbleToGetValueAfterInvalidationTime() throws Exception {
        //given
        ExpirableCache<String, String> cache = new ExpirableCache<String, String>(100, 1);
        cache.put("k1", "v1");
        Thread.sleep(200);
        //when
        String value = cache.get("k1");
        //then
        assertNull(value);
        assertEquals(cache.getCacheSize(), 0);
    }

    @Test
    public void shouldNoBeAbleToEvictValueAfter500AttemptsToGetOtherValue() throws Exception {
        //given
        ExpirableCache<String, String> cache = new ExpirableCache<String, String>(100, 1);
        cache.put("k1", "v1");
        Thread.sleep(200);
        //when
        int i = 0;
        while (i++ <= 500) {
            cache.get("k2");
        }
        //then
        assertEquals(cache.getCacheSize(), 0);
    }
}
