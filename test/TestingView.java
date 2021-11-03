package org.futurepages.test;

import org.futurepages.test.util.AssertUtils;
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

	public WebElement get(String selector) {
		return driver().findElement((bySelector(selector)));
	}

	public List<WebElement> getAll(String selector) {
		return driver().findElements((bySelector(selector)));
	}

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

	public void callENTER() {
		driver().switchTo().activeElement().sendKeys(Keys.ENTER);
	}

	public void write(By by, Object txt) {
		driver().findElement(by).clear();
		driver().findElement(by).sendKeys(txt.toString());
	}

	public void write(String selector, Object msg) {
		write(bySelector(selector), msg);
	}

	public void write(WebElement element, Object txt) {
		element.clear();
		element.sendKeys(txt.toString());
	}

	public String getText(By by) {
		return driver().findElement(by).getText();
	}

	public String getText(String selector) {
		return getText(bySelector(selector));
	}

	public String getVal(By by) {
		return driver().findElement(by).getAttribute("value");
	}

	public String getVal(String selector) {
		return getVal(bySelector(selector));
	}

	public void click(String selector) {
		click(bySelector(selector));
	}

	public String getAttributeOfOptionSelected(String selector, String attr) {
		Select select = new Select(get("select" + selector));
		WebElement option = select.getFirstSelectedOption();
		return option.getAttribute(attr);
	}

	public Actions getActions() {
		return new Actions(driver());
	}

	public void click(By by) {
		WebElement element = driver().findElement(by);
		try{
			element.click();
		}catch (Exception ex){
			getActions().moveToElement(element).click().perform();
		}
	}

	public boolean isSelected(By by) {
		return driver().findElement(by).isSelected();
	}

	public boolean isSelected(String selector) {
		return isSelected(bySelector(selector));
	}

	public void selectText(By by, Object txt) {
		WebElement element = driver().findElement(by);
		Select combo = new Select(element);
		combo.selectByVisibleText(txt.toString());
	}

	public void selectIndex(String selector, int idx) {
		Select combo = new Select(driver().findElement(bySelector(selector)));
		combo.selectByIndex(idx);
	}

	public void selectText(String selector, Object value) {
		selectText(bySelector(selector), value);
	}

	public void deselectText(By by, String valor) {
		WebElement element = driver().findElement(by);
		Select combo = new Select(element);
		combo.deselectByVisibleText(valor);
	}

	public void deselectText(String selector, String value) {
		deselectText(bySelector(selector), value);
	}

	public String getSelectVal(By by) {
		WebElement element = driver().findElement(by);
		Select combo = new Select(element);
		return combo.getFirstSelectedOption().getText();
	}

	public String getSelectVal(String selector) {
		return getSelectVal(bySelector(selector));
	}

	public List<String> selectList(By by) {
		WebElement element = driver().findElement(by);
		Select combo = new Select(element);
		List<WebElement> allSelectedOptions = combo.getAllSelectedOptions();
		List<String> valores = new ArrayList<String>();
		for (WebElement option :
				allSelectedOptions) {
			valores.add(option.getText());
		}
		return valores;
	}

	public List<String> selectList(String selector) {
		return selectList(bySelector(selector));
	}

	public int selectCountOptions(By by) {
		WebElement element = driver().findElement(by);
		Select combo = new Select(element);
		List<WebElement> options = combo.getOptions();
		return options.size();
	}

	public int selectCountOptions(String selector) {
		return selectCountOptions(bySelector(selector));
	}

	public boolean verifySelectOption(By by, String optionToVerify) {
		WebElement element = driver().findElement(by);
		Select combo = new Select(element);
		List<WebElement> allSelectedOptions = combo.getAllSelectedOptions();
		for (WebElement option :
				allSelectedOptions) {
			if (option.getText().equals(optionToVerify)) {
				return true;
			}
		}
		return false;
	}

	public boolean verifySelectOption(String selector, String optionToVerify) {
		return verifySelectOption(bySelector(selector), optionToVerify);
	}

	private By bySelector(String selector) {
		return selector.startsWith("xpath=")? By.xpath(selector.substring(6)) : By.cssSelector(selector);
	}

	public void alertAccept() {
		try{
			Alert alert = driver().switchTo().alert();
			alert.accept();
		}catch (Exception ignored){}
	}

	public void alertDismiss() {
		try{
			Alert alert = driver().switchTo().alert();
			alert.dismiss();
		}catch (Exception ignored){}
	}

	public void setAlertTextAndAccept(String valor) {
		Alert alert = driver().switchTo().alert();
		alert.sendKeys(valor);
		alert.accept();
	}

	public void enterFrame(String id) {
		driver().switchTo().frame(id);
	}

	public void exitFrame() {
		driver().switchTo().defaultContent();
	}

	public void switchToWindow(String id) {
		driver().switchTo().window(id);
	}

	public void waitSeconds(int seconds) {
		The.sleepOf(seconds*1000);
	}

	public Object executeJS(String cmd, Object... params) {
		// Exemplo:
		// executeJS("window.scrollBy(0, arguments[0])", getElement("concluirBtn").getLocation().y);
		JavascriptExecutor js = (JavascriptExecutor) driver();
		return js.executeScript(cmd, params);
	}

	public WebDriverWait stayWaiting(){
		return new WebDriverWait(driver(), DriverFactory.getTimeoutWaitElement());
	}

	public WebDriverWait stayWaiting(long seconds){
		return new WebDriverWait(driver(), seconds);
	}

	public void waitForElement(String selector) {
		waitForElement(bySelector(selector));
	}

	public void waitForElement(By by) {
		stayWaiting().until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public void waitForElementNotEmptyText(By by) {
		stayWaiting().until((ExpectedCondition<Boolean>) d -> !Is.empty(driver().findElement(by).getAttribute("value")));
	}

	public void waitForEnabled(String selector) {
		waitForEnabled(bySelector(selector));
	}

	public void waitForEnabled(By by) {
		stayWaiting().until((ExpectedCondition<Boolean>) d -> !Is.empty(driver().findElement(by).isEnabled()));
	}

	public void waitForElementNotEmptyText(String selector) {
		waitForElementNotEmptyText(bySelector(selector));
	}

	public void waitForSelect(By by, String value) {
		Select select = new Select(driver().findElement(by));
		stayWaiting().until((ExpectedCondition<Boolean>) d -> select.getFirstSelectedOption().getText().equalsIgnoreCase(value));
	}

	public void waitForSelect(String selector, String value) {
		waitForSelect(bySelector(selector), value);
	}

	public void waitForElementVisible(By by) {
		stayWaiting().until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public void waitForElementVisible(String selector) {
		waitForElementVisible(bySelector(selector));
	}

	public void waitMs(long timeMillis) {
		The.sleepOf(timeMillis);
	}

	public void assertTextEqualsInElement(String expectedTxt, String elementSelector){
		assertTrue("'"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		assertEquals(expectedTxt, get(elementSelector).getText().trim());
	}

	public void assertTextEqualsInElement(String msg, String expectedTxt, String elementSelector){
		assertTrue(msg+": '"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		assertEquals(msg, expectedTxt, get(elementSelector).getText().trim());
	}


	public void assertTextNotEqualsInElement(String msg, String expectedTxt, String elementSelector){
		assertTrue(msg+": '"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		assertNotEquals(msg, expectedTxt, get(elementSelector).getText().trim());
	}

	public void assertPresentAndVisible(String elementSelector){
		assertPresentAndVisible(null, elementSelector);
	}

	public void assertPresentAndVisible(String msg, String elementSelector){
		assertTrue(msg==null?"'"+elementSelector+"' NOT FOUND OR NOT VISIBLE!": elementSelector+": "+msg, isPresentAndVisible(elementSelector));
	}

	public void assertNotExistsOrHidden(String elementSelector){
		assertNotExistsOrHidden(null, elementSelector);
	}

	public void assertNotExistsOrHidden(String msg, String elementSelector){
		WebElement element = getIfPresentAndVisible(elementSelector);
		String failMsg = null;
		if(element!=null){
			failMsg = "'"+elementSelector+"', class='"+element.getAttribute("class")+"', text=\""+element.getText().trim()+"\" FOUND AND VISIBLE!"+(msg!=null?": "+msg:"");
		}
		assertTrue(failMsg, element==null);
	}

	public void assertHasTextInElement(String expectedTxt, String elementSelector){
		assertTrue("'"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		String textoDoElemento = get(elementSelector).getText();
		assertThat(textoDoElemento,containsString(expectedTxt));
	}

	public void assertPageHasText(String expectedTxt){
		String textoDoElemento = get("body").getText();
		assertThat(textoDoElemento,containsString(expectedTxt));
	}

	public void assertHasTextInElement(String msg, String expectedTxt, String elementSelector){
		assertTrue(msg+": '"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		String textoDoElemento = get(elementSelector).getText();
		assertThat(textoDoElemento,containsString(expectedTxt));
	}

	public void assertNotHasTextInElement(String expectedTxt, String elementSelector){
		assertTrue("'"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		String textoDoElemento = get(elementSelector).getText();
		assertThat(textoDoElemento,not(containsString(expectedTxt)));
	}

	public void assertPatternInElement(String regex, String elementSelector){
		assertTrue("'"+elementSelector+"' with REGEX=\""+regex+"\" NOT FOUND OR NOT VISIBLE",isPresentAndVisible(elementSelector));
		String textoDoElemento = get(elementSelector).getText();
		assertThat(textoDoElemento,matchesPattern(regex));
	}

	public void assertCurrentPathEquals(String path) {
		String currentPath = getCurrentUrl().replace( Apps.get("APP_HOST"), "").split("[?#]")[0];
		assertTrue("Current path ("+currentPath+") does not match expected ("+path+")", currentPath.equals(path));
	}

	public void waitToClick(String selector) {
		stayWaiting().until(ExpectedConditions.elementToBeClickable(bySelector(selector)));
		click(selector);
	}

	public void waitToWrite(String selector, String msg) {
		stayWaiting().until(ExpectedConditions.elementToBeClickable(bySelector(selector)));
		write(selector, msg);
	}
}
