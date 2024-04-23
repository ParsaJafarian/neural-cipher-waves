package com.nn;

import com.nn.utils.DataSection;
import com.nn.algo.Matrix;
import com.nn.algo.NeuralNetwork;
import com.nn.display.NeuralNetworkDisplay;
import com.nn.utils.LossSection;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.nn.utils.Alerts.showFirstLayerAlert;
import static com.nn.utils.Alerts.showLastLayerAlert;
import static com.nn.display.NeuralNetworkConfig.FIRST_LAYER_NEURONS;
import static com.nn.display.NeuralNetworkConfig.LAST_LAYER_NEURONS;

public class Controller {
    public HBox inputSection;
    public ComboBox<Double> learningRateCB;
    public ComboBox<String> activationCB, lossCB;
    public Button trainBtn, clrBtn, stopBtn;
    public Label epochLabel, trainingLossLabel;
    public LineChart<Number, Number> chart;
    public Pane networkContainer;
    public Button layerAdderBtn, layerRemoverBtn;
    public VBox inputDisplay, outputDisplay;
    public HBox inputBtns, outputBtns;
    private NeuralNetwork network;
    private NeuralNetworkDisplay networkDisplay;
    private LossSection lossSection;
    private DataSection dataSection;

    @FXML
    public void initialize() {
        initializeInputSection();

        network = new NeuralNetwork(0.001, FIRST_LAYER_NEURONS.get());
        networkDisplay = new NeuralNetworkDisplay(network, networkContainer);
        lossSection = new LossSection(chart, trainingLossLabel, epochLabel);
        dataSection = new DataSection(inputDisplay, inputBtns, outputDisplay, outputBtns);

        trainBtn.setOnAction(e -> train());
        clrBtn.setOnAction(e -> clear());
        layerAdderBtn.setOnAction(e -> networkDisplay.addLayer());
        layerRemoverBtn.setOnAction(e -> networkDisplay.removeLayer());
    }

    private void train(){
        Matrix[][] trainData = dataSection.getData();
        if (network.getNumNeurons(0) != FIRST_LAYER_NEURONS.get())
            showFirstLayerAlert();
        else if (network.getNumNeurons(-1) != LAST_LAYER_NEURONS.get())
            showLastLayerAlert();
        else
            trainForOneEpoch(trainData);
    }

    private void trainForOneEpoch(Matrix[][] trainData){
        network.setLearningRate(learningRateCB.getValue());
        network.setActivation(activationCB.getValue());
        network.setLoss(lossCB.getValue());

        network.sgd(trainData, trainData, 1, 1);

        double loss = network.evaluate(trainData);
        lossSection.addData(loss);

        networkDisplay.update();
    }

    private void clear(){
        networkDisplay.clear();
        lossSection.clear();
    }

    private void initializeInputSection() {
        learningRateCB.getItems().addAll(0.001, 0.01, 0.1, 1.0);
        learningRateCB.getSelectionModel().select(0);

        activationCB.getItems().addAll("sigmoid", "relu", "tanh");
        activationCB.getSelectionModel().select(0);

        lossCB.getItems().addAll("mse", "mae");
        lossCB.getSelectionModel().select(0);
    }
}