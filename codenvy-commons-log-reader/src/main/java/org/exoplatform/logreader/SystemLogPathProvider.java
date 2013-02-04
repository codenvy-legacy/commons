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

/**
 * @author <a href="mailto:kregent@exoplatform.com">Kostya Regent</a>
 * @version $Id: Jul 15, 2011 9:56:01 AM $
 *
 */
public class SystemLogPathProvider implements LogPathProvider
{

   public static final String LOG_DIR_PROPERTY = "org.exoplatform.logreader.logpath";

   /**
    * @see org.exoplatform.logreader.LogPathProvider#getLogDirectory()
    */
   @Override
   public String getLogDirectory()
   {
      return System.getProperty(LOG_DIR_PROPERTY);
   }

}
