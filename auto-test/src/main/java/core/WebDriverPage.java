package core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

public class WebDriverPage {

	protected final WebDriver driver;
	private static final Log log = LogFactory.getLog(WebDriverPage.class);
	
	protected static final int PAGE_LOAD_TIMEOUT = 100000;
	static JavascriptExecutor js;

	public WebDriverPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void waitForTestPageToLoad(int timeout) {
		WaitForPageToLoad wait = new WaitForPageToLoad();
		wait.apply(driver, new String[] { String.valueOf(timeout) });
	}

	public void waitForTestPageToLoad() {
		WaitForPageToLoad wait = new WaitForPageToLoad();
		try{
			wait.apply(driver, new String[] { String.valueOf(PAGE_LOAD_TIMEOUT) });
		}catch(WebDriverException e){
			log.info(e.getMessage());
		}
	}
	
	public void  open(String url){
		
		driver.get(url);
		waitForTestPageToLoad();
	}
	
	public boolean isElementEnabled(By by) {
		
		return driver.findElement(by).isEnabled();
	}
	
	public boolean isElementDisplayed(By by) {
		return driver.findElement(by).isDisplayed();
	}
	
	public void loadJavaScript() throws InterruptedException{
		js = (JavascriptExecutor) driver;
		js.executeScript("var doc = window.document; var scriptTag = doc.createElement('script'); scriptTag.type = 'text/javascript'; scriptTag.src = 'http://mld-d-j29wwq1:28080/examples/ng-test_updated.js'; doc.body.appendChild(scriptTag);");
		Thread.sleep(10000);
	}

}
