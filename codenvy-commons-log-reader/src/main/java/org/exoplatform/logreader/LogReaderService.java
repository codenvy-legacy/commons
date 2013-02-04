/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package org.exoplatform.logreader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/log-reader-service/")
public class LogReaderService
{

   private final LogPathProvider logPathProvider;

   public LogReaderService(LogPathProvider logPathProvider)
   {
      this.logPathProvider = logPathProvider;
   }

   @GET
   @Path("last-log")
   @Produces(MediaType.APPLICATION_JSON)
   public LogEntry getLastLog() throws LogReaderException
   {
      LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
      String token = logReader.getLastToken();
      if (token != null)
      {
         return new LogEntry(token, logReader.getLogByToken(token), logReader.hasNextToken(token),
            logReader.hasPrevToken(token));
      }
      else
      {
         throw new LogReaderException("No logs found.");
      }
   }

   @GET
   @Path("log")
   @Produces(MediaType.APPLICATION_JSON)
   public LogEntry getLog(@QueryParam("token") String token) throws LogReaderException
   {

      LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
      if (!token.equals("null"))
      {
         return new LogEntry(token, logReader.getLogByToken(token), logReader.hasNextToken(token),
            logReader.hasPrevToken(token));
      }
      else
      {
         throw new LogReaderException("Token must not be null.");
      }
   }

   @GET
   @Path("prev-log")
   @Produces(MediaType.APPLICATION_JSON)
   public LogEntry getPrevLog(@QueryParam("token") String token) throws LogReaderException
   {

      LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
      String newToken = logReader.getPrevToken(token);
      if (newToken != null)
      {
         return new LogEntry(newToken, logReader.getLogByToken(newToken), logReader.hasNextToken(newToken),
            logReader.hasPrevToken(newToken));
      }
      else
      {
         throw new LogReaderException("Previous token not found.");
      }
   }

   @GET
   @Path("next-log")
   @Produces(MediaType.APPLICATION_JSON)
   public LogEntry getNextLog(@QueryParam("token") String token) throws LogReaderException
   {
      LogReader logReader = new LogReader(logPathProvider.getLogDirectory());
      String newToken = logReader.getNextToken(token);
      if (newToken != null)
      {
         return new LogEntry(newToken, logReader.getLogByToken(newToken), logReader.hasNextToken(newToken),
            logReader.hasPrevToken(newToken));
      }
      else
      {
         throw new LogReaderException("Next token not found.");
      }
   }

}
