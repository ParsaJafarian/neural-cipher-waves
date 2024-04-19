package com;

import com.nn.Matrix;
import com.nn.Mnist;
import com.nn.NeuralNetwork;
import com.nn.display.NeuralNetworkDisplay;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static com.nn.display.NeuralNetworkConfig.FIRST_LAYER_NEURONS;
import static com.nn.display.NeuralNetworkConfig.LAST_LAYER_NEURONS;

public class Controller {
    public HBox inputSection;
    public ComboBox<Double> learningRateCB;
    public ComboBox<String> activationCB, lossCB;
    public Slider batchSlider;
    public Button trainBtn, clrBtn, stopBtn;
    public Label testLossLabel, trainingLossLabel;
    public LineChart<Integer, Double> trainingChart;
    public Pane networkContainer;
    public Button layerAdderBtn, layerRemoverBtn;
    private NeuralNetwork network;
    private NeuralNetworkDisplay networkDisplay;
    private boolean isTraining = false;
    Matrix[][] trainData = new Matrix[][]{
            new Matrix[]{
                    Matrix.random(10, 1),
                    new Matrix(new double[][]{{1}, {1}})
            }
    };

    @FXML
    public void initialize() throws IOException {
        learningRateCB.getItems().addAll(0.001, 0.01, 0.1, 1.0);
        learningRateCB.getSelectionModel().select(0);

        activationCB.getItems().addAll("sigmoid", "relu", "tanh");
        activationCB.getSelectionModel().select(0);

        lossCB.getItems().addAll("mse", "mae");
        lossCB.getSelectionModel().select(0);

        batchSlider.setMin(1);
        batchSlider.setValue(10);

        trainBtn.setText("Train");

        Matrix[][] trainData = new Matrix[][]{
                new Matrix[]{
                        new Matrix(new double[][]{{1}, {2}, {3}, {4}, {5}}),
                        new Matrix(new double[][]{{1}, {4}, {9}, {16}, {25}})
                }
        };

        network = new NeuralNetwork(0.001, "sigmoid", "mse", FIRST_LAYER_NEURONS);
        networkDisplay = new NeuralNetworkDisplay(network, networkContainer);


        trainBtn.setOnAction(e -> {
            if (!isTraining) {
                isTraining = true;
                train();
            }
        });
        stopBtn.setOnAction(e -> {
            clrBtn.fire();
            isTraining = false;
        });

        clrBtn.setOnAction(e -> networkDisplay.clear());

        layerAdderBtn.setOnAction(e -> networkDisplay.addLayer());
        layerRemoverBtn.setOnAction(e -> networkDisplay.removeLayer());
    }

    private void train(){
        new Thread(() -> {
            while (isTraining) {
                try {
                    network.setLearningRate(learningRateCB.getValue());
                    network.setActivation(activationCB.getValue());
                    network.setLoss(lossCB.getValue());
                    int miniBatchSize = (int) batchSlider.getValue();

                    network.sgd(trainData, null, 1, miniBatchSize);

                    networkDisplay.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}