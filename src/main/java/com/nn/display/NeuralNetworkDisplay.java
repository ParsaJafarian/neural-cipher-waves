package com.nn.display;

import com.nn.Matrix;
import com.nn.NeuralNetwork;
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
        if (FIRST_LAYER_NEURONS > MAX_NEURONS)
            throw new IllegalArgumentException("Number of neurons at the first layer exceeds the maximum number of neurons");

        layers.add(new ArrayList<>());
        addLayerButtons();

        Matrix lastActivations = network.getActivationsAtLayer(0);

        for (int neuronIndex = 0; neuronIndex < FIRST_LAYER_NEURONS; neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(0, activation);
        }

        weightsGenerator.generateLayerWeights(0);
        System.out.println(network.getSizes());
    }

    private void initializeOtherLayers() {
        for (int i = 1; i < MIN_LAYERS; i++)
            addLayer();
    }

    public void addLayer() {
        if (network.getNumLayers() >= MAX_LAYERS) return;

        network.addLayer(LAST_LAYER_NEURONS);
        layers.add(new ArrayList<>());
        addLayerButtons();

        int lastLayerIndex = network.getNumLayers() - 1;
        Matrix lastActivations = network.getActivationsAtLayer(lastLayerIndex);

        for (int neuronIndex = 0; neuronIndex < LAST_LAYER_NEURONS; neuronIndex++) {
            double activation = lastActivations.get(neuronIndex, 0);
            addNeuron(lastLayerIndex, activation);
        }

        weightsGenerator.generateLayerWeights(lastLayerIndex);
        System.out.println(network.getSizes());
    }

    /**
     * Add buttons to add or remove neurons from the layer
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

    private void addNeuron(int layerIndex, double activation) {
        if (layers.get(layerIndex).size() >= MAX_NEURONS) return;

        int lastNeuronIndex = layers.get(layerIndex).size();

        Neuron neuron = new Neuron(activation);
        neuron.setTranslateX(getLayerSpacing(layerIndex));
        neuron.setTranslateY(getNeuronSpacing(lastNeuronIndex + 1));

        layers.get(layerIndex).add(neuron);
        networkContainer.getChildren().add(neuron);

        weightsGenerator.generateWeights(lastNeuronIndex, layerIndex);
        System.out.println(network.getSizes());
    }

    private void removeNeuron(int layerIndex) {
        if (network.getNumNeurons(layerIndex) <= MIN_NEURONS) return;
        int lastNeuronIndex = network.getNumNeurons(layerIndex) - 1;

        network.removeNeuron(layerIndex);

        Neuron neuron = getNeuron(layerIndex, lastNeuronIndex);

        layers.get(layerIndex).remove(neuron);
        neuron.remove();
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

