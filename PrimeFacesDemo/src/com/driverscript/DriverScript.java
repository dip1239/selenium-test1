package com.driverscript;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;



public class DriverScript {
	
	private static Logger logger = Logger.getLogger(DriverScript.class);
	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		String path = "./config/log-config.xml";
		DOMConfigurator.configure(path);
		
		
		//Logger logs = Logger.getLogger("devpinoyLogger");
		String testURL=null;
		
		
		//getting test url from properties file
		logger.debug("Debug - Reading values from property files...");
				FileInputStream  fis=new FileInputStream(".\\config\\TestURL.properties");
				Properties prop=new Properties();
				prop.load(fis);
				System.out.println("***** Test URL is: "+prop.getProperty("testURL")+"  *******");
				logger.warn("warn ***** Test URL is: "+prop.getProperty("testURL")+"  *******");
				testURL=prop.getProperty("testURL");
		
		System.out.println("***********Initializing WebDriver....************");
		logger.info("info ***********Initializing WebDriver....************");
		WebDriver driver=new FirefoxDriver();
		driver.get(testURL);
		
	
		WebElement query = (new WebDriverWait(driver, 60)).until(new ExpectedCondition <WebElement>() {
			  // WebElement query= wait.until(new ExpectedCondition <WebElement>() {
				public WebElement apply (WebDriver d){
					return d.findElement(By.id("form:carsTable:model"));
				}});
		 
		 //search for "d" in MOdel filter
		logger.info("Verifying partial search functionality on Model column");
		 driver.findElement(By.id("form:carsTable:model:filter")).sendKeys("d");
		 boolean row_check=false;
		 Thread.sleep(5000L);
		 
		//validate data for the search filter
			 
			 List<WebElement> listQuery=driver.findElements(By.xpath("//tbody[@id='form:carsTable_data']/tr"));
			 System.out.println("No. of rows in Result Set Data for the searched text : "+listQuery.size());
			 logger.info("No. of rows in Result Set Data for the searched text : "+listQuery.size());
			 
			 //int count = (driver.findElements(By.xpath("//tbody[@id='form:carsTable_data']/tr")).size());
			 //System.out.println(count);
			 
			 if(listQuery.size()>0){
				 row_check=true;
				 
			 //printing searched data ...
				 for (WebElement resultData:listQuery){
					 if(resultData.getText().contains("No records found.")){
						 System.out.println("No data found for the searched text");
						 row_check=false;
						 break;
					 }
					 System.out.println(resultData.getText());
									 
				 }
				 
			 }
			 if (row_check){
			 
			//Selecting data link			
			 WebElement dataLink=listQuery.get(0).findElement(By.cssSelector("tr.ui-widget-content td"));
			 String model=dataLink.getText();
			 
			 dataLink.click(); 
			 Thread.sleep(3000L);
			 
			 //validating popup with main grid data
			 WebElement popup_table=driver.findElement(By.id("form:display"));
			 
			 List<WebElement> popup_Model=popup_table.findElements(By.tagName("span"));
			 System.out.println("No. of rows in the popup : "+popup_Model.size());
			 System.out.println("Model Name displayed in popup : "+popup_Model.get(0).getText());
			 
			 if(popup_Model.get(0).getText().equals(model))
			System.out.println("Name of the Car Model is MATCHING with the name that is displayed in the Main data grid");
			 else
			System.out.println("Name of the Car Model is NOT MATCHING with the name that is displayed in the Main data grid");
			
			 }
			 
			System.out.println("\n*****************End of Test *********************");
	}

}
