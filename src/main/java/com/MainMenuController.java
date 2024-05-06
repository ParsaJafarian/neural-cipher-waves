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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import static com.SceneSwitcher.switchToScene;

/**
 * FXML Controller class
 *
 * @author david
 */
public class MainMenuController implements Initializable {

    @FXML
    private Button start;
    @FXML
    private Button info;
    @FXML
    private Button exit;
    @FXML
    private Button closeInfo;
    @FXML
    private AnchorPane infoPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //creates a highlight effect on the "Start" button
        highlightButton(start);
        start.setOnAction(e -> switchToScene(start, "selection-menu.fxml"));

        //creates a highlight effect on the "Info" button
        highlightButton(info);
        info.setOnAction(e -> {
            infoPane.setVisible(true);
        });

        highlightButton(closeInfo);
        closeInfo.setOnAction(e -> {
            infoPane.setVisible(false);
        });


        //closes the application upon pressing on the exit button
        exit.setOnAction(e -> {
            System.exit(0);
        });
    }

    //style settings to make the buttons turn yellow when mouse is hovering over it and goes back to normal when the mouse is no longer on top of it.
    public void highlightButton(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: grey"));
        button.setOnMouseExited(e -> button.setStyle(null));
    }
}
