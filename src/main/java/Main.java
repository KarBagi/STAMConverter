
import managePlaylists.AppleMusicPage;
import org.openqa.selenium.WebDriver;
import settingsConfiguration.DriverManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DriverManager driverManager = new DriverManager();
        WebDriver driver = driverManager.initializeDriver();

        try {
            driver.get("https://music.apple.com/");

            AppleMusicPage appleMusicPage = new AppleMusicPage(driver);
            appleMusicPage.openPlaylistsTab();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Wprowadź nazwę playlisty: ");
            String playlistName = scanner.nextLine();

            appleMusicPage.openPlaylistByName(playlistName);

            ArrayList<ArrayList<String>> playlistDetails = appleMusicPage.getSongsAndArtists();

            for (ArrayList<String> details : playlistDetails) {
                for (String detail : details) {
                    System.out.println(detail);
                }
            }

            scanner.close();

        } finally {
            driverManager.closeDriver();
        }
    }
}


