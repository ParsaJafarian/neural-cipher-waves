package com;

import com.nn.Matrix;
import com.nn.Network;
import com.nn.NetworkDisplay;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        show(stage);
    }

    private static void show(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("network.fxml"));
        String css = Objects.requireNonNull(Main.class.getResource("network.css")).toExternalForm();
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getStylesheets().add(css);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private static void showNetwork(Stage stage){
        Matrix[][] trainData = {
                new Matrix[]{
                        Matrix.random(10, 1),
                        new Matrix(new double[][]{{1}})
                }
        };

        Network network = new Network(0.001, "sigmoid", "mse", 10,5,1);
        network.sgd(trainData,null, 2, 1);

        AnchorPane root = new AnchorPane();
//        NetworkDisplay display = new NetworkDisplay(network);
//        root.getChildren().add(display);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Neural Network");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}