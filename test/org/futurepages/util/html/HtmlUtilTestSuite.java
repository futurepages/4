package org.futurepages.util.html;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	HtmlMapCharsTest.class,
	HtmlRegexTest.class,
	HtmlStripperTest.class,
	HtmlTagReplacerTest_treated.class,
	HtmlStripperTest_noStyleText.class,
	HtmlStripperTest_noTrashText.class,
	HtmlStripperTest_poorText.class,
	HtmlStripperTest_replaceInTags.class,
	ParagraphApplierTest.class
	
})
public class HtmlUtilTestSuite {}