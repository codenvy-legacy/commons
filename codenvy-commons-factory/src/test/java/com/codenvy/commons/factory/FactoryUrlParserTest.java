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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import static com.codenvy.commons.factory.CommonFactoryUrlFormatTest.enc;
import static org.testng.Assert.assertEquals;

public class FactoryUrlParserTest {
    @Test
    public void shouldReturnFactoryUrlObjectOnSuccessfulParsing() throws URISyntaxException, IOException, FactoryUrlException {
        //given
        File testRepository = Files.createTempDirectory("testrepository").toFile();
        ZipUtils.unzip(new File(Thread.currentThread().getContextClassLoader().getResource("testrepository.zip").toURI()), testRepository);

        FactoryUrl expectedFactoryUrl =
                new FactoryUrl("1.0", "git", "file://" + testRepository + "/testrepository", "1234567", "eee", "ttt");

        //when
        FactoryUrl factoryUrl = FactoryUrlParser.parse("http://codenvy.com/factory?v=1.0&vcs=git&idcommit=1234567&pname=eee&wname=ttt" +
                                                       "&vcsurl=" + enc("file://" + testRepository + "/testrepository"));

        //then
        assertEquals(factoryUrl, expectedFactoryUrl);
    }

    @Test(dataProvider = "unsupportedUrls", expectedExceptions = FactoryUrlInvalidFormatException.class)
    public void shouldThrowFactoryUrlInvalidFormatExceptionForUnsupportedUrlFormat(String factoryUrl) throws FactoryUrlException {
        FactoryUrlParser.parse(factoryUrl);
    }

    @DataProvider(name = "unsupportedUrls")
    public Object[][] unsupportedUrlsProvider() throws UnsupportedEncodingException {
        return new Object[][]{// there is no format to satisfy that version
                              {"http://codenvy.com/factory?v=2.0&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" +
                               enc("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty")},
        };
    }
}
