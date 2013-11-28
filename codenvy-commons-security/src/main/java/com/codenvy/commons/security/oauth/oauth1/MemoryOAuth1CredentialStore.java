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
package com.codenvy.commons.security.oauth.oauth1;

import org.scribe.model.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** In memory implementation of {@code OAuthCredentialStore} */
public class MemoryOAuth1CredentialStore implements OAuth1CredentialStore {

    /** Lock on access to the store. */
    private final Lock lock = new ReentrantLock();

    /** Store of memory persisted token, indexed by userId. */
    private final Map<String, Token> store = new HashMap<>();

    public void put(String userId, Token token) {
        lock.lock();
        try {
            Token item = new Token(token.getToken(), token.getSecret());
            store.put(userId, item);
        } finally {
            lock.unlock();
        }
    }

    public void delete(String userId) {
        lock.lock();
        try {
            store.remove(userId);
        } finally {
            lock.unlock();
        }
    }

    public Token get(String userId) {
        lock.lock();
        try {
            Token item = store.get(userId);
            if (item != null) {
                item = new Token(item.getToken(), item.getSecret());
            }
            return item;
        } finally {
            lock.unlock();
        }
    }
}