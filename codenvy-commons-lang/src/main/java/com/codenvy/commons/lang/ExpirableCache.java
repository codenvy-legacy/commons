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

import java.util.LinkedHashMap;
import java.util.Map;

public class ExpirableCache<K, V> {
    private final int                cacheSize;
    private final long               expiredAfter;
    private final int                queryCountBeforeCleanup;
    private final Map<K, MyEntry<V>> map;

    private int queryCount;

    public ExpirableCache(long expiredAfter, int cacheSize) {
        this.expiredAfter = expiredAfter;
        this.cacheSize = cacheSize;
        queryCountBeforeCleanup = 500;
        map = new LinkedHashMap<K, MyEntry<V>>(this.cacheSize + 1, 1.1f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, MyEntry<V>> eldest) {
                return size() > ExpirableCache.this.cacheSize;
            }
        };
    }

    public V get(K key) {
        if (++queryCount >= queryCountBeforeCleanup) {
            cleanup();
        }
        MyEntry<V> myEntry = map.get(key);
        if (myEntry != null) {
            if (System.currentTimeMillis() - myEntry.created < expiredAfter) {
                return myEntry.value;
            } else {
                map.remove(key);
            }
        }
        return null;
    }

    public void put(K key, V value) {
        if (++queryCount >= queryCountBeforeCleanup) {
            cleanup();
        }
        MyEntry<V> myEntry = map.get(key);
        if (myEntry != null) {
            myEntry.created = System.currentTimeMillis();
            myEntry.value = value;
        } else {
            map.put(key, new MyEntry<V>(value));
        }
    }

    @SuppressWarnings("unchecked")
    private void cleanup() {
        Object[] keys = map.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            K key = (K)keys[i];
            MyEntry<V> myEntry = map.get(key);
            if (myEntry != null) {
                if (System.currentTimeMillis() - myEntry.created > expiredAfter) {
                    map.remove(key);
                }
            }
        }
        queryCount = 0;
    }

    /** @return - number of cached entries. */
    public int getCacheSize() {
        return map.size();
    }

    private static class MyEntry<V> {
        V    value;
        long created;


        MyEntry(V value) {
            this.value = value;
            this.created = System.currentTimeMillis();
        }
    }
}
