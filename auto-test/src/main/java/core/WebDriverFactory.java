package core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;


public class WebDriverFactory {

	private final Map<String, WebDriverCreator> drivers = new HashMap<String, WebDriverCreator>();
	private static final Log log = LogFactory.getLog(WebDriverFactory.class);
	
	private final String CHROME_DRIVER_LOC = System.getProperty("user.dir") + "\\lib\\chromedriver\\" + "chromedriver.exe";
	
	public WebDriverFactory() {
		initializeWebDriverCreators();
	}

	private void initializeWebDriverCreators() {
		drivers.put("chrome", new ChromeWebDriverCreator());
		drivers.put("firefox", new FireFoxDriverCreator());
	}

	public WebDriver createDriver(String browserName) {
		WebDriver webdriver = null;
		try{
			log.info("Creating webdriver instance for browser "+browserName);
			webdriver = drivers.get(browserName).create();
		}catch(WebDriverException e){
			log.info(e.getMessage());
			log.info("could not create driver for browser "+browserName);
		}
						
		EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(webdriver);
		return eventFiringWebDriver;
	}


	private interface WebDriverCreator {
		WebDriver create();
	}

	private class ChromeWebDriverCreator implements WebDriverCreator {
		public WebDriver create() {
			System.setProperty("webdriver.chrome.driver",CHROME_DRIVER_LOC);
			return new ChromeDriver(getChromeProfile());
		}
	}

	private class FireFoxDriverCreator implements WebDriverCreator {
		public WebDriver create() {
			WebDriver fireFoxDriver = new FirefoxDriver(getFirefoxProfile());
			return fireFoxDriver;
		}
	}
	
	private FirefoxProfile getFirefoxProfile() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAssumeUntrustedCertificateIssuer(true);
		profile.setEnableNativeEvents(true);
		return profile;
	}
	
	private ChromeOptions getChromeProfile(){
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-translate");
		return options;
	}
}
