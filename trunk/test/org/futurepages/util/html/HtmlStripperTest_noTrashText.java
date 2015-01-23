package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HtmlStripperTest_noTrashText {

	private String tag;
	private String expected;
	private String caso;

	public HtmlStripperTest_noTrashText(String tag, String expected, String caso) {
		this.tag = tag;
		this.expected = expected;
		this.caso = "Caso: "+caso;
	}

	@Test
	public void testTreated() {
		String result = RichTextTagReplacer.noStylesText(this.tag);
		Assert.assertEquals(caso, expected, result);
	}

	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
				{"<xml>  <abc> <a:bc/> \r\n erro <abc/>  </xml>","","0"},
				{"<XML>  <abc> <a:bc/> \r\n erro <abc/>  </xml>","","1"},
				
				{"<a class=kx class=xkp style=x align='right'>abc</a>#<ul></ul>#<p style=\"\"></p>def"
				,"<a align=\"right\">abc</a>#<ul></ul>#<p style=\"\"></p>def","2"},
				
				{"<a CLASS=kx class=xkp style=x align='right'>abc</a>#<UL></ul>#<p style=\"\"></p>def"
					,"<a align=\"right\">abc</a>#<UL></ul>#<p style=\"\"></p>def","3"},
				
				{"<a>#<xml>p:q/><a:c/></xml> abc </a>#<ul></ul><p style=\"\"></p>d<!-- comentário --><xml></xml>ef"
					,"<a># abc </a>#<ul></ul><p style=\"\"></p>def","4"},
					
				{"<xml type=\"text\">p:q/><a:c/></xml><ul></ul><p style=\"\"></p><!-- comentário --><xml></xml>"
				,"<ul></ul><p style=\"\"></p>","5"},
					
				{"<xml type=\"text\">p:q/><a:c/></xml><ul></ul><script type=\"text/javascript\">alert(},</script><p style=\"\"></p><!-- comentário --><xml></xml>"
				,"<ul></ul><p style=\"\"></p>","6"},
					
				{"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>"
					,"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>","7"},
				
				{"<a align='right'>'abc'</a>"
				,"<a align=\"right\">'abc'</a>","substituição de aspas em atributos."},	
			});
		return col;
	}

}