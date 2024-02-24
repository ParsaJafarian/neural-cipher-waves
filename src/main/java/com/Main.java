package com;

import com.nn.Network;
import com.nn.NetworkDisplay;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException{
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 800, 600);
        Network network = new Network(0.001, "sigmoid", "quadratic", 784, 30, 10);
        root.getChildren().add(new NetworkDisplay(network));
        stage.setTitle("Neural Network");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}