/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.commons.lang.cache;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/** A compound iterator, which iterates over two or more other iterators and represents few iterators as one. */
public class CompoundIterator<T> implements Iterator<T> {

    private final Iterator[] iterators;
    private       int        index;

    public CompoundIterator(Iterator<T> iterator1, Iterator<T> iterator2) {
        iterators = new Iterator[]{iterator1, iterator2};
    }

    public CompoundIterator(List<Iterator<T>> iterators) {
        this.iterators = iterators.toArray(new Iterator[iterators.size()]);
    }

    public boolean hasNext() {
        while (index < iterators.length) {
            if (iterators[index].hasNext()) {
                return true;
            }
            index++;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return (T)iterators[index].next();
    }

    public void remove() {
        iterators[index].remove();
    }
}
