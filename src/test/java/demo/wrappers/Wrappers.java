package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    WebDriver driver;
    WebDriverWait wait;

    String searchPageurl = "https://www.flipkart.com/search?q=Washing%20machine&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off";


    @FindBy(className ="Pke_EE")
    private WebElement searchInput;

    @FindBy(xpath ="//div[text()='Popularity']")
    private WebElement popularity;

    @FindBy(xpath = "//span[contains(@class, 'Y1HWO0')]//div[contains(@class, 'XQDdHH')]")
    private List<WebElement> ratings;

    @FindBy(className = "tUxRFH")
    private List<WebElement> items;

    @FindBy(xpath = "(//label[contains(@class, 'tJjCVx _3DvUAf')]//div[@class='_6i1qKy' ])[1]")
    private WebElement fourStarsRatings;


    @FindBy(className = "slAVV4")
    private List<WebElement> mugItems;






    public Wrappers(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);

    }

    public void searchItems(String itemName){

        try{
            wait.until(ExpectedConditions.visibilityOf(searchInput));
            searchInput.clear();
            searchInput.sendKeys(itemName);
            searchInput.sendKeys(Keys.ENTER);
            wait.until(ExpectedConditions.urlContains("search?q=" + itemName.replace(" ", "%20")));          
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("search?q=" + itemName.replace(" ", "%20"))) {
                System.out.println("Search result for: " + itemName);                
            }         
        }catch(Exception e){
            System.out.println("Unable to search for item");
            e.printStackTrace();
        }
    }

    public void sortByPopularity(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(popularity)).click();
            System.out.println("sorting by popularity");
        }catch(Exception e){
            System.out.println("unable to click on element");
            e.printStackTrace();
        }

    }

    public int itmesCount() throws InterruptedException{
        int  count =0;       
        wait.until(ExpectedConditions.visibilityOfAllElements(ratings));
        Thread.sleep(3000);
        for(WebElement rating: ratings){
                wait.until(ExpectedConditions.visibilityOf(rating));
                Thread.sleep(3000);

                String ratingText = rating.getText();
                if(!ratingText.isEmpty()){
                    try{
                        float actualRating = Float.parseFloat(ratingText);
                        if(actualRating<=4){
                            count++;
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Unable to parse rating :: "+ratingText);

                    }
                }               
         }       
        return count;                 
    }


    public void getTitleandDisCount(){
        for(WebElement item : items){
            try{
                wait.until(ExpectedConditions.visibilityOfAllElements(items));
                WebElement titleElement = item.findElement(By.xpath(".//div[contains(@class, 'KzDlHZ')]"));
                WebElement discountElement = item.findElement(By.xpath(".//div[contains (@class ,'cN1yYO')]//span"));
                
                String title = titleElement.getText();
                String discount = discountElement.getText().replaceAll("[^\\d]", "");
                int discountPercentage = Integer.parseInt(discount);
                
                if(discountPercentage>17){
                    System.out.println("Title :: "+ title);
                    System.out.println("Discount :: "+discountPercentage+ "%");
                }
            }catch(Exception e){
                System.out.println("Unable to find the items ");
                e.printStackTrace();
            }          
        }
    }

    public void selectCustomerRatings(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(fourStarsRatings)).click();
        }catch(Exception e){
            System.out.println("Unable to click on 4 stars ");
            e.printStackTrace();
        }
    }

    public void printItemsHighestNumbersOfRatings(){
        try{
            Thread.sleep(10000);
 
            wait.until(ExpectedConditions.visibilityOfAllElements(mugItems));
            System.out.println("mug items found");
            List<WebElement> updatedMugItems = driver.findElements(By.className("slAVV4"));



        // sorting the list of elements     
        List<WebElement> sortedItems = updatedMugItems.stream().sorted((a, b) -> {
            try {
                //here taking 2 revies at a time
                WebElement reviewElementA = wait.until(ExpectedConditions.visibilityOf(a.findElement(By.cssSelector(".Wphh3N"))));
                WebElement reviewElementB = wait.until(ExpectedConditions.visibilityOf(b.findElement(By.cssSelector(".Wphh3N"))));

                int reviewA = Integer.parseInt(reviewElementA.getText().replaceAll("\\D", ""));
                int reviewB = Integer.parseInt(reviewElementB.getText().replaceAll("\\D", ""));
                return Integer.compare(reviewB, reviewA);
                 } catch (StaleElementReferenceException e) {
                    //returning 0 because when review a = review b
                     return 0;
                 }
    
            }).limit(5).collect(Collectors.toList());
            System.out.println("items has been compared");
    
            for(WebElement item : sortedItems){
                WebElement titleElement = wait.until(ExpectedConditions.visibilityOf(item.findElement(By.cssSelector(".wjcEIp"))));
                WebElement imageElement = wait.until(ExpectedConditions.visibilityOf(item.findElement(By.cssSelector(".DByuf4"))));
    
                String title = titleElement.getText();
                String imageUrl = imageElement.getAttribute("src");
                System.out.println("Title :: " + title);
                System.out.println("Image url :: " + imageUrl);
                System.out.println("-----");
            }
    
        }catch(Exception e){
            System.out.println("Unable to find the items");
            e.printStackTrace();
        }

        }
       



     
}
