package pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import core.WebDriverPage;

public class LoginPage extends WebDriverPage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	private static final Log log = LogFactory.getLog(LoginPage.class);
	
	private static final By userName = By.name("username"); 
	private static final By passWord = By.name("password");
	private static final By signIn = By.className("signinBtn");
	
	public void launchApplication(String url){
		driver.get(url);
		waitForTestPageToLoad();
	}	
	
	public boolean checkIfLoginPageLoaded(){
		return driver.findElement(signIn).isDisplayed();
	}
	
	public void enterCredentials(String username, String password){
		  driver.manage().window().maximize();
		  driver.findElement(userName).sendKeys(username);
		  driver.findElement(passWord).sendKeys(password);
		  driver.findElement(signIn).click();
		  WebElement msg = (new WebDriverWait(driver, 400)).until(ExpectedConditions.elementToBeClickable(By.className("alert-success")));
		  log.info(msg.getText());
	}
	
	public void doSignIn(){
		driver.findElement(signIn).click();
	}

}
