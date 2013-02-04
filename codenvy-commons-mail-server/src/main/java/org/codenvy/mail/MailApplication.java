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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Set of email services.
 */
public class MailApplication extends Application
{
   private final Set<Object> objects = new LinkedHashSet<Object>();

   public MailApplication()
   {
      objects.add(new MailSender());
   }

   @Override
   public Set<Object> getSingletons()
   {
      return objects;
   }

   @Override
   public Set<Class<?>> getClasses()
   {
      return Collections.emptySet();
   }
}
