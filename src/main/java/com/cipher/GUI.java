package com.cipher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        show(stage);
    }

    /**
     * The GUI simply launches a scene according to the FXML file
     * @param stage
     * @throws IOException
     */
    public static void show(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("caesar.fxml"));
        var scene = new Scene(fxmlLoader.load(), 1100, 770);
        stage.setTitle("Dynamic Caesar Cipher");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
