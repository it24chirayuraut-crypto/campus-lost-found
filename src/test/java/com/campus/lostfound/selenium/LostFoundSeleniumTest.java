package com.campus.lostfound.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LostFoundSeleniumTest {

    private static WebDriver driver;
    private static final String BASE_URL = "http://localhost:8081";

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
    }

    @Test
    void testHomePageLoads() {
        driver.get(BASE_URL + "/");
        assertTrue(driver.getPageSource().contains("Campus Lost"),
                "Home page should load and show the app title");
    }

    @Test
    void testReportItemFlowEndToEnd() {
        // Go to the "Report an Item" page
        driver.get(BASE_URL + "/post");

        // Fill the form
        driver.findElement(By.name("name")).sendKeys("Blue Water Bottle");
        driver.findElement(By.name("location")).sendKeys("Library 2nd Floor");
        driver.findElement(By.name("description")).sendKeys("Steel bottle with a dent on the side");
        driver.findElement(By.name("contact")).sendKeys("chirayu@example.com");

        // Submit the form
        driver.findElement(By.id("submitBtn")).click();

        // Should now be back on the home page — verify the new item appears
        assertTrue(driver.getPageSource().contains("Blue Water Bottle"),
                "Newly reported item should appear on the home page");

        // Click into its detail page immediately, in the same test
        WebElement itemLink = driver.findElement(By.linkText("Blue Water Bottle"));
        itemLink.click();

        assertTrue(driver.getPageSource().contains("Library 2nd Floor"),
                "Item detail page should show the correct location");
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}