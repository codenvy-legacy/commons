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

/**
 * @author Sergii Leschenko
 * @author Alexander Garagatyi
 */
public class MemoryUtils {
    /**
     * Converts String RAM with suffix GB or MB to int RAM in MB.
     * e.g.
     * "1GB" -> 1024
     *
     * @param RAM
     *         string RAM in GB or MB
     * @return int RAM in MB
     */
    public static int convert(String RAM) {
        int ramMb;
        String unit = RAM.substring(RAM.length() - 2);
        switch (unit) {
            case "GB":
                ramMb = 1024 * Integer.parseInt(RAM.substring(0, RAM.length() - 2));
                break;
            case "MB":
                ramMb = Integer.parseInt(RAM.substring(0, RAM.length() - 2));
                break;
            default:
                throw new IllegalArgumentException("Unknown unit " + unit);
        }

        return ramMb;
    }
}
