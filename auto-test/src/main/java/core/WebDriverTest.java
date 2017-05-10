package core;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import utils.ReadProperties;

public class WebDriverTest {

	private static WebDriverFactory instance;
	public static String testBrowser;
	public static String testUrl;
	public static String credentials;
	static final String CONFIG_PROPERTIES_PATH =  "src//main//resources//Config.properties";
	private static final Log log = LogFactory.getLog(WebDriverTest.class);
	
	public static WebDriver driver;
	
	public static void start() {
		
		instance = new WebDriverFactory();
		Properties configProperties = new ReadProperties().loadProperties(CONFIG_PROPERTIES_PATH);
		testBrowser =  configProperties.getProperty("browser");
		testUrl = configProperties.getProperty("url");
		credentials = configProperties.getProperty("credentials");				
		createDriver(testBrowser);
		maximizeBrowser();
		setImplicitWait();
	}
	
	private static void createDriver(String browserName){
		
		driver = instance.createDriver(browserName);
	}
	
	public static void stop() {
		
		driver.quit();
		log.info("Killing webdriver instance");
	}

	protected static void maximizeBrowser() {
		
		driver.manage().window().setPosition(new Point(0, 0));
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dim = new Dimension((int) screenSize.getWidth(), ((int) screenSize.getHeight()) - 40);
		driver.manage().window().setSize(dim);
	}
	
	protected static void setImplicitWait(){
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static WebDriver getDriver() {
		
		return driver;
	}
}