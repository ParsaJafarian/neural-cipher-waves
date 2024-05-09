package com.waves;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author david
 */
public class SHM extends Application {

    Parent root;
    @Override
    public void start(Stage primaryStage) throws IOException {
        //makes the root the welcome screen
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        //creates scene
        Scene scene = new Scene(root);

        primaryStage.setTitle("Physics");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }
}