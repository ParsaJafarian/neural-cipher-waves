package com.nn.display;

import com.nn.algo.Matrix;
import com.nn.algo.NeuralNetwork;
import com.nn.utils.ButtonContainer;
import javafx.beans.property.SimpleDoubleProperty;
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
    private final ArrayList<ButtonContainer> btnContainers = new ArrayList<>();
    private final WeightsGenerator weightsGenerator;

    public NeuralNetworkDisplay(NeuralNetwork network, Pane networkContainer) {
        this.networkContainer = networkContainer;
        this.network = network;
        this.weightsGenerator = new WeightsGenerator(network, layers);

        initializeInputLayer();
        initializeOtherLayers();
    }

    private void initializeInputLayer() {
        if (FIRST_LAYER_NEURONS.get() > MAX_NEURONS)
            throw new IllegalArgumentException("Number of neurons at the first layer exceeds the maximum number of neurons");

        layers.add(new ArrayList<>());
        addLayerButtons();

        Matrix lastActivations = network.getActivationsAtLayer(0);

        for (int neuronIndex = 0; neuronIndex < FIRST_LAYER_NEURONS.get(); neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(0, activation);
        }

        weightsGenerator.generateLayerWeights(0);
    }

    private void initializeOtherLayers() {
        for (int i = 1; i < MIN_LAYERS; i++)
            addLayer();
    }

    /**
     * Add a layer to backend algo and update the display accordingly
     */
    public void addLayer() {
        if (network.getNumLayers() >= MAX_LAYERS) return;

        network.addLayer(LAST_LAYER_NEURONS.get());
        layers.add(new ArrayList<>());
        addLayerButtons();

        int lastLayerIndex = network.getNumLayers() - 1;
        Matrix lastActivations = network.getActivationsAtLayer(lastLayerIndex);

        for (int neuronIndex = 0; neuronIndex < LAST_LAYER_NEURONS.get(); neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(lastLayerIndex, activation);
        }

        weightsGenerator.generateLayerWeights(lastLayerIndex);
    }

    /**
     * Add buttons to add or remove neurons from the current last layer
     */
    private void addLayerButtons() {
        int lastLayerIndex = network.getNumLayers() - 1;

        ButtonContainer btnContainer = new ButtonContainer();

        btnContainer.setTranslateX(getLayerSpacing(lastLayerIndex));
        btnContainer.setAddNeuronAction(() -> addNeuronThroughBtn(lastLayerIndex));
        btnContainer.setRemoveNeuronAction(() -> removeNeuron(lastLayerIndex));

        networkContainer.getChildren().add(btnContainer);
        btnContainers.add(btnContainer);
    }

    private Neuron getNeuron(int layerIndex, int neuronIndex) {
        return layers.get(layerIndex).get(neuronIndex);
    }

    /**
     * Remove the last layer from the backend algo and update the display accordingly
     */
    public void removeLayer() {
        if (network.getNumLayers() <= MIN_LAYERS) return;
        int lastIndex = network.getNumLayers() - 1;

        network.removeLayer();
        networkContainer.getChildren().remove(btnContainers.get(lastIndex));

        ArrayList<Neuron> lastLayer = layers.get(lastIndex);

        for (Neuron neuron : lastLayer)
            neuron.remove();
        layers.remove(lastIndex);
        btnContainers.remove(lastIndex);
    }

    /**
     * General method to add a neuron to the display for when a layer is added or the addNeuron button is clicked
     * @param layerIndex the index of the layer to add the neuron to
     * @param activation the activation value of the neuron
     */
    private void addNeuron(int layerIndex, double activation) {
        if (layers.get(layerIndex).size() >= MAX_NEURONS) return;

        int lastNeuronIndex = layers.get(layerIndex).size();

        Neuron neuron = new Neuron(activation);
        neuron.setTranslateX(getLayerSpacing(layerIndex));
        neuron.setTranslateY(getNeuronSpacing(lastNeuronIndex + 1));

        layers.get(layerIndex).add(neuron);
        networkContainer.getChildren().add(neuron);

        weightsGenerator.generateWeights(lastNeuronIndex, layerIndex);
    }

    /**
     * Remove the neuron at layerIndex
     * @param layerIndex the index of the layer to remove the neuron from
     */
    private void removeNeuron(int layerIndex) {
        if (network.getNumNeurons(layerIndex) <= MIN_NEURONS) return;
        int lastNeuronIndex = network.getNumNeurons(layerIndex) - 1;

        network.removeNeuron(layerIndex);

        Neuron neuron = getNeuron(layerIndex, lastNeuronIndex);

        layers.get(layerIndex).remove(neuron);
        neuron.remove();
    }

    /**
     * Specific method for adding a neuron through the addNeuron button
     * @param layerIndex the index of the layer to add the neuron to
     */
    private void addNeuronThroughBtn(int layerIndex) {
        network.addNeuron(layerIndex);
        Matrix activation = network.getActivationsAtLayer(layerIndex);
        double activationValue = activation.get(activation.getRows() - 1, 0);
        addNeuron(layerIndex, activationValue);
    }

    /**
     * Clear the display and the backend algo
     */
    public void clear() {
        network.clear();
        update();
    }

    /**
     * Update the display so that the activation values are up to date
     */
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

