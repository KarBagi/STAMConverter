package managePlaylists;

// AppleMusicPage.java
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppleMusicPage {

    private WebDriver driver;

    public AppleMusicPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openPlaylistsTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navigation-item__all-playlists")));

            WebElement playlistsTab = driver.findElement(By.cssSelector(".navigation-item.navigation-item__all-playlists"));
            playlistsTab.click();

            wait.until(ExpectedConditions.urlContains("playlists"));

        } catch (Exception e) {
            System.out.println("Nie udało się znaleźć zakładki z playlistami.");
            e.printStackTrace();
        }
    }

    public List<String> getPlaylistNames() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[data-testid='product-lockup-title']")));

            List<WebElement> playlistElements = driver.findElements(By.cssSelector("span[data-testid='product-lockup-title']"));

            return playlistElements.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("Nie udało się pobrać nazw playlist.");
            e.printStackTrace();
            return List.of();
        }
    }

    public void openPlaylistByName(String playlistName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[data-testid='product-lockup-title']")));

            List<WebElement> playlistElements = driver.findElements(By.cssSelector("span[data-testid='product-lockup-title']"));

            for (WebElement element : playlistElements) {
                if (element.getText().equalsIgnoreCase(playlistName)) {
                    element.click();
                    return;
                }
            }

            System.out.println("Nie znaleziono playlisty o nazwie: " + playlistName);

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas próby otwarcia playlisty.");
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> getSongsAndArtists() {
        ArrayList<ArrayList<String>> playlistDetails = new ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.songs-list-row__song-name[data-testid='track-title']")));

            WebElement playlistTitleElement = driver.findElement(By.cssSelector("h1")); // Upewnij się, że selektor jest poprawny
            String playlistTitle = playlistTitleElement.getText();

            List<WebElement> songElements = driver.findElements(By.cssSelector("div.songs-list-row__song-name[data-testid='track-title']"));
            List<WebElement> artistElements = driver.findElements(By.cssSelector("a[data-testid='click-action'][href^='https://music.apple.com']"));

            ArrayList<String> playlistInfo = new ArrayList<>();
            playlistInfo.add(playlistTitle);

            for (int i = 0; i < songElements.size(); i++) {
                String songTitle = songElements.get(i).getText();
                String artistName = i < artistElements.size() ? artistElements.get(i).getText() : "Nieznany artysta";
                playlistInfo.add(songTitle + " - " + artistName);
            }

            playlistDetails.add(playlistInfo);

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas próby pobrania informacji o piosenkach.");
            e.printStackTrace();
        }

        return playlistDetails;
    }
}


