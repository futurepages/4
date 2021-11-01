package org.futurepages.test;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.futurepages.core.config.Apps;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class DefaultTestLogger extends TestWatcher {

	@Override
	protected void starting( Description description ) {
		System.out.println( "\nTesting >>  "+description.getTestClass().getSimpleName()+ " "+ description.getMethodName() );
	}

	protected void succeeded(Description description) {
		if(Apps.devLocalMode()){
			logInputs(description);
		}
		System.out.println("[OK] Passed!");
	}

	@Override
	protected void failed(Throwable e, Description description) {
		System.out.println("[FAIL/ERROR] " + e.getMessage());
		makeScreenshotIfNecessary();
		logInputs(description);
	}

	private void makeScreenshotIfNecessary() {
		if(!DriverFactory.isDriverLoaded()){
			return;
		}
		WebDriver driver = DriverFactory.getDefaultWebDriver();
		String realDirPath; String realFilePath; String urlFile;
		if(!Is.empty(DriverFactory.SCREENSHOTS_REAL_PATH)){
			realDirPath = DriverFactory.SCREENSHOTS_REAL_PATH;
			String fileName = System.currentTimeMillis()+".png";
			realFilePath = The.concatWith(File.separator, DriverFactory.SCREENSHOTS_REAL_PATH, fileName);
			urlFile = The.concatWith("/", DriverFactory.SCREENSHOTS_URL_PATH, fileName);
		}else{
			realDirPath = The.concatWith(File.separator, System.getProperty("user.dir"), "out", "screenshots");
			realFilePath = The.concatWith(File.separator, realDirPath, System.currentTimeMillis()+".png");
			urlFile = "file://" +(File.separator.equals("\\")?"/":"") + realFilePath.replaceAll("\\\\", "/");
		}
		System.out.println("URL: "+driver.getCurrentUrl());
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("[SCREENSHOT] "+urlFile);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		try {
			//noinspection ResultOfMethodCallIgnored
			new File(realDirPath).mkdirs();
			FileOutputStream out = new FileOutputStream(realFilePath);
			out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			out.close();
			clearOldSnapshots(realDirPath);
		} catch (Exception e) {
			System.out.println("Erro ao gerar print da tela: "+realFilePath);
			e.printStackTrace();
		}
	}

}