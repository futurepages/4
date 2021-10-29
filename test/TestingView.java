package org.futurepages.test;

import framework.util.AssertUtils;
import org.futurepages.core.config.Apps;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class TestingView extends AssertUtils {

	public List<WebElement> getAll(String selector) {
		return driver().findElements((bySelector(selector)));
	}

	private String path(String path) {
		return DriverFactory.getPath(path);
	}

	public TestingView(){

	}

	@Deprecated //Utilize somente como quebra galho - interessanste abstraí-lo ao máximo aqui nesta classe.
	public WebDriver getDriver() {
		return driver();
	}

	private WebDriver driver(){
		return DriverFactory.getDefaultWebDriver();
	}

	protected void call(){}

	@Deprecated //Utilize somente como quebra galho - interessanste abstraí-lo ao máximo aqui nesta classe.
	public WebDriver getDriver() {
		return driver();
	}

	public WebElement getLast(String selector) {
		List<WebElement> result = driver().findElements((bySelector(selector)));
		if (result.size() > 0){
			return result.get(result.size() - 1) ;
		}
		throw new NoSuchElementException("Cannot locate an element using " + selector);
	}
	public boolean has(String selector) {
		try {
			get(selector);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void callPath(String path) {
		driver().get(path(path));
	}

	public void callPathAndWait(String path, int seconds) {
		callPath(path);
		waitToLoadAll();
	}

	public void callPathAndWait(String path) {
		callPathAndWait(path, 0);
	}

	public void callURL(String url) {
		driver().get(url);
	}

	public void callURLAndWait(String url, int seconds) {
		callURL(url);
		waitSeconds(seconds);
		waitToLoadAll();
	}

	public void callURLAndWait(String url) {
		callURLAndWait(url, 0);
	}

	public void callESC() {
		driver().switchTo().activeElement().sendKeys(Keys.ESCAPE);
	}
}
