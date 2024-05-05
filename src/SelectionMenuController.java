/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author david
 */
public class SelectionMenuController implements Initializable {

    @FXML
    private Button physics;
    @FXML
    private Button back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        highlightButton(back);
        back.setOnAction((ActionEvent e) -> {
            try {
                back.getScene().setRoot(FXMLLoader.load(getClass().getResource("MainMenu.fxml")));
            } catch (IOException ex) {
                Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        highlightButton(physics);
        physics.setOnAction((ActionEvent e) -> {
            try {
                physics.getScene().setRoot(FXMLLoader.load(getClass().getResource("Menu.fxml")));
            } catch (IOException ex) {
                Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //style settings to make the buttons turn yellow when mouse is hovering over it and goes back to normal when the mouse is no longer on top of it.
    public void highlightButton(Button button) {
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: grey");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: hello-view.fxml");
        });
    }
}
