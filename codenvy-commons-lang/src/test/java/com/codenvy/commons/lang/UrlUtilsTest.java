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
package com.codenvy.commons.lang;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class UrlUtilsTest {
    @Test(dataProvider = "badUrlProvider", expectedExceptions = MalformedURLException.class)
    public void shouldThrowMalformedUrlExceptionIfUrlIsIllegal(String url) throws Exception {
        UrlUtils.getQueryParameters(url);
    }

    @DataProvider(name = "badUrlProvider")
    public Object[][] factoryUrlProvider() {
        return new Object[][]{{""},
                              {"?"},
                              {"www.localhost:8080/?ds"},
                              {"localhost:8080/?ds"},
                              {"www.cloud-ide.int/?ds"}
        };
    }

    @Test
    public void shouldExtractParametersWithoutValue() throws Exception {
        Map<String, List<String>> params = UrlUtils.getQueryParameters("http://codenvy.com/factory?v");
        assertTrue(params.containsKey("v"));
        assertNull(params.get("v").iterator().next());
    }

    @Test
    public void shouldExtractParametersWithMultipleValues() throws Exception {
        Map<String, List<String>> expectedParams = new HashMap<>();
        List<String> v = new LinkedList<>();
        v.add("123");
        v.add("qwe");
        v.add("www");
        expectedParams.put("v", v);

        Map<String, List<String>> params = UrlUtils.getQueryParameters("http://codenvy.com/factory?v=123&v=qwe&v=www");

        assertEquals(params, expectedParams);
    }

    @Test
    public void shouldExtractParametersWithMultipleValuesDividedAnotherParameters() throws Exception {
        Map<String, List<String>> expectedParams = new HashMap<>();
        List<String> v = new LinkedList<>();
        v.add("123");
        v.add("qwe");
        v.add("www");
        List<String> par = new LinkedList<>();
        par.add("test");
        expectedParams.put("v", v);
        expectedParams.put("par", par);

        Map<String, List<String>> params = UrlUtils.getQueryParameters("http://codenvy.com/factory?v=123&par=test&v=qwe&v=www");

        assertEquals(params, expectedParams);
    }

    @Test
    public void shouldIgnoreSlashAtTheEndOfPath() throws Exception {
        Map<String, List<String>> expectedParams = new HashMap<>();
        List<String> v = new LinkedList<>();
        v.add("123");
        v.add("qwe");
        v.add("www");
        List<String> par = new LinkedList<>();
        par.add("test");
        expectedParams.put("v", v);
        expectedParams.put("par", par);

        Map<String, List<String>> params = UrlUtils.getQueryParameters("http://codenvy.com/factory/?v=123&par=test&v=qwe&v=www");

        assertEquals(params, expectedParams);
    }

    @Test
    public void shouldExtractEncodedParameters() throws Exception {
        Map<String, List<String>> expectedParams = new HashMap<>();
        List<String> vcsurl = new LinkedList<>();
        vcsurl.add("http://github/some/path?somequery=qwe&somequery=sss&somequery=rty");
        expectedParams.put("vcsurl", vcsurl);


        Map<String, List<String>> params = UrlUtils.getQueryParameters("http://codenvy.com/factory?vcsurl=" + URLEncoder.encode(
                "http://github/some/path?somequery=qwe&somequery=sss&somequery=rty", "UTF-8"));

        assertEquals(params, expectedParams);
    }
}
