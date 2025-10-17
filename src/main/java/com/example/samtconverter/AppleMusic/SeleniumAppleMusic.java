package com.example.samtconverter.AppleMusic;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SeleniumAppleMusic {

    private WebDriver driver;
    private DriverManager driverManager;

    public SeleniumAppleMusic(WebDriver driver, DriverManager driverManager) {
        this.driver = driver;
        this.driverManager = driverManager;
    }

    public List<String> getPlaylistDetails(List<String> playlists) {
        try {
            driver.get("https://music.apple.com/pl/new");

            openPlaylistsTab();
            playlists = getPlaylistNames();

        } finally {
            driverManager.closeDriver();
        }
        return playlists;
    }

    public void openPlaylistsTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navigation-item__all-playlists")));

            WebElement playlistsTab = driver.findElement(By.cssSelector(".navigation-item.navigation-item__all-playlists"));
            playlistsTab.click();

            wait.until(ExpectedConditions.urlContains("playlists"));

        } catch (Exception e) {
            System.out.println("The playlist tab could not be found.");
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
            System.out.println("Failed to download playlist names.");
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

            System.out.println("The playlist named was not found: " + playlistName);

        } catch (Exception e) {
            System.out.println("An error occurred while trying to open a playlist.");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSongsAndArtists() {
        ArrayList<String> playlistDetails = new ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.songs-list-row__song-name[data-testid='track-title']")));

            WebElement playlistTitleElement = driver.findElement(By.cssSelector("h1"));
            String playlistTitle = playlistTitleElement.getText();

            List<WebElement> songElements = driver.findElements(By.cssSelector("[data-testid='track-column-song']"));
            List<WebElement> artistElements = driver.findElements(By.cssSelector("[data-testid='track-column-secondary']"));


            for (int i = 0; i < songElements.size(); i++) {
                String songTitle = songElements.get(i).getText();
                String artistName = i < artistElements.size() ? artistElements.get(i).getText() : "Unknown artist";
                playlistDetails.add(songTitle + " - " + artistName);
            }

        } catch (Exception e) {
            System.out.println("An error occurred while trying to download song information.");
            e.printStackTrace();
        }

        return playlistDetails;
    }

    public void createPlaylist(WebDriver driver, String songName, String playlistName) {

        driver.get("https://music.apple.com/pl/search?term=" + songName);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement firstSong = driver.findElement(By.cssSelector("[data-testid='grid-item']"));

        WebElement moreButton = firstSong.findElement(By.cssSelector("[data-testid='more-button']"));
        moreButton.click();

        WebElement addToPlaylist = moreButton.findElement(By.xpath("//*[@title='Add to Playlist']"));
        addToPlaylist.click();

        WebElement newPlaylist = addToPlaylist.findElement(By.xpath("//*[@title='New Playlist']"));
        newPlaylist.click();

        WebElement form = driver.findElement(By.xpath("//form[@class='playlist-form svelte-1kd2e9n']"));

        WebElement playlistTitle = form.findElement(By.cssSelector("[data-testid='playlist-title-input']"));

        playlistTitle.sendKeys(playlistName);

        WebElement buttons = form.findElement(By.className("buttons"));
        WebElement submitButton = buttons.findElement(By.className("submit-button"));
        WebElement createButton = submitButton.findElement(By.className("svelte-yk984v"));

        createButton.click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSongToPlaylist(String songName, String playlistName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://music.apple.com/pl/search?term=" + songName);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        List<WebElement> songs = driver.findElements(By.cssSelector("[data-testid='grid-item']"));


        for (WebElement song : songs) {
            // Pobierz pełny tekst z elementu
            String fullText = song.getText();

            // Rozbij pełny tekst na linie, aby łatwiej przetworzyć
            String[] lines = fullText.split("\n");

            if (!fullText.toLowerCase().contains("song")) {
                continue; // Jeśli nie, przejdź do następnego elementu
            }

            if (lines.length >= 2) { // Sprawdź, czy mamy przynajmniej dwie linie
                String actualTrackName = lines[0].trim(); // Pierwsza linia - nazwa piosenki
                String actualArtistName = lines[1].trim().replace("Song · ", ""); // Druga linia - artysta

                // Porównanie z sformatowanym tekstem
                String actualText = actualTrackName + " - " + actualArtistName;

                if (songName.equalsIgnoreCase(actualText)) {
                    WebElement moreButton = song.findElement(By.cssSelector("[data-testid='more-button']"));
                    moreButton.click();

                    //WebElement addToPlaylist = driver.findElement(By.xpath("//*[@title='Add to Playlist']"));
                    WebElement addToPlaylist = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@title='Add to Playlist']")));
                    addToPlaylist.click();

                    clickPlaylistByTitle(playlistName);
                    break;
                }
            }
        }
    }

    private void clickPlaylistByTitle(String playlistName) {
        try {
            WebElement playlistButton = driver.findElement(By.xpath("//div[@class='contextual-menu__group']" +
                    "//button[@title='" + playlistName + "']"));

            playlistButton.click();

            Thread.sleep(500);

            System.out.println("Added to playlist " + playlistName);

        } catch (Exception e) {
            System.out.println("Nie znaleziono playlisty o nazwie: " + playlistName);
        }
    }

    private void as(WebDriver driver) {
        ArrayList<String> songs = new ArrayList<>();
        songs.add("Ocean Drive - Duke Dumont");
        songs.add("Music Sounds Better with You - Stardust");
        songs.add("My Love (feat. Jess Glynne) - Route 94");
        songs.add("Lady (Hear Me Tonight) - Modjo");
        songs.add("Pumped Up Kicks - Foster the People");
        songs.add("Feel Good Inc. - Gorillaz");
        songs.add("Habits (Stay High) [Hippie Sabotage Remix] - Tove Lo");
        songs.add("Feels Like Summer - Childish Gambino");
        songs.add("I Got U (feat. Jax Jones) - Duke Dumont");

        List<String> playlists;
        String playlistName = "Chill";

        openPlaylistsTab();
        playlists = getPlaylistNames();

        if(playlists.contains(playlistName)) {
            System.out.println("Choose different playlist name");
        }
        else {
            createPlaylist(driver, songs.get(0),playlistName);
            songs.remove(0);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (String song : songs) {
                addSongToPlaylist(song, playlistName);
            }
        }
    }

    public void add(List<String> tracks, String playlistName) {
        createPlaylist(driver, tracks.get(0),playlistName);
        tracks.remove(0);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (String song : tracks) {
            addSongToPlaylist(song, playlistName);
        }
    }
}





