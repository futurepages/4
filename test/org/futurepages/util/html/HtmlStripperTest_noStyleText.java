package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HtmlStripperTest_noStyleText {

	private String tag;
	private String expected;
	private String caso;
	
	public HtmlStripperTest_noStyleText(String expected, String tag) {
		super();
		this.tag = tag;
		this.expected = expected;
		this.caso = "";
	}
	
	@Test
	public void testTreated() {
		String result = RichTextTagReplacer.noStylesText(this.tag);
		Assert.assertEquals(caso, expected, result);
	}

	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
				{"<a>", "<a class=k>"},
				{"<a>", "<a class=kx>"},
				{"<a>", "<a class=kxy>"},
				{"<a>", "<a class=kxy style=kgb>"},
				{"<a>", "<a style=kxy class=kgb style=may>"},
				{"<a>", "<a style=kxy class=kgb style=may>"},
				{"<a>", "<a style=kxy class=kgb style=may>"},
				{"<a>", "<a style=kxy class=kgb style=may>"},
				{"<a>", "<a style=kxy class=kgb style=may>"},
				{"<a>", "<a class=kxy style=xpto>"},
				{"<a>", "<a class=\"k\">"},
				{"<a>", "<a style=\"k\"class='topic'>"},
				{"<a>", "<a class='k'>"},
				{"<a>", "<a style='kx'>"},
				{"<a>", "<a style='kxy' class=\"mxp\">"},
				{"<a>", "<a style=\n\"kx\">"},
				{"<a>", "<a style=\t\"kx\">"},
				{"<a>", "<a style\n=\r\n\t\"kx\">"},
				{"<a>", "<a style=\"kx\">"},
				{"<a>", "<a style=\"kxy\">"},
				{"<a>", "<a style=k class=z>"},
				{"<a>", "<a class=z style=k>"},
				{"<a>", "<a style='k' class='z'>"},
				{"<a>", "<a style=\"k\" class=\"z\">"},
				{"<a>", "<a style=\"k\" class=\"k1 k2 k3\">"},
				{"<a >", "<a class=k ><style type=\"text/css\">.td{}</style>"},
				{"<a >", "<a class=kx >"},
				{"<a >", "<!-- coment --><a class=kxy >"},
				{"<a >", "<a class=kxy  style=kgb>"},
				{"<a >", "<xml class=kx></xml><a style=kxy class=kgb  style=may>"},
				{"<a >", "<a  class=kxy style=xpto>"},
				{"<a >", "<a  class = \"k\">"},
				{"<a >", "<a style=\"k\"class='topic' >"},
				{"<a >", "<a class = 'k' >"},
				{"<a >", "<a style = 'kx' >"},
				{"<a >", "<a style = 'kxy' class  =  \"sdfsdf sdf sd3f sdf sd3f sd_f sdfsdf fmxp\" >"},
				{"<a >", "<a style \r=\n 'kxy' class \n\r\t =  \"sdfsdf sdf sd3f sdf sd3f sd_f sdfsdf fmxp\" >"},
				{"<a >", "<a style=\"kx\" >"},
				{"<a >", "<a style    = \"kxy\" >"},
				{"<a >", "<a  class=z style=k>"},
				{"<a >", "<a style='k' class= 'z' >"},
				{"<a >", "<a style=\"k\" class=\"z\" >"},
				{"<a >", "<a style = \"k\"  style = \"k\" class=\"k1 k2 k3\">"}	,
				
				{"<a href=\"http://www.class/\">\\n<u>clique\\n</u> no <em>link</em> do \\n <strong>site\\n</strong>\\n</a>\\n",
				"<a style='text-decoration:underline' href=\"http://www.class/\" class=xpto>\\n<span style='text-decoration: underline;'>clique\\n</span> no <em>link</em> do \\n <span style=\"font-weight:bold;\">site\\n</span>\\n</a>\\n"},
				
				{"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>",
					"<a style='text-decoration:underline' href=\"http://www.class/\" class=xpto><span style='text-decoration: underline;'>clique</span> no <em>link</em> do <span style=\"font-weight:bold;\">site</span></a>"},
				
				{"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>",
					"<a href=\"http://www.class/\" class=xpto style='text-decoration: underline;'><span style=\"text-decoration:underline;\">clique</span> no <em>link</em> do <span style=\"font-weight:bold;text-align:right\">site</span></a>"}
			
		});
		return col;
	}

}