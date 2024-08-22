package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    Wrappers wrappers;
    WebDriverWait wait ; 

    
    @BeforeTest(enabled =  true)
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");


        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }


    @Test(description = "Verify the functionality of testCase01", enabled = true )
    public void testCase01() throws InterruptedException{
        driver.get("https://www.flipkart.com/");
        Thread.sleep(3000);
        wrappers = new Wrappers(driver);
        wrappers.searchItems("Washing Machine");
        wrappers.sortByPopularity();
        int itemsCoun = wrappers.itmesCount();
        System.out.println("Count of items with ratings less than or equal to 4 is ::  " + itemsCoun);
    }
    
    @Test(description = "Verify the functionality of testCase02" , enabled = true)
    public void testCase02() throws InterruptedException{
        driver.get("https://www.flipkart.com/");
        Thread.sleep(2000);
        wrappers = new Wrappers(driver);
        wrappers.searchItems("iPhone");
        wrappers.getTitleandDisCount();
        
    }

    @Test(description = "Verify the functionality of testcase03", enabled = true)
    public void testCase03() throws InterruptedException{
        driver.get("https://www.flipkart.com/");
        Thread.sleep(2000);
        wrappers = new Wrappers(driver);
        wrappers.searchItems("Coffee Mug");
        wrappers.selectCustomerRatings();
        wrappers.printItemsHighestNumbersOfRatings();
        }


    

    

    @AfterTest(enabled = true)
    public void endTest()
    {
        
        driver.quit();

    }
}