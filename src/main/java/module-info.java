module com.example.samtconverter {

    requires io.github.bonigarcia.webdrivermanager;
    requires dev.failsafe;
    requires org.seleniumhq.selenium.support;
    requires org.seleniumhq.selenium.chrome_driver;
    requires javafx.fxml;
    requires javafx.controls;


    opens com.example.samtconverter to javafx.fxml;
    exports com.example.samtconverter;
}