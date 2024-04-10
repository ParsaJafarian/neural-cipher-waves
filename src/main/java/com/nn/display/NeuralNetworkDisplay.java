package com.nn.display;

import com.nn.Matrix;
import com.nn.NeuralNetwork;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.ArrayList;

import static com.nn.display.NeuralNetworkConfig.*;

/**
 * This class consists of the visible neural display accessible in Simulation
 */
public class NeuralNetworkDisplay {

    private final Pane networkContainer;
    private final NeuralNetwork network;
    private final ArrayList<ArrayList<Neuron>> layers = new ArrayList<>();
    private final ArrayList<HBox> btnContainers = new ArrayList<>();
    private final WeightsGenerator weightsGenerator;

    public NeuralNetworkDisplay(Pane networkContainer) {
        this.networkContainer = networkContainer;
        this.network = new NeuralNetwork(0.01, 10);
        this.weightsGenerator = new WeightsGenerator(networkContainer, network, layers);

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

        weightsGenerator.generateLayerWeights(lastLayerIndex);
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

    private Neuron getNeuron(int layerIndex, int neuronIndex) {
        return layers.get(layerIndex).get(neuronIndex);
    }

    public void removeLayer() {
        if (network.getNumLayers() <= MIN_LAYERS + 1) return;
        int lastIndex = network.getNumLayers() - 1;

        network.removeLayer();
        networkContainer.getChildren().remove(btnContainers.get(lastIndex - 1));

        ArrayList<Neuron> lastLayer = layers.get(lastIndex);

        for (Neuron neuron : lastLayer)
            networkContainer.getChildren().remove(neuron);
        layers.remove(lastIndex);
    }

    public void addNeuron(int layerIndex, double activation) {
        if (layers.get(layerIndex).size() >= MAX_NEURONS) return;

        int lastNeuronIndex = layers.get(layerIndex).size();

        Neuron neuron = new Neuron(activation);
        neuron.setTranslateX(getLayerSpacing(layerIndex));
        neuron.setTranslateY(getNeuronSpacing(lastNeuronIndex + 1));

        layers.get(layerIndex).add(neuron);
        networkContainer.getChildren().add(neuron);

        weightsGenerator.generateWeights(lastNeuronIndex, layerIndex);
    }

    public void removeNeuron(int layerIndex) {
        if (network.getNumNeurons(layerIndex) <= MIN_NEURONS) return;
        int lastNeuronIndex = network.getNumNeurons(layerIndex) - 1;

        network.removeNeuron(layerIndex);

        Neuron neuron = getNeuron(layerIndex, lastNeuronIndex);
        layers.get(layerIndex).remove(neuron);
        networkContainer.getChildren().remove(neuron);
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

