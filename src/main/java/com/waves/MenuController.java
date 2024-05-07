package com.waves;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 * @author 2278304
 */
public class MenuController implements Initializable {
    @FXML
    private Button circular;
    @FXML
    private Button pendulum;
    @FXML
    private Button spring;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        circular.setOnAction((ActionEvent e) -> {
            try {
                //changes the root of the scene to direct the user to the slideshow before the race starts
                circular.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("visual.fxml"))));
            } catch (IOException ignored) {
            }
        });

        spring.setOnAction((ActionEvent e) -> {
            try {
                //changes the root of the scene to direct the user to the slideshow before the race starts
                circular.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("spring.fxml"))));
            } catch (IOException ignored) {
            }
        });

        pendulum.setOnAction((ActionEvent e) -> {
            try {
                //changes the root of the scene to direct the user to the slideshow before the race starts
                circular.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pendulum.fxml"))));
            } catch (IOException ignored) {
            }
        });
    }

}
