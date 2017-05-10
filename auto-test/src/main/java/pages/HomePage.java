package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import core.WebDriverPage;

public class HomePage extends WebDriverPage {

	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	private static By APP_TITLE = By.cssSelector("appTitle");
	
	public String getAppTitle(){
		
		return driver.findElement(APP_TITLE).getText();
	}

	public Boolean verifyAppTitle(String appName) {
		
		return getAppTitle().contains(appName);
	}

}
