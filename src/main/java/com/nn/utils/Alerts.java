package com.nn.utils;

import static com.nn.display.NeuralNetworkConfig.FIRST_LAYER_NEURONS;
import static com.nn.display.NeuralNetworkConfig.LAST_LAYER_NEURONS;

import javafx.scene.control.Alert;

public class Alerts {
    private static javafx.scene.control.Alert alert;

    public static void showLastLayerAlert() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid number of neurons at last layer");
        alert.setContentText("The number of neurons at the last layer should be " + LAST_LAYER_NEURONS.get() + " because the" +
                " model is trained to predict " + LAST_LAYER_NEURONS.get() + " numbers");
        alert.showAndWait();
    }

    public static void showFirstLayerAlert() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid number of neurons at first layer");
        alert.setContentText("The number of neurons at the first layer should be " + FIRST_LAYER_NEURONS.get() + " because the" +
                " model is trained to predict " + FIRST_LAYER_NEURONS.get() + " numbers");
        alert.showAndWait();
    }

    public static void showNumberFieldAlert() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty field");
        alert.setContentText("Please enter a number");
        alert.showAndWait();
    }
}
