package org.example.core;

import org.example.model.Car;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class SeleniumWebCrawler {


    public List<Car> crawl(String url) {

        WebDriver driver = new SafariDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        List<Car> cars = new ArrayList<>();
        try {
            driver.get(url);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.Box-sc-wfmb7k-0")));

            List<WebElement> carElements = driver.findElements(By.cssSelector("div[class*='Box-sc-wfmb7k-0 hzLKyY']"));


            for (WebElement carElement : carElements) {
                try {
                    WebElement linkElement = carElement.findElement(By.cssSelector("a[data-testid^='search-result-entry-header']"));
                    String link = linkElement.getAttribute("href");
                    String title = linkElement.findElement(By.cssSelector("h3.Text-sc-10o2fdq-0.DnRcg")).getText();
                    String price = carElement.findElement(By.cssSelector("span[data-testid^='search-result-entry-price']")).getText();


                    String subheader = carElement.findElement(By.cssSelector("span[data-testid*='search-result-entry-subheader']")).getText();
                    String[] parts = subheader.split(",");
                    String fuelType = parts[0].trim();
                    String transmission = parts.length > 1 ? parts[1].trim() : "";
                    String kilometers = carElement.findElement(By.cssSelector("div[data-testid*='search-result-entry-teaser-attributes']:nth-child(2) span.gTqzpY")).getText();

                    Car car = new Car(title, link, price, transmission, fuelType, kilometers);
                    cars.add(car);


                } catch (NoSuchElementException e) {
                    System.out.println("Ein Element wurde nicht gefunden: " + e.getMessage());
                    continue;
                }
            }
        } finally {
            driver.quit();
        }
        return cars;
    }

}
