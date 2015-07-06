package com.xidige.jfast.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {


	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsAllLetter() {
		assertEquals(true, StringUtil.isAlpha("addasdfa"));
		
		assertEquals(false, StringUtil.isAlpha("a da da"));		
		assertEquals(false, StringUtil.isAlpha(""));
		assertEquals(false, StringUtil.isAlpha("a-d"));
		assertEquals(false, StringUtil.isAlpha(null));
		assertEquals(false, StringUtil.isAlpha("我不是啊"));
	}

	@Test
	public void testCapitalize() {
		assertEquals("Cap",StringUtil.capitalize("cap"));
	}

	@Test
	public void testIsEmpty() {
		assertEquals(true,StringUtil.isEmpty(""));
		assertEquals(true,StringUtil.isEmpty(null));
		assertEquals(false,StringUtil.isEmpty("yes"));
	}

	@Test
	public void testSplitStringChar() {
		assertEquals(1, StringUtil.split("aaaa", ',').length);
		assertEquals(2, StringUtil.split(",", ',').length);
		assertEquals(2, StringUtil.split("a,", ',').length);
		assertEquals(3, StringUtil.split(",a,", ',').length);
		assertEquals(3, StringUtil.split(",,", ',').length);
		assertEquals(3, StringUtil.split(",ddddd,", ',').length);
	}
}
