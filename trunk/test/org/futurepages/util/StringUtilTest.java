package org.futurepages.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testReplace_stringNull() {
		replaceTestProcedure(null, "");
	}

	@Test
	public void testReplace_stringEmpty() {
		replaceTestProcedure("", "");
	}

	@Test
	public void testReplace_stringSemSubstituicoes() {
		replaceTestProcedure("abcdef", "abcdef");
	}

	@Test
	public void testReplace_stringComSubstituicoes() {
		replaceTestProcedure("a-c@ef", "a_c_ef");
	}

	@Test
	public void testReplace_stringCompletamenteSubstituida() {
		replaceTestProcedure("g -@", "G-__");
	}

	private void replaceTestProcedure(String in, String expected) {
		String result = StringUtils.replace(in, create());
		Assert.assertEquals(expected, result);
	}

	private Map<Character, String> create() {
		Map<Character, String> specials = new HashMap<Character, String>();
		specials.put('-', "_");
		specials.put(' ', "-");
		specials.put('@', "_");
		specials.put('g', "G");
		return specials;
	}
	
	@Test
	public void testConcatWith(){
		String result = StringUtils.concatWith(" - ","A","B","C");
		Assert.assertEquals("A strings não foram concatenadas corretamente", "A - B - C", result );
	}

	@Test
	public void testConcatWith_nenhumaString(){
		String result = StringUtils.concatWith(" - ");
		Assert.assertEquals("A strings não foram concatenadas corretamente","", result );
	}

}
