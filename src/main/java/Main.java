//Main.java
import GUI.MainPanel;
import managePlaylists.AppleMusicPage;
import org.openqa.selenium.WebDriver;
import settingsConfiguration.DriverManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        /*DriverManager driverManager = new DriverManager();
        WebDriver driver = driverManager.initializeDriver();

        AppleMusicPage appleMusicPage = new AppleMusicPage(driver,driverManager);
        appleMusicPage.initialize();*/

        SwingUtilities.invokeLater(() -> new MainPanel().ShowMainPanel());
    }
}


