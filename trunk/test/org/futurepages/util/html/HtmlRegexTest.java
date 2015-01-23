package org.futurepages.util.html;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leandro
 */
public class HtmlRegexTest {

	@Test
	@Ignore
	public void testTagsPattern() {
		//0 tags
		assertEquals(HtmlRegex.tagsPattern(false), null);
		assertEquals(HtmlRegex.tagsPattern(true), "</?.*?>");
		//1 tag
		assertEquals(HtmlRegex.tagsPattern(true, "a"), "</?(a\\b).*?>");
		assertEquals(HtmlRegex.tagsPattern(false,"a"), "<(?!a\\b)(?!/a).*?>");
		//2 tags
		assertEquals(HtmlRegex.tagsPattern(true,"p","a"), "</?(p\\b|a\\b).*?>");
		assertEquals(HtmlRegex.tagsPattern(false,"p","a"), "<(?!p\\b)(?!/p)(?!a\\b)(?!/a).*?>");
		//3 tags
		assertEquals(HtmlRegex.tagsPattern(true,"p","em","strong"), "</?(p\\b|em\\b|strong\\b).*?>");
		assertEquals(HtmlRegex.tagsPattern(false,"p","em","strong"), "<(?!p\\b)(?!/p)(?!em\\b)(?!/em)(?!strong\\b)(?!/strong).*?>");

	}

}