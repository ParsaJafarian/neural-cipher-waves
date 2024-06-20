package com.nn.utils;

import static com.nn.display.NeuralNetworkConfig.FIRST_LAYER_NEURONS;
import static com.nn.display.NeuralNetworkConfig.LAST_LAYER_NEURONS;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying alerts
 */
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

    public static void showAboutAlert() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Neural Networks");
        alert.setHeaderText("Neural Networks");
        alert.setContentText("""
                A neural network is a series of algorithms that endeavors to recognize underlying relationships \
                in a set of data through a process that mimics the way the human brain operates. \
                Neural networks can adapt to changing input; so the network generates the best possible result without\
                 needing to redesign the output criteria.\

                To run this program, select the number of neurons and layers. Play around with the activation functions and \
                loss functions to see how they affect the training process. \
                Write your own inputs and outputs and see how the model predicts the output.\s""");

        alert.showAndWait();
    }
}
