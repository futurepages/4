package org.futurepages;

import org.futurepages.core.persistence.HQLFieldTest;
import org.futurepages.core.persistence.HQLProviderTest;
import org.futurepages.enums.EnumTestSuite;
import org.futurepages.formatters.ElapsedTimeFormatterTest;
import org.futurepages.util.UtilTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	BeforeTestSuite.class,
	EnumTestSuite.class,
	ElapsedTimeFormatterTest.class,
	UtilTestSuite.class,
	HQLProviderTest.class,
	HQLFieldTest.class,
})
public class FuturePagesTestSuite {

}
