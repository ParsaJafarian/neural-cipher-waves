package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
    private final ArrayList<VBox> layerContainers = new ArrayList<>();

    public NeuralNetworkDisplay(HBox networkContainer) {
        this.networkContainer = networkContainer;
        this.network = new NeuralNetwork(0.01, 10);

        initializeInputLayer();
        initializeHiddenLayers();
    }

    private void initializeInputLayer() {
        VBox inputLayer = new VBox();
        networkContainer.getChildren().add(inputLayer);
        layerContainers.add(inputLayer);
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

        layerContainers.add(layerContainer);
        networkContainer.getChildren().add(layerContainer);

        int lastLayerIndex = network.getNumLayers() - 1;
        Matrix lastActivations = network.getActivationsAtLayer(lastLayerIndex);

        for (int neuronIndex = 0; neuronIndex < numberOfNeurons; neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(layerContainer, activation);
            if (lastLayerIndex >= 2)
                generateWeights(neuronIndex, lastLayerIndex);
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
        int prevNumNeurons = network.getNumNeurons(currLayerIndex - 1);
        for (int prevNeuronIndex = 0; prevNeuronIndex < prevNumNeurons; prevNeuronIndex++) {
            connectNeurons(currNeuronIndex, prevNeuronIndex, currLayerIndex);
        }
    }

    /**
     * Connect two neurons with a weighted line
     * @param currNeuronIndex index of the current neuron
     * @param prevNeuronIndex index of the previous neuron
     * @param currLayerIndex index of the current layer
     */
    private void connectNeurons(int currNeuronIndex, int prevNeuronIndex, int currLayerIndex){
        double weight = Math.abs(network.getWeightsAtLayer(currLayerIndex).get(currNeuronIndex, prevNeuronIndex));
        Line line = new Line();
        VBox layerContainer = getLayerContainer(currLayerIndex);
        StackPane stackPane = (StackPane) networkContainer.getParent();

        SimpleDoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);

        Circle currNeuron = getNeuron(currLayerIndex, currNeuronIndex);
        Circle prevNeuron = getNeuron(currLayerIndex - 1, prevNeuronIndex);

        Point2D currNeuronCoordinates = getNeuronStackPaneCoordinates(currNeuron, stackPane);
        Point2D prevNeuronCoordinates = getNeuronStackPaneCoordinates(prevNeuron, stackPane);

        line.setStartX(prevNeuronCoordinates.getX());
        line.setStartY(prevNeuronCoordinates.getY());

        line.setEndX(currNeuronCoordinates.getX());
        line.setEndY(currNeuronCoordinates.getY());

        // Set line appearance properties
        line.opacityProperty().bind(value.divide(2).add(0.5));
        line.strokeWidthProperty().bind(value.divide(1.5).add(0.5).multiply(3));

        // Add line to the container
        lineWeights.add(line);
        stackPane.getChildren().add(line);
    }

    private Point2D getNeuronStackPaneCoordinates(Circle neuron, StackPane stackPane){
        // Get the local coordinates of the circle in terms of the VBox
        double neuronLocalX = neuron.getBoundsInLocal().getMinX();
        double neuronLocalY = neuron.getBoundsInLocal().getMinY();

        // Convert local coordinates to scene coordinates
        double neuronSceneX = neuron.localToScene(neuronLocalX, neuronLocalY).getX();
        double neuronSceneY = neuron.localToScene(neuronLocalX, neuronLocalY).getY();

        // Convert scene coordinates to coordinates relative to the StackPane
        double neuronStackPaneX = stackPane.sceneToLocal(neuronSceneX, neuronSceneY).getX();
        double neuronStackPaneY = stackPane.sceneToLocal(neuronSceneX, neuronSceneY).getY();

        return new Point2D(neuronStackPaneX, neuronStackPaneY);
    }

    private Circle getNeuron(int layerIndex, int neuronIndex){
        VBox layerContainer = getLayerContainer(layerIndex);
        StackPane neuronPane = (StackPane) layerContainer.getChildren().get(neuronIndex + 1);
        return (Circle) neuronPane.getChildren().get(0);
    }

    private VBox getLayerContainer(int layerIndex){
        return layerContainers.get(layerIndex);
    }

    public void removeLayer(){
        if (network.getNumLayers() <= MIN_LAYERS + 1) return;
        int lastIndex = network.getNumLayers() - 1;
        network.removeLayer();
        VBox layerContainer = getLayerContainer(lastIndex);
        layerContainers.remove(layerContainer);
        networkContainer.getChildren().remove(layerContainer);
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
        Matrix activation = network.getActivationsAtLayer(layerIndex);
        double activationValue = activation.get(activation.getRows() - 1, 0);
        addNeuron(layerContainer, activationValue);
    }

    private int getLayerIndex(VBox layerContainer) {
        return layerContainers.indexOf(layerContainer);
    }

    public void clear() {
        network.clear();
        update();
    }

    public void update() {
        for (int layerIndex = 0; layerIndex < network.getNumLayers(); layerIndex++){
            Matrix activations = network.getActivationsAtLayer(layerIndex);

            for (int neuronIndex = 0; neuronIndex < activations.getRows(); neuronIndex++){
                Circle neuron = getNeuron(layerIndex, neuronIndex);
                SimpleDoubleProperty activationProp = ((SimpleDoubleProperty) neuron.getUserData());

                double activation = activations.get(neuronIndex, 0);
                activationProp.set(activation);
            }
        }
    }
}

