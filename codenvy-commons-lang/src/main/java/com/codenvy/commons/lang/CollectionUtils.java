/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.lang;

import java.util.Collection;
import java.util.Map;

/**
 * @author Vitaly Parfonov
 */
public class CollectionUtils {


    /**
     * check if the specified collection is empty.
     * If null returns true.
     *
     * @param coll
     *         the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }


    /**
     * check if the specified collection is not empty.
     * null returns true.
     *
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }


    /**
     * check if the specified map is empty.
     * if null returns true.
     *
     * @param map  the map to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * check if the specified map is not empty.
     * null returns false.
     */
    public static boolean isNotEmpty(final Map<?,?> map) {
        return !isEmpty(map);
    }

}
