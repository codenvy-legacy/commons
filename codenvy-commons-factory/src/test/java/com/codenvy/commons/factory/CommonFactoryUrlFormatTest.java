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

import com.codenvy.commons.lang.ZipUtils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;

import static org.testng.Assert.assertEquals;

public class CommonFactoryUrlFormatTest {
    private FactoryUrlFormat factoryUrlFormat;

    @BeforeMethod
    public void setUp() throws Exception {
        this.factoryUrlFormat = new CommonFactoryUrlFormat();
    }

    @Test
    public void shouldParseGoodUrl() throws Exception {
        //given
        File testRepository = Files.createTempDirectory("testrepository").toFile();
        ZipUtils.unzip(new File(Thread.currentThread().getContextClassLoader().getResource("testrepository.zip").toURI()), testRepository);

        FactoryUrl expectedFactoryUrl =
                new FactoryUrl("1.0", "git", "file://" + testRepository + "/testrepository", "1234567", "eee", "ttt");

        //when
        FactoryUrl factoryUrl = factoryUrlFormat.parse("http://codenvy.com/factory?v=1.0&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" + enc(
                "file://" + testRepository + "/testrepository"));

        //then
        assertEquals(factoryUrl, expectedFactoryUrl);
    }

    @Test(dataProvider = "badUrlProvider-InvalidFormat", expectedExceptions = FactoryUrlInvalidFormatException.class)
    public void shouldThrowFactoryUrlIllegalFormatExceptionIfUrlParametersIsMissing(String url) throws Exception {
        factoryUrlFormat.parse(url);
    }

    @DataProvider(name = "badUrlProvider-InvalidFormat")
    public Object[][] missingParametersFactoryUrlProvider() throws UnsupportedEncodingException {
        return new Object[][]{{"http://codenvy.com/factory?v=1.0&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// vcs par is missing
                              {"http://codenvy.com/factory?v=1.0&vcs=git&pname=eee&wname=ttt&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// idcommit par is missing
                              {"http://codenvy.com/factory?v=1.0&vcs=git&idcommit=1234567&wname=ttt&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// pname par is missing
                              {"http://codenvy.com/factory?v=1.0&vcs=git&idcommit=1234567&pname=eee&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// wname par is missing
                              {"http://codenvy.com/factory?v=1.0&vcs=git&idcommit=1234567&pname=eee&wname=ttt"}, // vcsurl par is missing
                              // there is no format to satisfy that version
                              {"http://codenvy.com/factory?v=2.0&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},
        };
    }

    @Test(dataProvider = "badUrlProvider-InvalidArgument", expectedExceptions = FactoryUrlInvalidArgumentException.class)
    public void shouldThrowFactoryUrlInvalidArgumentExceptionIfUrlHasInvalidParameters(String url) throws Exception {
        factoryUrlFormat.parse(url);
    }

    @DataProvider(name = "badUrlProvider-InvalidArgument")
    public Object[][] invalidParametersFactoryUrlProvider() throws UnsupportedEncodingException {
        return new Object[][]{{"http://codenvy.com/factory?vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// v par is missing
                              {"http://codenvy.com/factory?v=1.0&v=2.0&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// v par has is fuplicated
                              {"http://codenvy.com/factory?v=1.0&vcs=git&vcs=notagit&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},// vcs par is duplicated
                              {"http://codenvy.com/factory?v=1.0&vcs=&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" + enc(
                                      "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")}// vcs par has empty value
        };
    }

    static String enc(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }
}
