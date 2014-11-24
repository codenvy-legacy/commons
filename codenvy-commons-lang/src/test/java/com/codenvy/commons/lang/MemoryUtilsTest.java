/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.lang;

import org.testng.annotations.Test;

import static com.codenvy.commons.lang.MemoryUtils.convert;
import static org.testng.Assert.assertEquals;

/**
 * Test for {@link MemoryUtilsTest}
 *
 * @author Sergii Leschenko
 */
public class MemoryUtilsTest {

    @Test
    public void shouldConvertMegabyte() {
        assertEquals(256, convert("256MB"));
    }

    @Test
    public void shouldConvertMegabyteIfUnitInLowerCase() {
        assertEquals(256, convert("256mb"));
    }

    @Test
    public void shouldConvertGigabyte() {
        assertEquals(2048, convert("2GB"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Illegal size of memory")
    public void shouldThrowIllegalArgumentExceptionIfInputStringContainsIncorrectMemorySize() {
        convert("1");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Unknown unit GG")
    public void shouldThrowIllegalArgumentIfUnknownUnit() {
        String memorySize = "1000GG";
        convert(memorySize);
    }

    @Test(expectedExceptions = NumberFormatException.class, expectedExceptionsMessageRegExp = "For input string: \"one23\"")
    public void shouldThrowNumberFormatExceptionIfIncorrectNumber() {
        String memorySize = "one23GB";
        convert(memorySize);
    }
}
