package com;

import com.data.DataSection;
import com.nn.Matrix;
import com.nn.NeuralNetwork;
import com.nn.display.NeuralNetworkDisplay;
import com.nn.display.LossSection;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.atomic.AtomicInteger;

import static com.Alerts.showFirstLayerAlert;
import static com.Alerts.showLastLayerAlert;
import static com.nn.display.NeuralNetworkConfig.FIRST_LAYER_NEURONS;
import static com.nn.display.NeuralNetworkConfig.LAST_LAYER_NEURONS;

public class Controller {
    public HBox inputSection;
    public ComboBox<Double> learningRateCB;
    public ComboBox<String> activationCB, lossCB;
    public Slider batchSlider;
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
    private boolean isTraining = false;

    @FXML
    public void initialize() {
        initializeInputSection();

        network = new NeuralNetwork(0.001, FIRST_LAYER_NEURONS.get());
        networkDisplay = new NeuralNetworkDisplay(network, networkContainer);
        lossSection = new LossSection(chart, trainingLossLabel, epochLabel);
        dataSection = new DataSection(inputDisplay, inputBtns, outputDisplay, outputBtns);

        trainBtn.setOnAction(e -> {
            Matrix[][] trainData = dataSection.getData();
            if (network.getNumNeurons(0) != FIRST_LAYER_NEURONS.get())
                showFirstLayerAlert();
            else if (network.getNumNeurons(-1) != LAST_LAYER_NEURONS.get())
                showLastLayerAlert();
            else
                trainNetworkForOneEpoch(trainData);
        });
        clrBtn.setOnAction(e -> clear());
        layerAdderBtn.setOnAction(e -> networkDisplay.addLayer());
        layerRemoverBtn.setOnAction(e -> networkDisplay.removeLayer());
    }

    private void trainNetworkForOneEpoch(Matrix[][] trainData){
        network.setLearningRate(learningRateCB.getValue());
        network.setActivation(activationCB.getValue());
        network.setLoss(lossCB.getValue());
        int miniBatchSize = (int) batchSlider.getValue();

        network.sgd(trainData, trainData, 1, miniBatchSize);

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

        batchSlider.setValue(1);
    }

    private void train(){
        new Thread(() -> {
            while (isTraining) {
                try {
                    network.setLearningRate(learningRateCB.getValue());
                    network.setActivation(activationCB.getValue());
                    network.setLoss(lossCB.getValue());
                    int miniBatchSize = (int) batchSlider.getValue();

                    Matrix[][] trainData = dataSection.getData();

                    network.sgd(trainData, null, 1, miniBatchSize);

                    networkDisplay.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}