import managePlaylists.AppleMusicPage;
import org.openqa.selenium.WebDriver;
import settingsConfiguration.DriverManager;

public class Main {

    public static void main(String[] args) {
        DriverManager driverManager = new DriverManager();
        WebDriver driver = driverManager.initializeDriver();

        AppleMusicPage appleMusicPage = new AppleMusicPage(driver,driverManager);
        appleMusicPage.initialize();
    }
}


