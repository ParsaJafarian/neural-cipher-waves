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

import static com.SceneSwitcher.switchToScene;

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


    private VisualController visual = new VisualController();
    @FXML
    private Button exit;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //opens the circular motion application when the button is pressed
        circular.setOnAction((ActionEvent e) -> {
            try {
                circular.getScene().setRoot(FXMLLoader.load(getClass().getResource("Visual.fxml")));
            } catch (IOException ex) {
            }
        });

        //opens the spring motion application when the button is pressed
        spring.setOnAction((ActionEvent e) -> {
            try {
                circular.getScene().setRoot(FXMLLoader.load(getClass().getResource("Spring.fxml")));
            } catch (IOException ex) {
            }
        });

        //opens the pendulum application when the button is pressed
        pendulum.setOnAction((ActionEvent e) -> {
            try {
                circular.getScene().setRoot(FXMLLoader.load(getClass().getResource("Pendulum.fxml")));
            } catch (IOException ex) {
            }
        });

        //returns to the previous selection screen when the exit button is pressed.
        exit.setOnAction(e -> switchToScene(exit, "selection-menu.fxml"));
    }

}

