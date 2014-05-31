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

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class LogReaderExceptionMapper implements ExceptionMapper<LogReaderException> {

    /** @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable) */
    @Override
    public Response toResponse(LogReaderException exception) {
        return Response.status(204).header("JAXRS-Body-Provided", "Error-Message").entity(exception.getLocalizedMessage
                ()).type("text/plain").build();
    }

}
