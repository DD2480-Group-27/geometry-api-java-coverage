/*
 Copyright 1995-2017 Esri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 For additional information, contact:
 Environmental Systems Research Institute, Inc.
 Attn: Contracts Dept
 380 New York Street
 Redlands, California, USA 92373

 email: contracts@esri.com
 */


import org.junit.Test;

import java.beans.Transient;
import java.lang.annotation.Target;

import org.junit.Assert;
import junit.framework.TestCase;
import com.esri.core.geometry.StringUtils;

public class TestStringUtils extends TestCase {

    @Test
    public void testAppendDoubleNoPrecision() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(123.456, -1, sb);
        Assert.assertEquals("1e+02", sb.toString());
    }

    @Test
    public void testAppendDoubleMaxPrecision() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(123.456, 18, sb);
        Assert.assertEquals("123.456", sb.toString());
    }

    @Test
    public void testAppendDoubleWithPrecision() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(123.456, 2, sb);
        Assert.assertEquals("1.2e+02", sb.toString());
    }

    @Test
    public void testAppendDoubleWithTrailingZeros() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(123.4500, 6, sb);
        Assert.assertEquals("123.45", sb.toString());
    }

    @Test
    public void testAppendDoubleWithExponent() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(1.23456e10, 6, sb);
        Assert.assertEquals("1.23456e+10", sb.toString());
    }

    @Test
    public void testAppendDoubleNegativeValue() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(-123.456, 3, sb);
        Assert.assertEquals("-123", sb.toString());
    }

    @Test
    public void testAppendDoubleZeroValue() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendDouble(0.0, 3, sb);
        Assert.assertEquals("0", sb.toString());
    }
}
 