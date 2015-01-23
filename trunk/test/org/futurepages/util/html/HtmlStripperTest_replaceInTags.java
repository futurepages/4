package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HtmlStripperTest_replaceInTags {

	private String original;
	private String expected;
	private String caso;
	private String regex;
	private String replacement;
	
	public HtmlStripperTest_replaceInTags(String original, String regex, String replacement, String expected) {
		super();
		this.original = original;
		this.expected = expected;
		this.regex = regex;
		this.caso = "";
		this.replacement = replacement;
	}
	
	@Test
	public void testTreated() {
		String result = RichTextTagReplacer.replaceInTags(original,regex,replacement);
		Assert.assertEquals(caso, expected, result);
	}

	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
				{"#<a class='kxp'>#</a>#", "'", "\"","#<a class=\"kxp\">#</a>#"},
				{"<a class=\"cidadao brasileiro\" class=\"kxp\"></a>", "class=\".*?\\b\"", "","<a  ></a>"},
				{"<a a='xy' b='yz' c='xz'><b a='xy' b='yz' c='xz'><c a='xy' b='yz' c='xz'>", "'.*?'", "'#'","<a a='#' b='#' c='#'><b a='#' b='#' c='#'><c a='#' b='#' c='#'>"}
		});
		return col;
	}

}