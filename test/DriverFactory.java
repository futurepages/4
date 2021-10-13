package test;

import org.futurepages.drivers.CustomChromeDriver;
import org.futurepages.drivers.CustomFirefoxDriver;
import org.apache.commons.lang.SystemUtils;
import org.futurepages.core.config.Apps;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class DriverFactory {

	// ATENÇÃO: Todos os parâmetros deverão ser public String;
	public static String SO_DRIVER = ""; // SO onde está rodando (mac|windows|linux)
	public static String BROWSER_DRIVER = ""; // Driver do Chrome
	public static String VERSION_DRIVER = ""; // Versão do Driver do Chrome
	public static String APP_HOST = "";
	public static String IMPLICIT_WAIT = "";
	public static String TESTING_DELAY_MS = "";
	public static String TIMEOUT_WAIT_ELEMENT = "60";
	public static String RUN_TEST_IN_BACKGROUND = "true";
	public static String MAXIMIZE_ON_START = "false";
	public static String SCREENSHOTS_REAL_PATH = "";
	public static String SCREENSHOTS_URL_PATH = "";

	private static List<WebDriver> drivers = new ArrayList<>();

	public static long getTimeoutWaitElement() {
		return Long.valueOf(TIMEOUT_WAIT_ELEMENT);
	}

	public static String getPath(String path) {
		if(Is.empty(APP_HOST)){
			loadPropertiesFromFile();
		}
		return APP_HOST + path;
	}

	public static void reloadDefaultWebDriver() {
		driver.quit();
		drivers.remove(driver);
		driver = getNewWebDriver();
	}

	 private static void setImplicityWait(WebDriver driver) {
		if (!Is.empty(IMPLICIT_WAIT) && !IMPLICIT_WAIT.equals("0")) {
			driver.manage().timeouts().implicitlyWait(Long.parseLong(IMPLICIT_WAIT), TimeUnit.MILLISECONDS);
		}
	}

	enum Drivers{
		CHROME(CustomChromeDriver.class){
			@Override
			WebDriver newInstance() {
				if(RUN_TEST_IN_BACKGROUND.equals("true")){
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless");
					options.addArguments("--no-sandbox");
					options.addArguments("--disable-dev-shm-usage");
					return new CustomChromeDriver(options);
				}else{
					return new CustomChromeDriver();
				}
			}
		},
		GECKO(FirefoxDriver.class){
			@Override
			WebDriver newInstance() {
				if(RUN_TEST_IN_BACKGROUND.equals("true")){
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("--headless");
					return new CustomFirefoxDriver(options);
				}else{
					return new CustomFirefoxDriver();
				}
			}
		};

		private Class<? extends WebDriver> drvClass;
		Drivers(Class<? extends WebDriver> drvClss){
			this.drvClass = drvClss;
		}

		public static WebDriver newInstanceFor(String browserIdLower)  {
			setPathForDriver();
			try {
				WebDriver webDriver = valueOf(browserIdLower.toUpperCase()).newInstance();
				setImplicityWait(webDriver);
				if(MAXIMIZE_ON_START.equals("true")){
					webDriver.manage().window().maximize();
				}
				return webDriver;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}

		WebDriver newInstance() throws IllegalAccessException, InstantiationException {
			return getWebDriverClass().newInstance();
		}

		Class<? extends WebDriver> getWebDriverClass(){
			return this.drvClass;
		}
	}

	private static WebDriver driver;

	private static void setPathForDriver() {
		String DRIVER_PATH = null;
		try {
			DRIVER_PATH = FileUtil.classesPath(DriverFactory.class) + "/conf/drivers/"
					+ SO_DRIVER + "/" + BROWSER_DRIVER + "_" + VERSION_DRIVER
					+ (SO_DRIVER.equalsIgnoreCase("windows") ? ".exe" : "");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
//		# Definindo path dos drivers <N precisa se e eles estiverem no /etc/paths> #
		System.setProperty("webdriver."+BROWSER_DRIVER+".driver", DRIVER_PATH);
	}

	private static void loadPropertiesFromFile(){
		try {
			String os =              SystemUtils.IS_OS_WINDOWS? "windows"
									:SystemUtils.IS_OS_MAC_OSX? "mac"
									:SystemUtils.IS_OS_LINUX  ? "linux" : "?";
			System.out.println("IDENTIED OS: "+os);
			if(os.equals("?")){
				System.out.println(System.getProperty("os.name"));
			}
			SO_DRIVER = os;

			Properties props = new Properties();
			String configPath = FileUtil.classesPath(DriverFactory.class)+"conf/testing.properties";
			InputStream inputStream = new FileInputStream(configPath);
			props.load(inputStream);
			inputStream.close();
			Set<String> keys = props.stringPropertyNames();
			for (String key : keys) {
				System.out.println(key + " : " + props.getProperty(key));
				ReflectionUtil.setStaticField(DriverFactory.class,key, props.getProperty(key));
			}
			if(Is.empty(APP_HOST)){
				APP_HOST = Apps.get("APP_HOST");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static WebDriver getDefaultWebDriver() {
		if (driver == null) {
			loadPropertiesFromFile();
			driver = getNewWebDriver();
//			driver.manage().window().setSize(new Dimension(703, 710));
		}
		return driver;
	}

	public static boolean isDriverLoaded(){
		return driver!=null;
	}

	// antes era public quando tinhamos multiplos...
	private static WebDriver getNewWebDriver() {
		return addDriver(Drivers.newInstanceFor(BROWSER_DRIVER));
	}

	public static WebDriver addDriver(WebDriver driver){
		drivers.add(driver);
		return driver;
	}

	public static void quitDrivers() {
		driver = null;
		for(WebDriver driver : drivers){
			driver.quit();
		}
		drivers.clear();
	}

}
