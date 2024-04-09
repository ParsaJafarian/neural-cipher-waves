package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;

import static com.nn.NeuralNetworkConfig.*;

/**
 * This class consists of the visible neural display accesible in Simulation
 */
public class NeuralNetworkDisplay {

    private final HBox networkContainer;
    private final NeuralNetwork network;
    private final ArrayList<Line> lineWeights = new ArrayList<>();
    private final ArrayList<ArrayList<Circle>> layers;
    private final ArrayList<Matrix> activations;

    public NeuralNetworkDisplay(HBox networkContainer) {
        this.networkContainer = networkContainer;
        this.layers = new ArrayList<>();
        this.network = new NeuralNetwork(0.01, 10);
        this.activations = network.getActivations();

        initializeInputLayer();
        initializeHiddenLayers();
    }

    private void initializeInputLayer() {
        VBox inputLayer = new VBox();
        networkContainer.getChildren().add(inputLayer);
        layers.add(new ArrayList<>());
    }

    private void initializeHiddenLayers() {
        for (int i = 0; i < MIN_LAYERS; i++)
            addLayer(MIN_NEURONS);
    }

    public void addLayer(int numberOfNeurons) {
        if (network.getNumLayers() > MAX_LAYERS) return;

        network.addLayer(MIN_NEURONS);

        VBox layerContainer = new VBox();
        layerContainer.setAlignment(Pos.TOP_CENTER);
        layerContainer.setSpacing(5);
        addLayerButtons(layerContainer);

        networkContainer.getChildren().add(layerContainer);

        Matrix lastActivation = activations.get(activations.size() - 1);

        for (int neuronIndex = 0; neuronIndex < numberOfNeurons; neuronIndex++) {
            double activation = lastActivation.get(neuronIndex, 0);
            addNeuron(layerContainer, activation);
        }
    }

    /**
     * Generate weights between last layer and the before last layer
     */
    private void generateLayerWeights() {
        int lastIndex = network.getNumLayers() - 1;
        int currNumNeurons = network.getNumNeurons(lastIndex);
        for (int currNeuronIndex = 0; currNeuronIndex < currNumNeurons; currNeuronIndex++) {
            generateWeights(currNeuronIndex, lastIndex);
        }
    }

    /**
     * Generate weight lines between current neuron and all neurons of previous layer
     * @param currNeuronIndex index of the current neuron
     * @param currLayerIndex  index of the current layer
     */
    private void generateWeights(int currNeuronIndex, int currLayerIndex) {
        int prevNumNeurons = activations.get(currLayerIndex - 1).getRows();
        for (int prevNeuronIndex = 0; prevNeuronIndex < prevNumNeurons; prevNeuronIndex++) {
            connectNeurons(currNeuronIndex, prevNeuronIndex, currLayerIndex);
        }
    }

    /**
     * Connects two neurons with a weighted line
     * @param currNeuronIndex index of the current neuron
     * @param prevNeuronIndex index of the previous neuron
     * @param currLayerIndex index of the current layer
     */
    private void connectNeurons(int currNeuronIndex, int prevNeuronIndex, int currLayerIndex){
        double weight = Math.abs(network.getWeights().get(currLayerIndex).get(currNeuronIndex, prevNeuronIndex));
        Line line = new Line();
        VBox layer = (VBox) networkContainer.getChildren().get(currLayerIndex);

        SimpleDoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);

        Circle currNeuron = layers.get(currLayerIndex).get(currNeuronIndex);
        Circle prevNeuron = layers.get(currLayerIndex - 1).get(prevNeuronIndex);

        // Calculate the center positions for the current and previous neurons
        double currNeuronCenterX = currNeuron.getBoundsInParent().getCenterX();
        double prevNeuronCenterX = prevNeuron.getBoundsInParent().getCenterX();

        // Bind line coordinates to neuron centers accounting for VBox layout
        line.startXProperty().bind(currNeuron.parentProperty().get().layoutXProperty().add(currNeuronCenterX));
        line.endXProperty().bind(prevNeuron.parentProperty().get().layoutXProperty().add(prevNeuronCenterX));

        // Bind line coordinates to neuron centers accounting for HBox
        line.startYProperty().bind(
                currNeuron.layoutYProperty().add(currNeuron.getTranslateY()).add(currNeuron.getRadius()).add(layer.layoutYProperty())
        );
        line.endYProperty().bind(
                prevNeuron.layoutYProperty().add(prevNeuron.getTranslateY()).add(prevNeuron.getRadius()).add(layer.layoutYProperty())
        );

        // Set line appearance properties
        line.opacityProperty().bind(value.divide(2).add(0.5));
        line.strokeWidthProperty().bind(value.divide(1.5).add(0.5).multiply(3));
        line.toBack();
        line.setManaged(false);

        // Add line to the container
        networkContainer.getChildren().add(line);
    }

    public void removeLayer(){
        if (networkContainer.getChildren().size() <= MIN_LAYERS + 1) return;
        network.removeLayer();
        networkContainer.getChildren().remove(networkContainer.getChildren().size() - 1);
    }

    public void addNeuron(@NotNull VBox layerContainer, double activation){
        if (layerContainer.getChildren().size() > MAX_NEURONS) return;

        StackPane neuronPane = new StackPane();

        Label value = new Label();
        value.toFront();
        value.setId("activationValue");

        DoubleProperty prop = new SimpleDoubleProperty(activation);
        value.textProperty().bind(prop.asString("%.2f"));

        Circle neuron = new Circle(20);
        neuron.setUserData(prop);

        neuronPane.getChildren().add(neuron);
        neuronPane.getChildren().add(value);

        layerContainer.getChildren().add(neuronPane);
    }

    public void removeNeuron(@NotNull VBox layerContainer){
        if (layerContainer.getChildren().size() <= MIN_NEURONS + 1) return;
        network.removeNeuron(getLayerIndex(layerContainer));
        layerContainer.getChildren().remove(layerContainer.getChildren().size() - 1);
    }

    /**
     * Add buttons to add or remove neurons from the layer
     * @param layerContainer container to add the buttons to
     */
    private void addLayerButtons(@NotNull VBox layerContainer) {
        HBox btnContainer = new HBox();
        btnContainer.setSpacing(5);

        Button addNeuronBtn = new Button("+");
        Button removeNeuronBtn = new Button("-");

        addNeuronBtn.setOnAction(e -> addNeuronThroughBtn(layerContainer));
        removeNeuronBtn.setOnAction(e -> removeNeuron(layerContainer));

        btnContainer.getChildren().add(addNeuronBtn);
        btnContainer.getChildren().add(removeNeuronBtn);

        layerContainer.getChildren().add(btnContainer);
    }

    private void addNeuronThroughBtn(VBox layerContainer){
        int layerIndex = getLayerIndex(layerContainer);
        network.addNeuron(layerIndex);
        Matrix activation = activations.get(layerIndex);
        double activationValue = activation.get(activation.getRows() - 1, 0);
        addNeuron(layerContainer, activationValue);
    }

    private int getLayerIndex(VBox layerContainer) {
        return networkContainer.getChildren().indexOf(layerContainer) - 1;
    }

    public void clear() {
        network.clear();
        update();
    }

    public void update() {
        for (int i = 0; i < activations.size(); i++)
            for (int j = 0; j < activations.get(i).getRows(); j++)
                ((SimpleDoubleProperty) layers.get(i).get(j).getUserData()).set(activations.get(i).get(j, 0));
    }
}

