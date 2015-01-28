package org.futurepages.util;

import org.futurepages.util.html.HtmlUtilTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    CNPJUtilTest.class,
    CPFUtilTest.class,
    BrazilianCalendarUtilTest.class,
    BrazilianCalendarUtil_literalRangeOfDatesTest.class,
    BrazilianCalendarUtil_literalRangeOfTimesTest.class,
    CollectionUtilTest.class,
    BrazilianDateUtilTest.class,
    FileUtilTest.class,
    HQLUtilTest.class,
    HtmlUtilTestSuite.class,
	IsTest.class,
    NumberUtilTest.class,
    ProbabilityUtilTest.class,
    SecurityTest.class,
    SEOUtilTest.class,
    StringUtilTest.class,
    TheTest.class
})
public class UtilTestSuite {
}