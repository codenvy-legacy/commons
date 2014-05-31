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
package com.codenvy.inject;

/**
 * DynaModule
 * Marker annotation for dynamically created module
 * CodenvyBootstrap automatically finds and loads Guice modules (subclasses of com.google.inject.Module)
 * annotated with &#064DynaModule
 *
 * @author gazarenkov
 */
public @interface DynaModule {
}
