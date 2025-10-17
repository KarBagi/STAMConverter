package com.example.samtconverter;

import com.example.samtconverter.AppleMusic.DriverManager;
import com.example.samtconverter.AppleMusic.SeleniumAppleMusic;
import com.example.samtconverter.Spotify.SpotifyAuthorization;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {

    @FXML
    private ImageView appleMusicButton1;

    @FXML
    private ImageView appleMusicButton2;

    @FXML
    private ImageView arrowLeftButton;

    @FXML
    private ImageView arrowRightButton;

    @FXML
    private VBox playlistsVBox2;

    @FXML
    private VBox playlistsVBox1;

    @FXML
    private Button showStatisticButton;

    @FXML
    private ImageView spotifyButton1;

    @FXML
    private ImageView spotifyButton2;

    @FXML
    private Button subbmitButton;

    @FXML
    private ImageView tidalButton1;

    @FXML
    private ImageView tidalButton2;

    @FXML
    private Button howToUseButton;

    private boolean isChecked = false;

    private int playlistVboxIdChecked = 0;

    private String playlistVboxNameChecked = "";

    private Integer i = 0;

    List<String> playlists = new ArrayList<>();
    List<String> spotifyPlaylists = new ArrayList<>();
    List<String> appleMusicPlaylists = new ArrayList<>();

    SeleniumAppleMusic seleniumAppleMusic;
    SpotifyAuthorization spotifyAuthorization = new SpotifyAuthorization();

    ImageView[] appChoices1;
    ImageView[] appChoices2;
    ImageView[] arrowChoices1;

    @FXML
    public void initialize() {
        appChoices1 = new ImageView[] {
                spotifyButton1, appleMusicButton1, tidalButton1
        };
        appChoices2 = new ImageView[] {
                spotifyButton2, appleMusicButton2, tidalButton2
        };
        arrowChoices1 = new ImageView[] {
                arrowLeftButton, arrowRightButton
        };
    }

    @FXML
    public boolean spotify1(javafx.scene.input.MouseEvent mouseEvent) {

        appChoice("spotifyButton1", appChoices1);
        showSpotifyPlaylists();
        showPlaylists(spotifyPlaylists, true);

        return true;
    }

    @FXML
    public boolean appleMusic1(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("appleMusicButton1", appChoices1);

        showAppleMusicPlaylists();
        showPlaylists(appleMusicPlaylists, true);
        return true;
    }

    @FXML
    public boolean tidal1(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("tidalButton1", appChoices1);

        return true;
    }

    @FXML
    public boolean spotify2(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("spotifyButton2", appChoices2);
        showSpotifyPlaylists();
        showPlaylists(spotifyPlaylists, true);

        return true;
    }

    @FXML
    public boolean appleMusic2(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("appleMusicButton2", appChoices2);

        showAppleMusicPlaylists();
        showPlaylists(appleMusicPlaylists, false);

        return true;
    }

    @FXML
    public boolean tidal2(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("tidalButton2", appChoices2);

        return true;
    }

    @FXML
    public boolean arrowLeft(javafx.scene.input.MouseEvent mouseEvent) {
        arrowChoice("arrowLeftButton", arrowChoices1);

        return true;
    }

    @FXML
    public boolean arrowRight(javafx.scene.input.MouseEvent mouseEvent) {
        arrowChoice("arrowRightButton", arrowChoices1);

        return true;
    }

    @FXML
    public void submitButton() throws IOException {
        List<String> spotifyPlaylistsId = spotifyAuthorization.getPlaylistsId();
        DriverManager driverManager = new DriverManager();
        WebDriver driver = driverManager.initializeDriver();

        seleniumAppleMusic = new SeleniumAppleMusic(driver, driverManager);

/*        for(int i = 0; i < spotifyPlaylists.size(); i++){
            if(spotifyPlaylists.get(i).equals("Mac")){
                System.out.println(i);
                break;
            }
        }*/

        //System.out.println(spotifyPlaylists.get(0));
        List<String> spotifyPlaylistsTracks = spotifyAuthorization.fetchTracksFromPlaylist(spotifyPlaylistsId.get(playlistVboxIdChecked));
        seleniumAppleMusic.add(spotifyPlaylistsTracks, playlistVboxNameChecked);

        //TODO dodaje się cały album dla piosenek, które się tak samo nazywają xdxdddddxddxxdxdd
        //System.out.println(spotifyAuthorization.fetchTracksFromPlaylist(spotifyPlaylistsId.get(0)));
    }

    public void appChoice(String appName, ImageView[] appPool) {
        for (ImageView app : appPool) {
            ColorAdjust colorAdjust = new ColorAdjust();

            if (appName.equals(app.getId())) {
                colorAdjust.setBrightness(1.0);
            } else {
                colorAdjust.setBrightness(0.5);
            }

            if(appPool[0].getId().equals("spotifyButton1")) {
                playlistsVBox1.getChildren().clear();
            } else {
                playlistsVBox2.getChildren().clear();
            }

            app.setEffect(colorAdjust);
        }
    }

    public void arrowChoice(String arrowName, ImageView[] arrowPool) {
        for (ImageView arrow : arrowPool) {
            ColorAdjust colorAdjust = new ColorAdjust();

            if (arrowName.equals(arrow.getId())) {
                colorAdjust.setBrightness(1.0);
            } else {
                colorAdjust.setBrightness(0.5);
            }

            arrow.setEffect(colorAdjust);
        }
    }

    public void showPlaylists(List<String> playlists, boolean whichVBox) {

        for (String playlist : playlists.subList(0, playlists.size())) {
            HBox playlistRow = new HBox();
            playlistRow.setStyle("-fx-background-color: #666666; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 0 0 1 0;");
            playlistRow.setAlignment(Pos.CENTER_LEFT);
            playlistRow.setUserData(i);

            Label playlistLabel = new Label(playlist);
            playlistLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            playlistRow.getChildren().add(playlistLabel);

            playlistRow.setOnMouseClicked(event -> {

                if (!isChecked) {
                    playlistRow.setStyle("-fx-background-color: #828282; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 0 0 1 0;");
                    isChecked = true;

                    playlistVboxIdChecked = (int) playlistRow.getUserData();
                    playlistVboxNameChecked = playlistLabel.getText();

                    System.out.println(playlistVboxIdChecked + " " + playlistVboxNameChecked);
                } else {
                    playlistRow.setStyle("-fx-background-color: #666666; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 0 0 1 0;");
                    isChecked = false;
                    playlistVboxIdChecked = i;
                }
            });

            if (whichVBox) playlistsVBox1.getChildren().add(playlistRow);
            else playlistsVBox2.getChildren().add(playlistRow);
            i++;
        }
    }

    public void showSpotifyPlaylists() {
        spotifyPlaylists.clear();

        try {
            spotifyAuthorization.initiate();
            spotifyPlaylists = spotifyAuthorization.fetchPlaylists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAppleMusicPlaylists() {
        appleMusicPlaylists.clear();

        DriverManager driverManager = new DriverManager();
        WebDriver driver = driverManager.initializeDriver();

        seleniumAppleMusic = new SeleniumAppleMusic(driver, driverManager);
        appleMusicPlaylists = seleniumAppleMusic.getPlaylistDetails(playlists);

    }
}