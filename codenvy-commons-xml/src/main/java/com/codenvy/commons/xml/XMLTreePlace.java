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

import java.util.LinkedList;

/**
 * @author Eugene Voevodin
 */
public class XMLTreePlace {

    public static XMLTreePlace after(String name) {
        return new XMLTreePlace(Place.AFTER, name);
    }

    public static XMLTreePlace before(String name) {
        return new XMLTreePlace(Place.BEFORE, name);
    }

    public static XMLTreePlace inTheEnd() {
        return new XMLTreePlace(Place.END, "");
    }

    public static XMLTreePlace inTheStart() {
        return new XMLTreePlace(Place.START, "");
    }

    private LinkedList<XMLTreePlace> places;
    private Place                    place;
    private String                   subject;

    private XMLTreePlace(Place place, String value) {
        this.place = place;
        this.subject = value;
    }

    public XMLTreePlace or(XMLTreePlace place) {
        places().add(place);
        return this;
    }

    void evalInsert(Element parent, NewElement newElement) {
        places().addFirst(this);
        for (XMLTreePlace place : places) {
            switch (place.place) {
                case AFTER:
                    if (parent.hasSingleChild(place.subject)) {
                        parent.getSingleChild(place.subject)
                              .insertAfter(newElement);
                        return;
                    }
                    break;
                case BEFORE:
                    if (parent.hasSingleChild(place.subject)) {
                        parent.getSingleChild(place.subject)
                              .insertBefore(newElement);
                        return;
                    }
                    break;
                case START:
                    final Element first = parent.getFirstChild();
                    if (first != null) {
                        first.insertBefore(newElement);
                    } else {
                        parent.appendChild(newElement);
                    }
                    return;
                case END:
                    parent.appendChild(newElement);
                    return;
            }
        }
        throw new XMLTreeException("It is not possible to insert element in specified place");
    }

    private LinkedList<XMLTreePlace> places() {
        return places == null ? places = new LinkedList<>() : places;
    }

    private enum Place {
        AFTER,
        BEFORE,
        START,
        END
    }
}
