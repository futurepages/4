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

	public void switchToWindow(Integer window) {
		List<String> windowHandles = new ArrayList<>(driver().getWindowHandles());
		try {
			driver().switchTo().window(windowHandles.get((window != null ? window : 1) - 1));
		} catch (NoSuchElementException ex) {}
	}

	public void closeWindow() {
		driver().close();
	}

	public ArrayList<String> getAllWindowsId() {
		return new ArrayList<>(driver().getWindowHandles());
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

	/**
	 * Pick the last element of a selenium list and make a comparison between the text expected and the text of element.
	 * @param expectedTxt The text that expect.
	 * @param elementsSelector The selector of element, example: .element, #element or xpath=//element.
	 */
	public void assertTextEqualsInLastElementForElements(String expectedTxt, String elementsSelector){
		assertTrue("'"+elementsSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE", isPresentAndVisible(elementsSelector));
		assertEquals(expectedTxt, getLast(elementsSelector).getText().trim()); // .replace(",", ".")
	}

	public void assertTextEqualsInElement(String expectedTxt, String elementSelector){
		assertTrue("'"+elementSelector+"' with TEXT=\""+expectedTxt+"\" NOT FOUND OR NOT VISIBLE", isPresentAndVisible(elementSelector));
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

	public void waitAndClickIfPresent(String selector) {
		try{
			// nao usar aqui o waitToClick
			waitForElement(selector);
			click(selector);
		}catch (Exception ignored){
		}
	}

	public void waitAndClickIfPresent(int seconds, String selector) {
		try{
			// nao usar aqui o waitToClick
			stayWaiting(seconds).until(ExpectedConditions.presenceOfElementLocated(bySelector(selector)));
			click(selector);
		}catch (Exception ignored){
		}
	}

	public void waitToClick(By by) {
		stayWaiting().until(ExpectedConditions.elementToBeClickable(by));
		click(by);
	}

	public boolean has$(String selector) {
		return !size$(selector).equals(0);
	}

	public Long size$(String selector) {
		selector = selector.replaceAll("'", "\"");
		try {
			return (Long) executeJS("return $('"+selector+"').size()");
		} catch (Exception ignored) {
			return 0L;
		}
	}

	public void waitFor$(String selector){
		stayWaiting().until((ExpectedCondition<Boolean>) d -> has$(selector));
	}

	public void waitToClick$(String selector) {
		waitFor$(selector);
		click$(selector);
	}

	public void click$(String selector) {
		selector = selector.replaceAll("'", "\"");
		executeJS("$('"+selector+"').click()");
	}

	/**
	 * Click in all elements what is find in selenium
	 * @param selector The selector of element, example: .element, #element or xpath=//element.
	 * */
	public void clickInAllElementBy(String selector) {
		List<WebElement> elements = getAll(selector);
		for (WebElement element: elements) {
			element.click();
		}
	}

	public void clickAndHold(String selector) {
		clickAndHold(bySelector(selector));
	}

	public void clickAndHold(By by) {
		WebElement element = driver().findElement(by);
		getActions().clickAndHold(element);
	}

	public void clickHoldAndMoveToX(String selector, int offSet) {
		clickHoldAndMoveToX(bySelector(selector), offSet);
	}

	public void clickHoldAndMoveToX(By by, int offSet) {
		WebElement element = driver().findElement(by);
		getActions().clickAndHold(element);
		getActions().moveToElement(element, offSet, 0);
	}

	public void clickHoldAndMoveToY(String selector, int offSet) {
		clickHoldAndMoveToY(bySelector(selector), offSet);
	}

	public void clickHoldAndMoveToY(By by, int offSet) {
		WebElement element = driver().findElement(by);
		getActions().clickAndHold(element);
		getActions().moveToElement(element, 0, offSet);
	}

	public void moveToElementX_js(String selector) {
		moveToElementX_js(selector,0);
	}

	public void moveToElementX_js(String selector, int offset) {
		executeJS("window.scrollTo(arguments[0], 0)", driver().findElement(bySelector(selector)).getLocation().y+offset);
	}

	public void moveToElementY_js(String selector) {
		moveToElementY_js(selector,0);
	}

	public void moveToElementY_js(String selector, int offset) {
		executeJS("window.scrollTo(0, arguments[0])", driver().findElement(bySelector(selector)).getLocation().y+offset);
//      //OUTRA OPÇÃO:
//		executeJS("arguments[0].scrollIntoView(true)", get(selector));
//		if(offset!=0){
//			executeJS("window.scrollBy(0, arguments[0])", offset);
//		}
	}

	public boolean isPresentAndVisible(String selector) {
		WebElement element;
		try{
			element = get(selector);
			return element.isDisplayed();
		}catch (NoSuchElementException ex){
			return false;
		}
	}


	public WebElement getIfPresentAndVisible(String selector) {
		WebElement element;
		try{
			element = get(selector);
			if(element.isDisplayed()){
				return element;
			}
		}catch (NoSuchElementException ignored){
		}
		return null;
	}

	public void waitToBeClickable(String selector) {
		stayWaiting().until(ExpectedConditions.elementToBeClickable(bySelector(selector)));
	}

	public void waitVisibilityToClick(String selector) {
		waitForElementVisible(selector);
		waitToBeClickable(selector);
		click(selector);
	}

	public void waitAllToClick(String selector) {
		waitForElementVisible(selector);
		waitToBeClickable(selector);
		waitForStopJSChanges(5000, 500);
		click(selector);
	}

	public void waitEnabledToClick(String selector) {
		waitForEnabled(selector);
		click(selector);
	}

	public String waitToGetTextOf(String selector) {
		waitForElement(selector);
		return getText(selector);
	}

	public boolean hasText(String text, String selector){
	    try {
			String bodyText = driver().findElement(bySelector(selector)).getText();
			return bodyText.contains(text);
		} catch (Exception e){
	    	return false;
		}
	}

	public boolean hasText(String text){
		return hasText(text, "body");
	}

	public void waitForText(String text){
		stayWaiting().until((ExpectedCondition<Boolean>) d -> hasText(text));
	}

	public String getAttr(String selector, String attr){
		return driver().findElement(bySelector(selector)).getAttribute(attr);
	}

	public String getHref(String selector){
		return getAttr(selector, "href");
	}

	private boolean isPageLoadComplete() {
		try {
			return executeJS("return document.readyState").equals("complete");
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
	}

	public void waitForPageLoad(){
		stayWaiting().until((ExpectedCondition<Boolean>) d -> isPageLoadComplete());
	}

	private boolean isAjaxLoadComplete() {
		try {
			return executeJS("return jQuery.active").equals(0L);
		} catch (Exception ignored) {
			return true;
		}
	}

	public void waitForAjaxLoad(){
		stayWaiting().until((ExpectedCondition<Boolean>) d -> isAjaxLoadComplete());
	}

	public void waitForStopJSChanges(int maxWaitMillis, int pollDelimiter) {
		double startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime + maxWaitMillis) {
			String prevState = driver().getPageSource();
			The.sleepOf(pollDelimiter);
			if (prevState.equals(driver().getPageSource())) {
				return;
			}
		}
	}

	private String getResPath(TestingView tv, String imagePath)  {
		try {
			String filePath = FileUtil.classesPath(tv.getClass()) + "res/"+ imagePath;
			return DriverFactory.SO_DRIVER.equals("windows")?filePath.substring(1):filePath;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}


	public boolean ajaxUploadImageFromRes(String ajaxUploadFileContainerId, String imagePath) {
		String xpath = "xpath=//input[@id='uploadImage_"+ajaxUploadFileContainerId+"']//..//input[@class='input_file']";
		return ajaxUploadFileFromRes(xpath, imagePath);
	}

	public boolean ajaxUploadFileFromRes(String ajaxUploadSelector, String filePath) {
		WebElement element = get(ajaxUploadSelector);
		try {
			element.sendKeys(getResPath(this, filePath));
			return true;
		} catch (Exception ex) {
			getActions().moveToElement(element).sendKeys(filePath);
			return true;
		}

	}

	public URL getImgSrcAsURL(String imgSelector) {
		try {
			return new URL(get(imgSelector).getAttribute("src"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public URL getAHrefAsURL(String imgSelector) {
		try {
			return new URL(get(imgSelector).getAttribute("href"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getImgBytes(String imgSelector) {
		WebElement element = get(imgSelector);
		try {
			return FileUtil.getBytesFromUrl(element.getAttribute("src"),null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getBytesFromResImg(String filePath) {
		try {
			return FileUtil.getBytesFromUrl((new File(getResPath(this,filePath))).toURI().toURL().toString(),null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void waitForInvisibilityOf(String selector) {
		stayWaiting().until(ExpectedConditions.invisibilityOf(get(selector)));
	}

	public String so() {
		return DriverFactory.SO_DRIVER;
	}

	public void quit() {
		driver().quit();
	}

	public void callAndWait() {
		call();
		waitToLoadAll();
	}

	public void waitToLoadAll() {
		final long MIN_LOAD_TIME = 1000;
		long start = System.currentTimeMillis();

		waitForPageLoad();
		waitForStopJSChanges(5000,500);
		waitForAjaxLoad();

		long end = System.currentTimeMillis();
		long diff = end - start;

		// espera mínima ...
		if(diff<MIN_LOAD_TIME){
			The.sleepOf(MIN_LOAD_TIME-diff);
		}
	}

	public <Page extends TestingView>  Page waitToLoadAll(Class<Page> pageOfTemplateClass) {
		try {
			Page pageOfTemplate = pageOfTemplateClass.newInstance();
			pageOfTemplate.waitToLoadAll();
			return pageOfTemplate;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T extends TestingView> T callAndWait(Class<T> pageClass) {
		try {
			T page = pageClass.newInstance();
			page.callAndWait();
			return page;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// recarrega e joga pro topo.
	public void reloadPage() {
		callURLAndWait(driver().getCurrentUrl());
		executeJS("window.scrollTo(0, 0)");
	}

	// atenção, pode não subir a página para o topo.
	public void refreshPage() {
		driver().navigate().refresh();
	}

	public void reloadPageAndWait() {
		callURLAndWait(driver().getCurrentUrl());
	}

	public String getCurrentUrl() {
		return driver().getCurrentUrl();
	}

	// demora muito fechar e abrir o browser. Só se o teste for realmente necessário o fechamento.
	// Na maioria das vezes, o reloadSession() resolverá sua vida.
	@Deprecated
	public void reloadBrowser() {
		DriverFactory.reloadDefaultWebDriver();
	}

	public void reloadSession() {
		driver().manage().deleteAllCookies();
	}

	public void confirmAlert(boolean accept){
		WebDriver driver = driver();
		Alert alert = driver.switchTo().alert();
		if(accept){
			alert.accept();
		}else{
			alert.dismiss();
		}
		driver.switchTo().defaultContent();
	}

	public void writeIfPresent(String selector, String msg) {
		try {
			write(selector, msg);
		} catch (Exception ignored) {
		}
	}

	public void scrollToPageBottom() {
		WebDriver driver = driver();
		String prevState;
		do {
			prevState = driver().getPageSource();
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
			waitToLoadAll();
			//waitForStopJSChanges(1000,500);
		}while (!prevState.equals(driver().getPageSource()));
	}
}
