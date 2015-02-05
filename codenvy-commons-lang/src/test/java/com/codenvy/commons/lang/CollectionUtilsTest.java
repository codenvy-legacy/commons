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

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vitaly Parfonov
 */
public class CollectionUtilsTest {

    @Test
    public void checkWithNullCollection() {
        List<Object> nullList = null;
        Assert.assertTrue(CollectionUtils.isEmpty(nullList));
        Assert.assertFalse(CollectionUtils.isNotEmpty(nullList));
    }

    @Test
    public void checkWithEmptyCollection() {
        List<Object> emptyList = new ArrayList<>();
        Assert.assertTrue(CollectionUtils.isEmpty(emptyList));
        Assert.assertFalse(CollectionUtils.isNotEmpty(emptyList));
    }

    @Test
    public void checkWithNullMap() {
        Map<?,?> nullMap = null;
        Assert.assertTrue(CollectionUtils.isEmpty(nullMap));
        Assert.assertFalse(CollectionUtils.isNotEmpty(nullMap));
    }

    @Test
    public void checkWithEmptyMap() {
        Map<?,?> emptyMap = new HashMap<>();
        Assert.assertTrue(CollectionUtils.isEmpty(emptyMap));
        Assert.assertFalse(CollectionUtils.isNotEmpty(emptyMap));
    }


    @Test
    public void checkWithNotEmptyList() {
        List<Object> list = new ArrayList<>(2);
        list.add("foo");
        list.add("bar");
        Assert.assertFalse(CollectionUtils.isEmpty(list));
        Assert.assertTrue(CollectionUtils.isNotEmpty(list));
    }



    @Test
    public void checkWithNotEmptyMap() {
        Map<String,String> emptyMap = new HashMap<>(2);
        emptyMap.put("foo","bar");
        emptyMap.put("bar","foo");
        Assert.assertFalse(CollectionUtils.isEmpty(emptyMap));
        Assert.assertTrue(CollectionUtils.isNotEmpty(emptyMap));
    }
}
