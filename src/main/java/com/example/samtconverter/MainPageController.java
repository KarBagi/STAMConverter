package com.example.samtconverter;

import com.example.samtconverter.AppleMusic.DriverManager;
import com.example.samtconverter.AppleMusic.SeleniumAppleMusic;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openqa.selenium.WebDriver;

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

    List<String> playlists = new ArrayList<>();

    SeleniumAppleMusic seleniumAppleMusic;

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

        return true;
    }

    @FXML
    public boolean appleMusic1(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("appleMusicButton1", appChoices1);

        showAppleMusicPlaylists(true);
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

        return true;
    }

    @FXML
    public boolean appleMusic2(javafx.scene.input.MouseEvent mouseEvent) {
        appChoice("appleMusicButton2", appChoices2);

        showAppleMusicPlaylists(false);

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

    public void showAppleMusicPlaylists(boolean whichVBox) {
        DriverManager driverManager = new DriverManager();
        WebDriver driver = driverManager.initializeDriver();


        seleniumAppleMusic = new SeleniumAppleMusic(driver, driverManager);
        playlists = seleniumAppleMusic.getPlaylistDetails(playlists);

        for (String playlist : playlists.subList(1, playlists.size())) {
            HBox playlistRow = new HBox();
            playlistRow.setStyle("-fx-background-color: #666666; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 0 0 1 0;");
            playlistRow.setAlignment(Pos.CENTER_LEFT);

            Label playlistLabel = new Label(playlist);
            playlistLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            playlistRow.getChildren().add(playlistLabel);

            playlistRow.setOnMouseClicked(event -> {
                if(!isChecked){
                    playlistRow.setStyle("-fx-background-color: #828282; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 0 0 1 0;");
                    isChecked = true;
                }
                else{
                    playlistRow.setStyle("-fx-background-color: #666666; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 0 0 1 0;");
                    isChecked = false;
                }
            });

            if(whichVBox) playlistsVBox1.getChildren().add(playlistRow);
            else playlistsVBox2.getChildren().add(playlistRow);
        }
    }
}