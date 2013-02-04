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
package org.codenvy.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StreamUtil
{

   private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);

   private StreamUtil()
   {
   }

   /**
    * Reads bytes from input stream and builds a string from them.
    *
    * @param inputStream
    *    source stream
    * @return string
    * @throws java.io.IOException
    *    if any i/o error occur
    */
   public static String readStream(InputStream inputStream) throws IOException
   {
      if (inputStream == null)
      {
         return null;
      }

      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      byte[] buf = new byte[8192];
      int r;
      while ((r = inputStream.read(buf)) != -1)
      {
         bout.write(buf, 0, r);
      }
      return bout.toString();
   }

   /**
    * Reads bytes from input stream and builds a string from them. InputStream
    * closed after consumption.
    *
    * @param inputStream
    *    source stream
    * @return string
    * @throws java.io.IOException
    *    if any i/o error occur
    */
   public static String readStreamAndClose(InputStream inputStream) throws IOException
   {
      try
      {
         return readStream(inputStream);
      }
      catch (IOException e)
      {
         LOG.error(e.getLocalizedMessage(), e);
         throw e;
      }
      finally
      {
         if (inputStream != null)
         {
            try
            {
               inputStream.close();
            }
            catch (IOException e)
            {
               LOG.error(e.getLocalizedMessage(), e);
            }
         }
      }
   }

   /**
    * Execute post request and return status code.
    *
    * @param requestURL
    * @return response code
    * @throws IOException
    */
   public static int executePostRequest(String requestURL) throws IOException
   {
      HttpURLConnection conn = null;
      try
      {
         conn = (HttpURLConnection)new URL(requestURL).openConnection();
         conn.setRequestMethod("POST");
         return conn.getResponseCode();
      }
      finally
      {
         if (conn != null)
         {
            conn.disconnect();
         }
      }
   }

   /**
    * Execute get request and return status code.
    *
    * @param requestURL
    * @return response code
    * @throws IOException
    */
   public static int executeGetRequest(String requestURL) throws IOException
   {
      HttpURLConnection conn = null;
      try
      {
         conn = (HttpURLConnection)new URL(requestURL).openConnection();
         conn.setRequestMethod("GET");
         return conn.getResponseCode();
      }
      finally
      {
         if (conn != null)
         {
            conn.disconnect();
         }
      }
   }

   /**
    * Check if response code is successful.
    *
    * @param responseStatus
    * @return - true if response code is successful.
    */
   public static boolean isResponseSuccessful(int responseStatus)
   {
      return (responseStatus / 100) == 2;
   }

   /**
    * Return page content.
    *
    * @param requestURL
    * @return
    * @throws IOException
    */
   public static String getRequestContent(String requestURL) throws IOException
   {

      HttpURLConnection conn = null;
      try
      {
         LOG.debug("Execute request to {}", requestURL);
         conn = (HttpURLConnection)new URL(requestURL).openConnection();
         return readStreamAndClose(conn.getInputStream());
      }
      catch (IOException e)
      {
         LOG.error(e.getLocalizedMessage(), e);
         throw e;
      }
      finally
      {
         if (conn != null)
         {
            conn.disconnect();
         }
      }

   }
}
