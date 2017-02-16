package com.github.bachelorpraktikum.dbvisualization.view;

import com.github.bachelorpraktikum.dbvisualization.config.ConfigFile;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.annotation.Nonnull;

public class LoginController {

    @FXML
    private Button openButton;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    private ObjectProperty<String> userProperty;
    private ObjectProperty<String> passwordProperty;

    @FXML
    public void initialize() {
        userProperty = new SimpleObjectProperty<>();
        passwordProperty = new SimpleObjectProperty<>();

        userField.textProperty().bindBidirectional(userProperty);
        passwordField.textProperty().bindBidirectional(passwordProperty);

        passwordProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && userProperty.getValue() != null) {
                openButton.setDisable(false);
            }
        });
        userProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && passwordProperty.getValue() != null) {
                openButton.setDisable(false);
            }
        });
        openButton.setOnAction(event -> {
            putCredentials();
        });
    }

    @Nonnull
    public String getUser() {
        return userProperty.getValue();
    }

    @Nonnull
    public String getPassword() {
        return userProperty.getValue();
    }

    void setStage(Stage stage) {
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);

        stage.centerOnScreen();
    }

    private void putCredentials() {
        ConfigFile instance = ConfigFile.getInstance();
        String userKey = ResourceBundle.getBundle("config_keys").getString("databaseUserKey");
        String passwordKey = ResourceBundle.getBundle("config_keys")
            .getString("databasePasswordKey");
        instance.put(userKey, getUser());
        instance.put(passwordKey, getPassword());
    }

}
