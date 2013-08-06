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
