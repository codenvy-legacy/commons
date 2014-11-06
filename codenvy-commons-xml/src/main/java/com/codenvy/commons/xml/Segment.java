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
package com.codenvy.commons.xml;

/**
 * @author Eugene Voevodin
 */
final class Segment {
    int left;
    int right;

    Segment(int left, int right) {
        this.left = left;
        this.right = right;
    }

    void shift(int offset) {
        left += offset;
        right += offset;
    }
}
