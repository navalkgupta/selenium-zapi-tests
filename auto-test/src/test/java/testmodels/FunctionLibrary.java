package testmodels;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.testng.Assert;

import core.WebDriverTest;
import pages.HomePage;
import pages.LoginPage;

public class FunctionLibrary extends WebDriverTest {
	
	private static String actualResult = "Not as exppected";
	
	LoginPage loginPage = null;
	HomePage homePage = null;
	
	
	public FunctionLibrary(){
		
		this.loginPage = new LoginPage(getDriver());
		this.homePage = new HomePage(getDriver());
	}
	
	List<Boolean>  errors;
	
	public String launchApp(String data, String result){
		if(data != null && !data.isEmpty()){
			data = testUrl;
		}
		
		loginPage.open(data);
		
		verifyResult(loginPage.checkIfLoginPageLoaded(), result);
		return actualResult;
	}
	
	public String doLogin(String data, String result){
		
		Assert.assertTrue(loginPage.checkIfLoginPageLoaded());
		String credentials[] = data.split(":");
		loginPage.enterCredentials(credentials[0], decodePassword(credentials[1]));
		loginPage.doSignIn();
		
		verifyResult(homePage.verifyAppTitle("Test App"), result);
		return actualResult;
	}
	
	private String decodePassword(String encodedPassword){

		Base64 decoder = new Base64();
		byte[] decodedBytes = decoder.decode(encodedPassword);
		return new String(decodedBytes);
	}
	
	private void verifyResult(Boolean condition, String result){
		
		if(condition){
			actualResult = result;
		}
	}

}
