package settingsConfiguration;

// DriverManager.java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {

    private WebDriver driver;

    public WebDriver initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        String userHome = System.getProperty("user.home");
        String chromeUserData = userHome + "\\AppData\\Local\\Google\\Chrome\\User Data";
        options.addArguments("user-data-dir=" + chromeUserData);
        options.addArguments("profile-directory=Default");

        driver = new ChromeDriver(options);
        return driver;
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
