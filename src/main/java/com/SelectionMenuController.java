package com;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import static com.SceneSwitcher.switchToScene;

/**
 * FXML Controller class
 *
 * @author david
 */
public class SelectionMenuController implements Initializable {

    @FXML
    private Button physics;
    @FXML
    private Button neuralNetwork;
    @FXML
    private Button back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        back.setOnAction(e -> switchToScene(back, "main-menu.fxml"));
        neuralNetwork.setOnAction(e -> switchToScene(neuralNetwork, "network.fxml"));
        physics.setOnAction(e -> switchToScene(physics, "waves/menu.fxml"));
    }
}
