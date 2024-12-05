package com.example.samtconverter.AppleMusic;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {

    private WebDriver driver;

    public WebDriver initializeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        String userHome = System.getProperty("user.home");
        String chromeUserData = userHome + "\\AppData\\Local\\Google\\Chrome\\User Data";
        options.addArguments("user-data-dir=" + chromeUserData);
        options.addArguments("profile-directory=Default");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        return driver;
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}

