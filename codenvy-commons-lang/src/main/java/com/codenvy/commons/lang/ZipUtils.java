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

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Utils for ZIP.
 *
 * @author Eugene Voevodin
 */
public class ZipUtils {

    public static void zipDir(String zipRootPath, File dir, File zip, FilenameFilter filter) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory. ");
        }
        if (filter == null) {
            filter = IoUtil.ANY_FILTER;
        }
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)))) {
            zipOut.setLevel(0);
            byte[] b = new byte[1024];
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
                        final String zipEntryName = f.getAbsolutePath().substring(zipRootPath.length() + 1).replace('\\', '/');
                        if (f.isDirectory()) {
                            q.push(f);
                            zipOut.putNextEntry(new ZipEntry(zipEntryName.endsWith("/") ? zipEntryName : (zipEntryName + '/')));
                            zipOut.closeEntry();
                        } else {
                            zipOut.putNextEntry(new ZipEntry(zipEntryName));
                            try (InputStream in =  new BufferedInputStream(new FileInputStream(f))) {
                                int r;
                                while ((r = in.read(b)) != -1) {
                                    zipOut.write(b, 0, r);
                                }
                            } finally {
                                zipOut.closeEntry();
                            }
                        }
                    }
                }
            }
            zipOut.finish();
        }
    }

    public static Collection<String> listEntries(File zip) throws IOException {
        return listEntries(new FileInputStream(zip));
    }

    public static Collection<String> listEntries(InputStream in) throws IOException {
        List<String> list = new ArrayList<String>();
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(in);
            ZipEntry zipEntry;
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    list.add(zipEntry.getName());
                }
                zipIn.closeEntry();
            }
        } finally {
            if (zipIn != null) {
                zipIn.close();
            }
            in.close();
        }
        return list;
    }

    public static void unzip(File zip, File targetDir) throws IOException {
        unzip(new FileInputStream(zip), targetDir);
    }

    public static void unzip(InputStream in, File targetDir) throws IOException {
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(in);
            byte[] b = new byte[8192];
            ZipEntry zipEntry;
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                File file = new File(targetDir, zipEntry.getName());
                if (!zipEntry.isDirectory()) {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    try {
                        int r;
                        while ((r = zipIn.read(b)) != -1) {
                            fos.write(b, 0, r);
                        }
                    } finally {
                        fos.close();
                    }
                } else {
                    file.mkdirs();
                }
                zipIn.closeEntry();
            }
        } finally {
            if (zipIn != null) {
                zipIn.close();
            }
            in.close();
        }
    }


    /**
     * Checks is specified file is zip file or not. Zip file <a href="http://en.wikipedia.org/wiki/Zip_(file_format)#File_headers">headers
     * description</a>.
     */
    public static boolean isZipFile(java.io.File file) throws IOException {
        if (file.isDirectory()) {
            return false;
        }
        // NOTE: little-indian bytes order!
        byte[] bytes = new byte[4];
        try (FileInputStream fIn = new FileInputStream(file)) {
            if (fIn.read(bytes) != 4) {
                return false;
            }
        }
        final int header = bytes[0] + (bytes[1] << 8) + (bytes[2] << 16) + (bytes[3] << 24);
        return 0x04034b50 == header;
    }


    /**
     * Merges set of zip archives into one zip archive.
     *
     * @param archives
     *         set of archives that will be merged
     * @param targetArchive
     *         result of merging. If file exists it will be replaced with new file.
     * @param tmpDirParent
     *         parent directory that will be used for temporary file storing. If it is {@code null} system property java.io.tmpdir will be
     *         used. All temp files will be deleted after merging.
     * @throws IOException when it is not possible to create temp directory, check file type, zip directory or unzip archive.
     */
    public static void mergeArchives(java.io.File targetArchive, java.io.File tmpDirParent, java.io.File... archives) throws IOException {
        if (tmpDirParent == null) {
            tmpDirParent = new File(System.getProperty("java.io.tmpdir"));
        }
        File tmp = Files.createTempDirectory(tmpDirParent.toPath(), "tmp").toFile();
        try {
            for (File archive : archives) {
                if (isZipFile(archive)) {
                    unzip(archive, tmp);
                }
            }
            zipDir(tmp.getAbsolutePath(), tmp, targetArchive, IoUtil.ANY_FILTER);
        } finally {
            IoUtil.deleteRecursive(tmp);
        }
    }

    private ZipUtils() {
    }
}
