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

    private static class MyEntry<V> {
        V    value;
        long created;


        MyEntry(V value) {
            this.value = value;
            this.created = System.currentTimeMillis();
        }
    }
}
