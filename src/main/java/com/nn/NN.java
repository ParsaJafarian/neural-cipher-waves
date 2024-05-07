package com.nn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NN extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        show(stage);
    }

    //#6f5327 -> brown

    private static void show(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NN.class.getResource("network.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 725);
        stage.setTitle("Neural Network Simulation");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}