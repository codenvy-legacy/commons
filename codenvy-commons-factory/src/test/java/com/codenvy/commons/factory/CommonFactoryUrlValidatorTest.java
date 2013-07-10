/*
 * Copyright (C) 2013 eXo Platform SAS.
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
package com.codenvy.commons.factory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CommonFactoryUrlValidatorTest {
    private FactoryUrlValidator factoryUrlValidator;

    @BeforeMethod
    public void setUp() throws Exception {
        this.factoryUrlValidator = new CommonFactoryUrlValidator();
    }

    @Test
    public void shouldValidateGoodUrl() throws Exception {
        factoryUrlValidator.validate("http://codenvy.com/factory?v=123&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                                     enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty"));
    }

    @Test(dataProvider = "badUrlProvider", expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUrlIsIllegal(String url) throws Exception {
        factoryUrlValidator.validate(url);
    }

    @DataProvider(name = "badUrlProvider")
    public Object[][] factoryUrlProvider() throws UnsupportedEncodingException {
        return new Object[][]{{"http://codenvy.com/factory?vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// v par is missing
                              {"http://codenvy.com/factory?v=123&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// vcs par is missing
                              {"http://codenvy.com/factory?v=123&vcs=git&pname=eee&wname=ttt&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// idcommit par is missing
                              {"http://codenvy.com/factory?v=123&vcs=git&idcommit=1234567&wname=ttt&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// pname par is missing
                              {"http://codenvy.com/factory?v=123&vcs=git&idcommit=1234567&pname=eee&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// wname par is missing
                              {"http://codenvy.com/factory?v=123&vcs=git&idcommit=1234567&pname=eee&wname=ttt"}, // vcsurl par is missing
                              {"http://codenvy.com/factory?v=123&vcs=git&vcs=notagit&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// vcs par is duplicated
                              {"http://codenvy.com/factory?v=123&vcs=&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")}// vcs par has empty value
        };
    }

    static String enc(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }
}
