package com;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneSwitcher {
    /**
     * Redirects to a new scene
     * @param button the button that was clicked
     * @param pathToFxml the path to the fxml file
     */
    public static void switchToScene(@NotNull Button button, String pathToFxml) {
        try {
            button.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource(pathToFxml))));
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
