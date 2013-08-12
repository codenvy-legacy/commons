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
package com.codenvy.logreader;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class reads log files from folder. Folder must have next format:
 * ${log.dir}/{tenant}/{year}/{month}/{day}/{hour}/{filename}
 * <p/>
 * Each file can be represented by token. Tokens for each files are different.
 * Tokens for files are unchanged.
 * <p/>
 * You can get file content by it's token. Also you can get next and previous tokens.
 */
public class LogReader {

    /**
     * Filter for skipping hidden files and folders.
     * For example: .svn and .git
     */
    private final static FileFilter fileFilter = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().equals("tenant.log")) {
                return false;
            }
            return !pathname.isHidden();
        }

    };

    /**
     * Comparator for sorting in format:
     * file-0
     * file-1
     * file-2
     * file-3
     * ...
     * file-9
     * file-10
     * file-11
     * ...
     * file-20
     * ...
     * <p/>
     * NOTE: expected that all files/folders have same prefix and suffix and differs only by number.
     * In other words, all files must matches by this regexp: {@code prefix[0-9]*suffix},
     * where prefix and suffix same for all files.
     * <p/>
     * WARNING: this implementation of file comparator not consider leading zeroes in file number.
     * So, file with name {@code file-0001.log} more than {@code file-02.log}. But file with name {@code file-0001
     * .log} less than {@code file-0002.log}.
     */
    private final static Comparator<File> fileComparator = new Comparator<File>() {

        @Override
        public int compare(File f1, File f2) {
            String n1 = f1.getName();
            String n2 = f2.getName();
            if (n1.length() > n2.length()) {
                return 1;
            }
            if (n1.length() < n2.length()) {
                return -1;
            }
            return n1.compareTo(n2);
        }

    };

    private File logDir;

    public LogReader(String logDir) {
        this.logDir = new File(logDir);
    }

    /**
     * Return last token from all log directory.
     *
     * @return last token
     * @throws LogReaderException
     */
    public String getLastToken() throws LogReaderException {
        return fileToToken(getLastFileInFolder(logDir));
    }

    /**
     * Return first token before {@code token}. If previous token not found then will be returned null.
     *
     * @param token
     * @return token before token
     * @throws LogReaderException
     */
    public String getPrevToken(String token) throws LogReaderException {
        File logFile = tokenToFile(token);
        return fileToToken(getPrevFile(logFile, logFile));
    }

    /**
     * Method returns true if previous token for {@code token} is exists, and false if not exists.
     *
     * @param token
     * @return
     * @throws LogReaderException
     */
    public boolean hasPrevToken(String token) throws LogReaderException {
        String prevToken = getPrevToken(token);
        return prevToken != null;
    }

    /**
     * Returns first token after {@code token}. If next token not found, then will be returned null.
     *
     * @param token
     * @return next token after {@code token}
     * @throws LogReaderException
     */
    public String getNextToken(String token) throws LogReaderException {
        File logFile = tokenToFile(token);
        return fileToToken(getNextFile(logFile, logFile));
    }

    /**
     * Method returns true if previous token for {@code token} is exists, and false if not exists.
     *
     * @param token
     * @return
     * @throws LogReaderException
     */
    public boolean hasNextToken(String token) throws LogReaderException {
        String nextToken = getNextToken(token);
        return nextToken != null;
    }

    /**
     * Returns content of log file represented by token.
     *
     * @param token
     *         token of log file
     * @return content of log file
     * @throws LogReaderException
     */
    public String getLogByToken(String token) throws LogReaderException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tokenToFile(token)));
            StringBuilder content = new StringBuilder();
            try {
                char[] cbuf = new char[1024];
                int length = 0;
                while (length >= 0) {
                    content.append(cbuf, 0, length);
                    length = reader.read(cbuf);
                }
            } finally {
                reader.close();
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            throw new LogReaderException("Log file with token " + token + " not found.", e);
        } catch (IOException e) {
            throw new LogReaderException("Error while reading file with token " + token + ".", e);
        }
    }

    /**
     * Returns file by token.
     *
     * @param token
     * @return
     * @throws LogReaderException
     *         if token has prohibited literals or token has invalid format
     */
    private File tokenToFile(String token) throws LogReaderException {
        if (token == null) {
            return null;
        }
        if (token.contains("/../")) {
            throw new LogReaderException("You cann't use wildcard symbols in token.");
        }
        File logFile = new File(logDir + token);
        if (!logFile.exists() || !logFile.isFile()) {
            throw new LogReaderException("Token has invalid format");
        }
        return logFile;
    }

    /**
     * Returns token by file.
     * Current token format:
     * /{year}/{month}/{day}/{hour}/{filename}
     *
     * @param file
     * @return
     * @throws LogReaderException
     *         if token has invalid format
     */
    private String fileToToken(File file) throws LogReaderException {
        if (file == null) {
            return null;
        }
        String filename = file.getAbsolutePath();
        String[] parts = filename.split("/");

        if (parts.length < 5) {
            throw new LogReaderException("Log file name is invalid.");
        }

        StringBuilder token = new StringBuilder();
        for (int i = parts.length - 5; i < parts.length; i++) {
            token.append("/");
            token.append(parts[i]);
        }

        return token.toString();
    }

    /**
     * Returns next file after {@code baseFile}. Before calling variable {@code currentFile} must be equals with
     * {@code baseFile}.
     * Algorithm is recursive. Recursion steps:
     * currentFile                                               baseFile
     * {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log      {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log
     * {log.dir}/{year}/{month}/{day}/{hour}                     {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log
     * {log.dir}/{year}/{month}/{day}                            {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log
     * {log.dir}/{year}/{month}                                  {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log
     * {log.dir}{year}                                           {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log
     * {log.dir}                                                 {log.dir}/{year}/{month}/{day}/{hour}/{filename}.log
     * <p/>
     * On each recursive level, algorithm try to find next file after {@code baseFile}.
     * If file is found, then recursion is finished, else - go to next level.
     * <p/>
     * For example:
     * currentFile                                  baseFile
     * 1)  {log.dir}/2011/07/13/15/default-5.log        {log.dir}/2011/07/13/15/default-5.log
     * try to find next file after "default-5.log" in folder "{log.dir/2011/07/13/15}".
     * For example, we found file "default-6.log", then recursion is finished. Answer: "{log
     * .dir}/2011/07/13/15/default-6.log"
     * If file "default-5.log" is last file in folder, then go to next level
     * <p/>
     * 2)  {log.dir}/2011/07/13/15                      {log.dir}/2011/07/13/15/default-5.log
     * In previous level we try to find next file after "default-5.log", but not found. Then we must try to find next
     * file in newer "hour" directory.
     * Now, we try to find "hour" directory after current - "15". For example we found directory "16",
     * then we get first file from it and returns.
     * If we not found next directory after "15", then we go to next level
     * <p/>
     * 3)  {log.dir}/2011/07/13                         {log.dir}/2011/07/13/15/default-5.log
     * Now, we try to find "day" directory after current - "13". If we found it, then return first file from it.
     * if we not found next directory, then we go to next level.
     * <p/>
     * 4)  {log.dir}/2011/07                            {log.dir}/2011/07/13/15/default-5.log
     * Similar to the previous level...
     * <p/>
     * 5)  {log.dir}/2011                               {log.dir}/2011/07/13/15/default-5.log
     * Similar to the previous level...
     * <p/>
     * 6)  {log.dir}                                    {log.dir}
     * Similar to the previous level, but if next file not found, then method returns null,
     * and recursion was finished anyway.
     *
     * @param currentFile
     * @param baseFile
     * @return
     * @throws LogReaderException
     */
    private File getNextFile(File currentFile, File baseFile) throws LogReaderException {
        // finish recursion if current directory is root log directory.
        if (currentFile.equals(logDir)) {
            return null;
        }
        File result = getFirstFileInFolder(getNextItemInFolder(currentFile));
        if (result != null) {
            return result;
        } else {
            return getNextFile(currentFile.getParentFile(), baseFile);
        }
    }

    /**
     * Similar with {@link LogReader#getNextFile(File, File)}, but on each level we try to find previous file.
     *
     * @param currentFile
     * @param baseFile
     * @return
     * @throws LogReaderException
     */
    private File getPrevFile(File currentFile, File baseFile) throws LogReaderException {
        // finish recursion if current directory is root log directory.
        if (currentFile.equals(logDir)) {
            return null;
        }
        File result = getLastFileInFolder(getPrevItemInFolder(currentFile));
        if (result != null) {
            return result;
        } else {
            return getPrevFile(currentFile.getParentFile(), baseFile);
        }
    }

    /**
     * Returns <b>next</b> file or directory after current.
     * For example:
     * currentFile = folder/file-1
     * In this case, method try to find file in directory {@code folder/} which <b>more</b> than {@code file-1}.
     * For comparing files used file comparator {@link LogReader#fileComparator}.
     *
     * @param currentFile
     * @return
     * @throws LogReaderException
     */
    private File getNextItemInFolder(File currentFile) throws LogReaderException {
        File[] files = currentFile.getParentFile().listFiles(fileFilter);
        Arrays.sort(files, fileComparator);

        int index = Arrays.binarySearch(files, currentFile, fileComparator);
        if (index < 0) {
            throw new LogReaderException("File with token " + fileToToken(currentFile) + " not found.");
        }

        if (index + 1 < files.length) {
            return files[index + 1];
        } else {
            return null;
        }
    }

    /**
     * Returns <b>previous</b> file or directory after current.
     * For example:
     * currentFile = folder/file-1
     * In this case, method try to find file in directory {@code folder/} which <b>less</b> than {@code file-1}.
     * For comparing files used file comparator {@link LogReader#fileComparator}.
     *
     * @param currentFile
     * @return
     * @throws LogReaderException
     */
    private File getPrevItemInFolder(File currentFile) throws LogReaderException {
        File[] files = currentFile.getParentFile().listFiles(fileFilter);
        Arrays.sort(files, fileComparator);

        int index = Arrays.binarySearch(files, currentFile, fileComparator);
        if (index < 0) {
            throw new LogReaderException("File with token " + fileToToken(currentFile) + " not found.");
        }

        if (index - 1 >= 0) {
            return files[index - 1];
        } else {
            return null;
        }
    }

    /**
     * Returns first file in current directory.
     * This method uses recursive algorithm, where in all levels we get least item. If this item is file,
     * then this is answer.
     * If this item is directory, then this method will be called recursively with new folder as parameter.
     * For comparing files used file comparator {@link LogReader#fileComparator}.
     *
     * @param currentDir
     * @return
     */
    private File getFirstFileInFolder(File currentDir) {
        if (currentDir == null) {
            return null;
        }
        if (currentDir.isFile()) {
            return currentDir;
        }
        File[] files = currentDir.listFiles(fileFilter);
        if (files == null || files.length == 0) {
            return null;
        }
        Arrays.sort(files, fileComparator);

        if (files[0].isDirectory()) {
            return getFirstFileInFolder(files[0]);
        } else {
            return files[0];
        }
    }

    /**
     * Returns last file in current directory.
     * This method uses recursive algorithm, where in all levels we get biggest item. If this item is file,
     * then this is answer.
     * If this item is directory, then this method will be called recursively with new folder as parameter.
     * For comparing files used file comparator {@link LogReader#fileComparator}.
     *
     * @param currentDir
     * @return
     */
    private File getLastFileInFolder(File currentDir) {
        if (currentDir == null) {
            return null;
        }
        if (currentDir.isFile()) {
            return currentDir;
        }
        File[] files = currentDir.listFiles(fileFilter);
        if (files == null || files.length == 0) {
            return null;
        }
        Arrays.sort(files, fileComparator);

        if (files[files.length - 1].isDirectory()) {
            return getLastFileInFolder(files[files.length - 1]);
        } else {
            return files[files.length - 1];
        }
    }

}
