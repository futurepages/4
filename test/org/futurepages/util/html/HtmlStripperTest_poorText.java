package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HtmlStripperTest_poorText {

	private String tag;
	private String expected;
	private String caso;

	public HtmlStripperTest_poorText(String tag, String expected) {
		super();
		this.tag = tag;
		this.expected = expected;
		this.caso = "";
	}

	@Test
	public void testTreated() {
		String result = new HtmlStripper(tag).poorText();
		Assert.assertEquals(caso, expected, result);
	}

	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
				{"a<p style=\"text-align:center\">b<strong>c</strong>d</p>e"
					, "abcde"},
				{"a<p style=\"text-align:center\">b\n<strong>\nc</strong>d</p>e"
					, "ab\n\ncde"},
				{" a <p style=\"text-align:center\"> b <strong> c </strong> d </p> e "
					, " a  b  c  d  e "}
		});
		return col;
	}

}