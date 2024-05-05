package com;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
        back.setOnAction(e -> redirect(back, "main-menu.fxml"));
        neuralNetwork.setOnAction(e -> redirect(neuralNetwork, "network.fxml"));
        physics.setOnAction(e -> redirect(physics, "waves/menu.fxml"));
    }

    private void redirect(Button button, String pathToFxml) {
        try {
            button.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pathToFxml))));
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
