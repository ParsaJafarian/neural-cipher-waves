package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.util.ArrayList;

import static com.nn.NeuralNetworkConfig.*;

/**
 * This class consists of the visible neural display accesible in Simulation
 */
public class NeuralNetworkDisplay {

    private final Pane networkContainer;
    private final NeuralNetwork network;
    private final ArrayList<ArrayList<Neuron>> layers = new ArrayList<>();
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
            addLayer();
    }

    public void addLayer() {
        if (network.getNumLayers() > MAX_LAYERS) return;

        network.addLayer(MIN_NEURONS);
        layers.add(new ArrayList<>());
        addLayerButtons();

        int lastLayerIndex = network.getNumLayers() - 1;
        Matrix lastActivations = network.getActivationsAtLayer(lastLayerIndex);

        for (int neuronIndex = 0; neuronIndex < MIN_NEURONS; neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(lastLayerIndex, activation);
        }

        if (lastLayerIndex >= 2)
            generateLayerWeights(lastLayerIndex);
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
        btnContainers.add(btnContainer);
    }

    /**
     * Generate weights between last layer and the before last layer
     */
    private void generateLayerWeights(int currLayerIndex) {
        int currNumNeurons = network.getNumNeurons(currLayerIndex);
        for (int currNeuronIndex = 0; currNeuronIndex < currNumNeurons; currNeuronIndex++) {
            generateInputWeights(currNeuronIndex, currLayerIndex);
            generateOutputWeights(currNeuronIndex, currLayerIndex);
        }
    }

    /**
     * Generate weight lines between current neuron and all neurons of previous layer
     *
     * @param currNeuronIndex index of the current neuron
     * @param currLayerIndex  index of the current layer
     */
    private void generateInputWeights(int currNeuronIndex, int currLayerIndex) {
        if (currLayerIndex <= 1) return;

        int prevLayerIndex = currLayerIndex - 1;
        int prevNumNeurons = network.getNumNeurons(prevLayerIndex);
        for (int prevNeuronIndex = 0; prevNeuronIndex < prevNumNeurons; prevNeuronIndex++)
            connectNeurons(currNeuronIndex, prevNeuronIndex, currLayerIndex);
    }

    private void generateOutputWeights(int currNeuronIndex, int currLayerIndex) {
        if (currLayerIndex == network.getNumLayers() - 1) return;

        int nextLayerIndex = currLayerIndex + 1;
        int nextNumNeurons = network.getNumNeurons(nextLayerIndex);

        for (int nextNeuronIndex = 0; nextNeuronIndex < nextNumNeurons; nextNeuronIndex++)
            connectNeurons(nextNeuronIndex, currNeuronIndex, nextLayerIndex);
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
        line.setId("weightLine");
        line.toBack();

        SimpleDoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);

        Neuron currNeuron = getNeuron(currLayerIndex, currNeuronIndex);
        Neuron prevNeuron = getNeuron(currLayerIndex - 1, prevNeuronIndex);

        line.startXProperty().bind(prevNeuron.outputXProperty());
        line.startYProperty().bind(prevNeuron.outputYProperty());

        line.endXProperty().bind(currNeuron.inputXProperty());
        line.endYProperty().bind(currNeuron.inputYProperty());

        networkContainer.getChildren().add(line);
    }

    private Neuron getNeuron(int layerIndex, int neuronIndex) {
        return layers.get(layerIndex).get(neuronIndex);
    }

    private ArrayList<Neuron> getLastLayer() {
        return layers.get(network.getNumLayers() - 1);
    }

    public void removeLayer() {
        if (network.getNumLayers() <= MIN_LAYERS + 1) return;
        int lastIndex = network.getNumLayers() - 1;
        network.removeLayer();
        layers.remove(lastIndex);

        networkContainer.getChildren().remove(btnContainers.get(lastIndex));
        for (Neuron neuron : getLastLayer())
            networkContainer.getChildren().remove(neuron);
    }

    public void addNeuron(int layerIndex, double activation) {
        if (layers.get(layerIndex).size() >= MAX_NEURONS) return;

        int lastNeuronIndex = layers.get(layerIndex).size() + 1;

        Neuron neuron = new Neuron(activation);
        neuron.setTranslateX(getLayerSpacing(layerIndex));
        neuron.setTranslateY(getNeuronSpacing(lastNeuronIndex));

        layers.get(layerIndex).add(neuron);
        networkContainer.getChildren().add(neuron);


        generateInputWeights(lastNeuronIndex - 1, layerIndex);
        generateOutputWeights(lastNeuronIndex - 1, layerIndex);

    }

    public void removeNeuron(int layerIndex) {
        if (network.getNumNeurons(layerIndex) <= MIN_NEURONS + 1) return;
        network.removeNeuron(layerIndex);

        int lastNeuronIndex = network.getNumNeurons(layerIndex) - 1;
        Neuron neuron = getNeuron(layerIndex, lastNeuronIndex);
        layers.get(layerIndex).remove(neuron);
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
                Neuron neuron = getNeuron(layerIndex, neuronIndex);
                SimpleDoubleProperty activationProp = ((SimpleDoubleProperty) neuron.getUserData());

                double activation = activations.get(neuronIndex, 0);
                activationProp.set(activation);
            }
        }
    }
}

