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

import static com.SceneSwitcher.switchToScene;
import static com.nn.display.NeuralNetworkConfig.FIRST_LAYER_NEURONS;
import static com.nn.display.NeuralNetworkConfig.LAST_LAYER_NEURONS;
import static com.nn.utils.Alerts.*;

public class Controller {
    public HBox inputSection;
    public ComboBox<Double> learningRateCB;
    public ComboBox<String> activationCB, lossCB;
    public Button trainBtn, clrBtn;
    public Label epochLabel, trainingLossLabel;
    public LineChart<Number, Number> chart;
    public Pane networkContainer;
    public Button layerAdderBtn, layerRemoverBtn;
    public VBox inputDisplay, outputDisplay;
    public HBox inputBtns, outputBtns;
    public Button backBtn;
    public MenuItem aboutMenuItem;
    private NeuralNetwork network;
    private NeuralNetworkDisplay networkDisplay;
    private LossSection lossSection;
    private DataSection dataSection;

    @FXML
    public void initialize() {
        initializeInputSection();

        //menu item
        aboutMenuItem.setOnAction(e -> showAboutAlert());

        network = new NeuralNetwork(0.001, FIRST_LAYER_NEURONS.get());
        networkDisplay = new NeuralNetworkDisplay(network, networkContainer);
        lossSection = new LossSection(chart, trainingLossLabel, epochLabel);
        dataSection = new DataSection(inputDisplay, inputBtns, outputDisplay, outputBtns);

        trainBtn.setOnAction(e -> train());
        clrBtn.setOnAction(e -> clear());
        layerAdderBtn.setOnAction(e -> networkDisplay.addLayer());
        layerRemoverBtn.setOnAction(e -> networkDisplay.removeLayer());
        backBtn.setOnAction(e -> switchToScene(backBtn, "selection-menu.fxml"));
    }

    /**
     * If the number of neurons at the first layer is not equal to the number of input fields, show an alert
     * If the number of neurons at the last layer is not equal to the number of output fields, show an alert
     * Otherwise, train the model
     */
    private void train() {
        Matrix[][] trainData = dataSection.getData();
        if (network.getNumNeurons(0) != FIRST_LAYER_NEURONS.get())
            showFirstLayerAlert();
        else if (network.getNumNeurons(-1) != LAST_LAYER_NEURONS.get())
            showLastLayerAlert();
        else
            trainForOneEpoch(trainData);
    }

    /**
     * Train the model and update the display for one epoch
     *
     * @param trainData The training data
     */
    private void trainForOneEpoch(Matrix[][] trainData) {
        network.setLearningRate(learningRateCB.getValue());
        network.setActivation(activationCB.getValue());
        network.setLoss(lossCB.getValue());

        network.sgd(trainData, trainData, 1, 1);

        double loss = network.evaluate(trainData);
        lossSection.addData(loss);

        networkDisplay.update();
    }

    /**
     * Clear the display
     */
    private void clear() {
        networkDisplay.clear();
        lossSection.clear();
    }

    /**
     * Initialize the input section with default values
     */
    private void initializeInputSection() {
        learningRateCB.getItems().addAll(0.001, 0.01, 0.1, 1.0);
        learningRateCB.getSelectionModel().select(0);

        activationCB.getItems().addAll("sigmoid", "relu", "tanh");
        activationCB.getSelectionModel().select(0);

        lossCB.getItems().addAll("mse", "mae");
        lossCB.getSelectionModel().select(0);
    }
}