/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.logreader;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class LogReaderServiceApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> cls = new HashSet<Class<?>>(1);
        cls.add(LogReaderService.class);
        return cls;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> objs = new HashSet<Object>();
        objs.add(new LogReaderExceptionMapper());
        return objs;
    }

}
