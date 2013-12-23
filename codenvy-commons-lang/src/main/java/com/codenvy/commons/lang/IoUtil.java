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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IoUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IoUtil.class);

    private IoUtil() {
    }

    /** Represents filter what select any file */
    public static final FilenameFilter ANY_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return true;
        }
    };
    /** Represent filter what select only .git entries. */
    public static final FilenameFilter GIT_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return !(".git".equals(name));
        }
    };

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
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int r;
        while ((r = inputStream.read(buf)) != -1) {
            bout.write(buf, 0, r);
        }
        return bout.toString();
    }

    /**
     * Reads bytes from input stream and builds a string from them. InputStream closed after consumption.
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
     * @return - InputStream of resource
     * @throws IOException
     */
    public static InputStream getResource(String resource) throws IOException {
        InputStream is = null;

        File resourceFile = new File(resource);
        if (resourceFile.exists() && !resourceFile.isFile()) {
            throw new IOException(resourceFile.getAbsolutePath() + " is not a file. ");
        }
        is = resourceFile.exists() ? new FileInputStream(resourceFile) : IoUtil.class.getResourceAsStream(resource);
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


    /**
     * Remove specified file or directory.
     *
     * @param fileOrDirectory
     *         the file or directory to cancel
     * @return <code>true</code> if specified File was deleted and <code>false</code> otherwise
     */
    public static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] list = fileOrDirectory.listFiles();
            if (list == null) {
                return false;
            }
            for (File f : list) {
                if (!deleteRecursive(f)) {
                    return false;
                }
            }
        }
        return !fileOrDirectory.exists() || fileOrDirectory.delete();
    }

    /**
     * Remove specified file or directory.
     *
     * @param fileOrDirectory
     *         the file or directory to cancel
     * @param followLinks
     *         are symbolic links followed or not?
     * @return <code>true</code> if specified File was deleted and <code>false</code> otherwise
     */
    public static boolean deleteRecursive(File fileOrDirectory, boolean followLinks) {
        if (fileOrDirectory.isDirectory()) {
            // If fileOrDirectory represents a symbolic link to a folder,
            // do not read a target folder content. Just remove this symbolic link.
            if (!followLinks && java.nio.file.Files.isSymbolicLink(fileOrDirectory.toPath())) {
                return !fileOrDirectory.exists() || fileOrDirectory.delete();
            }
            File[] list = fileOrDirectory.listFiles();
            if (list == null) {
                return false;
            }
            for (File f : list) {
                if (!deleteRecursive(f, followLinks)) {
                    return false;
                }
            }
        }
        return !fileOrDirectory.exists() || fileOrDirectory.delete();
    }

    private static final SecureRandom DIR_NAME_GENERATOR = new SecureRandom();

    /**
     * Create temporary directory and use specified parent. If parent is <code>null</code> then use 'java.io.tmpdir'.
     *
     * @param parent
     *         parent
     * @param prefix
     *         prefix, may not be <code>null</code> and must be at least three characters long
     * @return newly create directory
     * @throws java.io.IOException
     *         if creation of new directory failed
     * @deprecated - Use Files.createTempDirectory
     */
    public static File createTempDirectory(File parent, String prefix) throws IOException {

        if (prefix == null) {
            throw new IllegalArgumentException("Prefix may not be null. ");
        }
        if (prefix.length() < 3) {
            throw new IllegalArgumentException("Prefix is too short. Must be at least three characters long. ");
        }
        if (parent == null) {
            parent = new File(System.getProperty("java.io.tmpdir"));
        }
        File dir = new File(parent, prefix + Long.toString(Math.abs(DIR_NAME_GENERATOR.nextLong())));
        if (!dir.mkdirs()) {
            throw new IOException(String.format("Unable create temp directory %s", dir.getAbsolutePath()));
        }
        return dir;
    }

    /**
     * Create temporary directory and use 'java.io.tmpdir' as parent.
     *
     * @param prefix
     *         prefix, may not be <code>null</code> and must be at least three characters long
     * @return newly create directory
     * @throws java.io.IOException
     *         if creation of new directory failed
     * @deprecated - Use Files.createTempDirectory
     */
    public static File createTempDirectory(String prefix) throws IOException {
        return createTempDirectory(null, prefix);
    }

    /**
     * Download file.
     *
     * @param parent
     *         parent directory, may be <code>null</code> then use 'java.io.tmpdir'
     * @param prefix
     *         prefix of temporary file name, may not be <code>null</code> and must be at least three characters long
     * @param suffix
     *         suffix of temporary file name, may be <code>null</code>
     * @param url
     *         URL for download
     * @return downloaded file
     * @throws java.io.IOException
     *         if any i/o error occurs
     */
    public static File downloadFile(File parent, String prefix, String suffix, URL url) throws IOException {
        File file = File.createTempFile(prefix, suffix, parent);
        URLConnection conn = null;
        final String protocol = url.getProtocol().toLowerCase();
        try {
            conn = url.openConnection();
            if ("http".equals(protocol) || "https".equals(protocol)) {
                HttpURLConnection http = (HttpURLConnection)conn;
                http.setInstanceFollowRedirects(false);
                http.setRequestMethod("GET");
            }
            InputStream input = conn.getInputStream();
            FileOutputStream fOutput = null;
            try {
                fOutput = new FileOutputStream(file);
                byte[] b = new byte[8192];
                int r;
                while ((r = input.read(b)) != -1) {
                    fOutput.write(b, 0, r);
                }
            } finally {
                if (fOutput != null) {
                    fOutput.close();
                }
                input.close();
            }
        } finally {
            if (conn != null && "http".equals(protocol) || "https".equals(protocol)) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
        return file;
    }

    /**
     * Copy file or directory to the specified destination. Existed files in destination directory will be overwritten.
     *
     * @param source
     *         copy source
     * @param target
     *         copy destination
     * @param filter
     *         copy filter
     * @throws java.io.IOException
     *         if any i/o error occurs
     */
    public static void copy(File source, File target, FilenameFilter filter) throws IOException {
        copy(source, target, filter, false, true);
    }

    /**
     * Copy file or directory to the specified destination. Existed files in destination directory will be overwritten.
     * <p/>
     * This method use java.nio for coping files.
     *
     * @param source
     *         copy source
     * @param target
     *         copy destination
     * @param filter
     *         copy filter
     * @throws java.io.IOException
     *         if any i/o error occurs
     */
    public static void nioCopy(File source, File target, FilenameFilter filter) throws IOException {
        copy(source, target, filter, true, true);
    }

    /**
     * Copy file or directory to the specified destination.
     *
     * @param source
     *         copy source
     * @param target
     *         copy destination
     * @param filter
     *         copy filter
     * @param replaceIfExists
     *         if <code>true</code>  existed files in destination directory will be overwritten
     * @throws java.io.IOException
     *         if any i/o error occurs
     */
    public static void copy(File source, File target, FilenameFilter filter, boolean replaceIfExists) throws IOException {
        copy(source, target, filter, false, replaceIfExists);
    }

    /**
     * Copy file or directory to the specified destination.
     * <p/>
     * This method use java.nio for coping files.
     *
     * @param source
     *         copy source
     * @param target
     *         copy destination
     * @param filter
     *         copy filter
     * @param replaceIfExists
     *         if <code>true</code>  existed files in destination directory will be overwritten
     * @throws java.io.IOException
     *         if any i/o error occurs
     */
    public static void nioCopy(File source, File target, FilenameFilter filter, boolean replaceIfExists)
            throws IOException {
        copy(source, target, filter, true, replaceIfExists);
    }

    private static void copy(File source, File target, FilenameFilter filter, boolean nio, boolean replaceIfExists)
            throws IOException {
        if (source.isDirectory()) {
            if (!(target.exists() || target.mkdirs())) {
                throw new IOException(String.format("Unable create directory '%s'. ", target.getAbsolutePath()));
            }
            if (filter == null) {
                filter = ANY_FILTER;
            }
            String sourceRoot = source.getAbsolutePath();
            LinkedList<File> q = new LinkedList<File>();
            q.add(source);
            while (!q.isEmpty()) {
                File current = q.pop();
                File[] list = current.listFiles();
                if (list != null) {
                    for (File f : list) {
                        if (!filter.accept(current, f.getName())) {
                            continue;
                        }
                        File newFile = new File(target, f.getAbsolutePath().substring(sourceRoot.length() + 1));
                        if (f.isDirectory()) {
                            if (!(newFile.exists() || newFile.mkdirs())) {
                                throw new IOException(
                                        String.format("Unable create directory '%s'. ", newFile.getAbsolutePath()));
                            }
                            if (!f.equals(target)) {
                                q.push(f);
                            }
                        } else {
                            if (nio) {
                                nioCopyFile(f, newFile, replaceIfExists);
                            } else {
                                copyFile(f, newFile, replaceIfExists);
                            }
                        }
                    }
                }
            }
        } else {
            File parent = target.getParentFile();
            if (!(parent.exists() || parent.mkdirs())) {
                throw new IOException(String.format("Unable create directory '%s'. ", parent.getAbsolutePath()));
            }
            if (nio) {
                nioCopyFile(source, target, replaceIfExists);
            } else {
                copyFile(source, target, replaceIfExists);
            }
        }
    }

    private static void copyFile(File source, File target, boolean replaceIfExists) throws IOException {
        if (!target.createNewFile()) // atomic
        {
            if (target.exists() && !replaceIfExists) {
                throw new IOException(String.format("File '%s' already exists. ", target.getAbsolutePath()));
            }
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] b = new byte[8192];
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(target);
            int r;
            while ((r = in.read(b)) != -1) {
                out.write(b, 0, r);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private static void nioCopyFile(File source, File target, boolean replaceIfExists) throws IOException {
        if (!target.createNewFile()) // atomic
        {
            if (target.exists() && !replaceIfExists) {
                throw new IOException(String.format("File '%s' already exists. ", target.getAbsolutePath()));
            }
        }
        FileInputStream sourceStream = null;
        FileOutputStream targetStream = null;
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceStream = new FileInputStream(source);
            targetStream = new FileOutputStream(target);
            sourceChannel = sourceStream.getChannel();
            targetChannel = targetStream.getChannel();
            final long size = sourceChannel.size();
            long transferred = 0L;
            while (transferred < size) {
                transferred += targetChannel.transferFrom(sourceChannel, transferred, (size - transferred));
            }
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (targetChannel != null) {
                targetChannel.close();
            }
            if (sourceStream != null) {
                sourceStream.close();
            }
            if (targetStream != null) {
                targetStream.close();
            }
        }
    }

    public static List<File> list(File dir, FilenameFilter filter) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory. ");
        }
        if (filter == null) {
            filter = ANY_FILTER;
        }
        List<File> files = new ArrayList<File>();
        LinkedList<File> q = new LinkedList<File>();
        q.add(dir);
        while (!q.isEmpty()) {
            File current = q.pop();
            File[] list = current.listFiles();
            if (list != null) {
                for (File f : list) {
                    if (!filter.accept(current, f.getName())) {
                        continue;
                    }
                    if (f.isDirectory()) {
                        q.push(f);
                    } else {
                        files.add(f);
                    }
                }
            }
        }
        return files;
    }

    public static String countFileHash(File file, MessageDigest digest) throws IOException {
        FileInputStream fis = null;
        DigestInputStream dis = null;
        byte[] b = new byte[8192];
        try {
            fis = new FileInputStream(file);
            dis = new DigestInputStream(fis, digest);
            while (dis.read(b) != -1) {
            }
            return toHex(digest.digest());
        } finally {
            if (dis != null) {
                dis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    private static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHex(byte[] hash) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            b.append(hex[(hash[i] >> 4) & 0x0f]);
            b.append(hex[hash[i] & 0x0f]);
        }
        return b.toString();
    }

}
