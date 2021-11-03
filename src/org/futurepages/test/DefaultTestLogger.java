package org.futurepages.test;

import org.futurepages.core.config.Apps;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
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

	private void clearOldSnapshots(String dir) {
		File tempFolder = new File(dir);
		List<File> files = FileUtil.listFilesFromDirectory(tempFolder, false);
		long tempoDeInicio = System.currentTimeMillis();
		int cont= 0;
		if (!files.isEmpty()) {
			for (File f : files) {
				if (CalendarUtil.getDifferenceInDays(tempoDeInicio, f.lastModified()) >= 1) {
					f.delete();
					cont++;
				}
			}
			System.out.println(">>> deleted old snapshots: " + cont);
		}
	}

	private void logInputs(Description description) {
		Field[] declaredFields = description.getTestClass().getDeclaredFields();
		if(declaredFields.length>0){
			System.out.println("STATICALLY DECLARED INPUTS.....................\n");
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					try {
						System.out.println(field.getName()+": "+(field.get(null)!=null? The.contentOf(field.get(null)) :"<null>"));
					} catch (IllegalAccessException ignore) {
					}
				}
			}
			System.out.println("\n...............................................");
		}
	}

	@Override
	protected void finished(Description description) {
		System.out.println();
	}

}