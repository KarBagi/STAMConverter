module com.example.samtconverter {

    requires io.github.bonigarcia.webdrivermanager;
    requires dev.failsafe;
    requires org.seleniumhq.selenium.support;
    requires org.seleniumhq.selenium.chrome_driver;
    requires javafx.fxml;
    requires javafx.controls;
    requires se.michaelthelin.spotify;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires java.net.http;
    requires android.json;
    requires okhttp3;


    opens com.example.samtconverter to javafx.fxml;
    exports com.example.samtconverter;
}