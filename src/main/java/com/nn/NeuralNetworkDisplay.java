package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;

import static com.nn.NeuralNetworkConfig.*;

/**
 * This class consists of the visible neural display accesible in Simulation
 */
public class NeuralNetworkDisplay {

    private final Pane networkContainer;
    private final NeuralNetwork network;
    private final ArrayList<ArrayList<Circle>> layers = new ArrayList<>();
    private final ArrayList<HBox> btnContainers = new ArrayList<>();

    public NeuralNetworkDisplay(Pane networkContainer) {
        this.networkContainer = networkContainer;
        this.network = new NeuralNetwork(0.01, 10);

        initializeInputLayer();
        initializeHiddenLayers();
    }

    private void initializeInputLayer() {
        layers.add(new ArrayList<>());
        networkContainer.getChildren().add(new Pane());
    }

    private void initializeHiddenLayers() {
        for (int i = 0; i < MIN_LAYERS; i++)
            addLayer(MIN_NEURONS);
    }

    public void addLayer(int numberOfNeurons) {
        if (network.getNumLayers() > MAX_LAYERS) return;

        network.addLayer(MIN_NEURONS);
        layers.add(new ArrayList<>());
        addLayerButtons();

        int lastLayerIndex = network.getNumLayers() - 1;
        Matrix lastActivations = network.getActivationsAtLayer(lastLayerIndex);

        for (int neuronIndex = 0; neuronIndex < numberOfNeurons; neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(lastLayerIndex, activation);
            if (lastLayerIndex >= 2)
                generateWeights(neuronIndex, lastLayerIndex);
        }
    }

    /**
     * Add buttons to add or remove neurons from the layer
     */
    private void addLayerButtons() {
        int lastLayerIndex = network.getNumLayers() - 1;

        HBox btnContainer = new HBox();
        btnContainer.setSpacing(5);
        btnContainer.toFront();
        btnContainer.setTranslateX(getLayerSpacing(lastLayerIndex));

        Button addNeuronBtn = new Button("+");
        Button removeNeuronBtn = new Button("-");

        addNeuronBtn.setOnAction(e -> addNeuronThroughBtn(lastLayerIndex));
        removeNeuronBtn.setOnAction(e -> removeNeuron(lastLayerIndex));

        btnContainer.getChildren().add(addNeuronBtn);
        btnContainer.getChildren().add(removeNeuronBtn);

        networkContainer.getChildren().add(btnContainer);
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
     *
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
     *
     * @param currNeuronIndex index of the current neuron
     * @param prevNeuronIndex index of the previous neuron
     * @param currLayerIndex  index of the current layer
     */
    private void connectNeurons(int currNeuronIndex, int prevNeuronIndex, int currLayerIndex) {
        double weight = Math.abs(network.getWeightsAtLayer(currLayerIndex).get(currNeuronIndex, prevNeuronIndex));
        Line line = new Line();

        SimpleDoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);

        Circle currNeuron = getNeuron(currLayerIndex, currNeuronIndex);
        Circle prevNeuron = getNeuron(currLayerIndex - 1, prevNeuronIndex);

        setLineCoordinates(line, currNeuron, prevNeuron);

        // Set line appearance properties
        line.opacityProperty().bind(value.divide(2).add(0.5));
        line.strokeWidthProperty().bind(value.divide(1.5).add(0.5).multiply(3));

        // Add line to the container
        networkContainer.getChildren().add(line);
    }

    private void setLineCoordinates(@NotNull Line line, @NotNull Circle neuron1, @NotNull Circle neuron2) {
        Point2D start = neuron1.localToScene(neuron1.getCenterX(), neuron1.getCenterY());
        Point2D end = neuron2.localToScene(neuron2.getCenterX(), neuron2.getCenterY());

        line.setStartX(start.getX());
        line.setStartY(start.getY());
        line.setEndX(end.getX());
        line.setEndY(end.getY());
    }

    private Circle getNeuron(int layerIndex, int neuronIndex) {
        return layers.get(layerIndex).get(neuronIndex);
    }

    private ArrayList<Circle> getLastLayer() {
        return layers.get(network.getNumLayers() - 1);
    }

    public void removeLayer() {
        if (network.getNumLayers() <= MIN_LAYERS + 1) return;
        int lastIndex = network.getNumLayers() - 1;
        network.removeLayer();
        layers.remove(lastIndex);

        networkContainer.getChildren().remove(btnContainers.get(lastIndex));
        for (Circle neuron : getLastLayer())
            networkContainer.getChildren().remove(neuron);
    }

    public void addNeuron(int layerIndex, double activation) {
        if (network.getNumNeurons(layerIndex) > MAX_NEURONS) return;

        Label value = new Label();
        value.toFront();
        value.setId("activationValue");

        DoubleProperty prop = new SimpleDoubleProperty(activation);
        value.textProperty().bind(prop.asString("%.2f"));

        Circle neuron = new Circle(20);
        neuron.setUserData(prop);

        int lastNeuronIndex = network.getNumNeurons(layerIndex) - 1;

        neuron.setTranslateX(getLayerSpacing(layerIndex));
        neuron.setTranslateY(getNeuronSpacing(lastNeuronIndex));

        value.setTranslateX(getLayerSpacing(layerIndex));
        value.setTranslateY(getNeuronSpacing(lastNeuronIndex));

        layers.get(layerIndex).add(neuron);
        networkContainer.getChildren().add(neuron);
    }

    public void removeNeuron(int layerIndex) {
        if (network.getNumNeurons(layerIndex) <= MIN_NEURONS + 1) return;
        network.removeNeuron(layerIndex);

        int lastNeuronIndex = network.getNumNeurons(layerIndex) - 1;
        Circle neuron = getNeuron(layerIndex, lastNeuronIndex);
        layers.get(layerIndex).remove(neuron);
    }

    private int getLayerSpacing(int layerIndex) {
        return 50 * layerIndex;
    }

    private int getNeuronSpacing(int neuronIndex) {
        return 50 * neuronIndex;
    }

    private void addNeuronThroughBtn(int layerIndex) {
        network.addNeuron(layerIndex);
        Matrix activation = network.getActivationsAtLayer(layerIndex);
        double activationValue = activation.get(activation.getRows() - 1, 0);
        addNeuron(layerIndex, activationValue);
    }

    public void clear() {
        network.clear();
        update();
    }

    public void update() {
        for (int layerIndex = 0; layerIndex < network.getNumLayers(); layerIndex++) {
            Matrix activations = network.getActivationsAtLayer(layerIndex);

            for (int neuronIndex = 0; neuronIndex < activations.getRows(); neuronIndex++) {
                Circle neuron = getNeuron(layerIndex, neuronIndex);
                SimpleDoubleProperty activationProp = ((SimpleDoubleProperty) neuron.getUserData());

                double activation = activations.get(neuronIndex, 0);
                activationProp.set(activation);
            }
        }
    }
}

