/*
 * Copyright (C) 2012 eXo Platform SAS.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class IoOUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IoOUtil.class);

    private IoOUtil() {
    }

    /**
     * Reads bytes from input stream and builds a string from them.
     *
     * @param inputStream
     *         source stream
     * @return string
     * @throws java.io.IOException
     *         if any i/o error occur
     */
    public static String readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int r;
        while ((r = inputStream.read(buf)) != -1) {
            bout.write(buf, 0, r);
        }
        return bout.toString();
    }

    /**
     * Reads bytes from input stream and builds a string from them.
     * InputStream closed after consumption.
     *
     * @param inputStream
     *         source stream
     * @return string
     * @throws java.io.IOException
     *         if any i/o error occur
     */
    public static String readAndCloseQuietly(InputStream inputStream) throws IOException {
        try {
            return readStream(inputStream);
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
     * Looking for resource by given path. If no file exist by this path, method will try to find it in context.
     *
     * @param resource
     *         - path to resource
     * @return -   InputStream of resource
     * @throws IOException
     */
    public static InputStream getResource(String resource) throws IOException {
        InputStream is = null;

        File resourceFile = new File(resource);
        if (resourceFile.exists() && !resourceFile.isFile()) {
            throw new IOException(resourceFile.getAbsolutePath() + " is not a file. ");
        }
        is = resourceFile.exists() ? new FileInputStream(resourceFile) : IoOUtil.class.getResourceAsStream(resource);
        if (is == null) {
            throw new IOException("Not found resource: " + resource);
        }
        return is;
    }

    /** Remove directory and all its sub-resources with specified path */
    public static boolean removeDirectory(String pathToDir) {
        File directory = new File(pathToDir);

        if (!directory.exists()) {
            return true;
        }
        if (!directory.isDirectory()) {
            return false;
        }

        String[] list = directory.list();

        if (list != null) {
            for (String element : list) {
                File entry = new File(directory, element);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry.getPath())) {
                        return false;
                    }
                } else {
                    if (!entry.delete()) {
                        return false;
                    }
                }
            }
        }

        return directory.delete();
    }
}
