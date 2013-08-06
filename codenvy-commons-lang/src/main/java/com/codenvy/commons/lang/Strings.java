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

/** Utility class to work with String */
public class Strings {
    private Strings() {
    }

    /**
     * Returns the given string if it is non-null; the empty string otherwise.
     *
     * @param string
     *         the string to test and possibly return
     * @return {@code string} itself if it is non-null; {@code ""} if it is null
     */
    public static String nullToEmpty(String string) {
        return (string == null) ? "" : string;
    }

    /**
     * Returns the given string if it is nonempty; {@code null} otherwise.
     *
     * @param string
     *         the string to test and possibly return
     * @return {@code string} itself if it is nonempty; {@code null} if it is
     *         empty or null
     */
    public static String emptyToNull(String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    /**
     * Returns {@code true} if the given string is null or is the empty string.
     * <p/>
     * <p>Consider normalizing your string references with {@link #nullToEmpty}.
     * If you do, you can use {@link String#isEmpty()} instead of this
     * method, and you won't need special null-safe forms of methods like {@link
     * String#toUpperCase} either. Or, if you'd like to normalize "in the other
     * direction," converting empty strings to {@code null}, you can use {@link
     * #emptyToNull}.
     *
     * @param string
     *         a string reference to check
     * @return {@code true} if the string is null or is the empty string
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * Returns a string containing the string representation of each of parts,
     * using configured separator between each.
     * @param delimiter
     *         separator placed between consecutive elements.
     * @param parts
     *         strings to concatenate
     * @return
     *         string containing the string representation of each of parts separated by delimiter
     */
    public static String join(String delimiter, String ... parts) {
        if (delimiter == null) {
            throw new IllegalArgumentException("First argument can't be null.");
        }

        StringBuilder sb = new StringBuilder();
        for (String alias : parts) {
            if (sb.length() != 0) {
                sb.append(delimiter);
            }
            sb.append(alias);
        }
        return sb.toString();
    }

    /**
     * Search longest common prefix.
     *
     * @param input
     *         - input array.
     * @return - longest common prefix of the input array of the string
     */
    public static String longestCommonPrefix(String... input) {
        String prefix = new String();
        if (input.length > 0) {
            prefix = input[0];
        }
        for (int i = 1; i < input.length; ++i) {
            String s = input[i];
            int j = 0;
            for (; j < Math.min(prefix.length(), s.length()); ++j) {
                if (prefix.charAt(j) != s.charAt(j)) {
                    break;
                }
            }
            prefix = prefix.substring(0, j);
        }
        return prefix;
    }

}
