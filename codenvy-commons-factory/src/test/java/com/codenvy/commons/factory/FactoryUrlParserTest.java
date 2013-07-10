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

import org.testng.annotations.Test;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;

public class FactoryUrlParserTest {
    @Test
    public void shouldValidateGoodUrl() throws Exception {
        File testRepository = Files.createTempDirectory("testrepository").toFile();
        ZipUtils.unzip(new File(Thread.currentThread().getContextClassLoader().getResource("testrepository.zip").toURI()), testRepository);

        FactoryUrlParser.parse("http://codenvy.com/factory?v=123&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" + URLEncoder
                .encode("file://" + testRepository + "/testrepository", "UTF-8"));
    }

    @Test(expectedExceptions = FactoryUrlException.class)
    public void shouldNotValidateBadUrl() throws Exception {
        FactoryUrlParser.parse("http://codenvy.com/factory?v=123&vcs=git&idcommit=1234567&pname=eee&wname=ttt&vcsurl=" + URLEncoder
                .encode("file:///tmp/123123123/testrepository", "UTF-8"));
    }
}
