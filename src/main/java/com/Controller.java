package com;

import com.nn.NeuralNetwork;
import com.nn.NeuralNetworkDisplay;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import static com.nn.NeuralNetworkConfig.MIN_NEURONS;

public class Controller {
    public HBox inputSection;
    public ComboBox<Double> learningRateCB;
    public ComboBox<String> activationCB, lossCB;
    public Slider batchSlider;
    public Button startStopBtn, clrBtn;
    public Label testLossLabel, trainingLossLabel;
    public LineChart<Integer, Double> trainingChart;
    public Canvas canvas;
    public Pane networkContainer;
    public Button btnAdderBtn, btnRemoverBtn;
    private NeuralNetwork network;
    private NeuralNetworkDisplay networkDisplay;

    @FXML
    public void initialize() {
        learningRateCB.getItems().addAll(0.001, 0.01, 0.1, 1.0);
        learningRateCB.getSelectionModel().select(0);

        activationCB.getItems().addAll("sigmoid", "relu", "tanh");
        activationCB.getSelectionModel().select(0);

        lossCB.getItems().addAll("mse", "mae");
        lossCB.getSelectionModel().select(0);

        startStopBtn.setText("Train");

//        Matrix[][] trainData = new Matrix[][]{
//                new Matrix[]{
//                        Matrix.random(4, 1),
//                        new Matrix(new double[][]{{1}})
//                }
//        };

        network = new NeuralNetwork(learningRateCB.getValue(), activationCB.getValue(), lossCB.getValue(), 4,6,1);
        networkDisplay = new NeuralNetworkDisplay(networkContainer);

        startStopBtn.setOnAction(e -> {
            network.setLearningRate(learningRateCB.getValue());
            network.setActivation(activationCB.getValue());
            network.setLoss(lossCB.getValue());

//            network.sgd(trainData, null, 2, 1);
            //update display
            networkDisplay.update();
        });

        clrBtn.setOnAction(e -> networkDisplay.clear());

        btnAdderBtn.setOnAction(e -> networkDisplay.addLayer(MIN_NEURONS));
        btnRemoverBtn.setOnAction(e -> networkDisplay.removeLayer());

//        btnRemoverBtn.setOnAction(e -> {
//            if (btnContainer.getChildren().isEmpty()) return;
//            int size = btnContainer.getChildren().size();
//            btnContainer.getChildren().remove(size - 1 );
//        });
    }
}