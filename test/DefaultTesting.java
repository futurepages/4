package org.futurepages.test;

import framework.util.AssertUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;


public class DefaultTesting extends AssertUtils {

	@Rule
	public DefaultTestLogger logger = new DefaultTestLogger();
	@Rule
	public ConditionalIgnoreRule conditional = new ConditionalIgnoreRule();

	@BeforeClass
	public static void _beforeClass(){
		if(DriverFactory.isDriverLoaded()){
			DriverFactory.getDefaultWebDriver().manage().deleteAllCookies();
		}
	}

	@AfterClass
	public static void _afterClass(){
		if(TestingContext.getInstance()==null){
			TestingContext.close();
		}
	}
}