package com.org.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.org.ui.base.SeleniumUtils;

public class crmpropage extends SeleniumUtils{
	  @FindBy(name = "username")
	    private WebElement Username;
	  @FindBy(name = "password")
	    private WebElement Password;
	  @FindBy(xpath = "//*[@id=\"loginForm\"]/div/div/input")
	    private WebElement Login;
	  
	  public void crmlogin(String un, String pw) {
		  Username.sendKeys(un);
		  Password.sendKeys(pw);
		 // Login.click();
		  
		  WebElement login = driver.findElement(By.xpath("//input[@value='Login']"));
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].click();", login);
		 
	    }
	  
	  public void crmloginbutton() {
		  Login.click();
		 
	    }


}
