package org.futurepages.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DefaultTestSuite {

	@BeforeClass
	public static void beforeClass(){
		TestingContext.init();
	}

	@AfterClass
	public static void afterClass(){
		TestingContext.finish();
	}
}
