package com.nn.algo;

import static com.nn.display.NeuralNetworkConfig.MIN_LAYERS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NeuralNetworkTester {

    private static NeuralNetwork network;

    /**
     * Test adding a neuron to the specified layer
     *
     * @param layerIndex the index of the layer to add the neuron to
     * @param sizes the sizes of the layers in the network
     */
    static void testAddNeuron(int layerIndex, int... sizes) {
        network = new NeuralNetwork(0.001, "relu", "mse", sizes);
        int oldNumNeurons = network.getNumNeurons(layerIndex);

        network.addNeuron(layerIndex);

        int newNumNeurons = network.getNumNeurons(layerIndex);
        assertEquals(oldNumNeurons + 1, newNumNeurons);
        assertEquals(newNumNeurons, network.getNumNeurons(layerIndex));

        Matrix activation = network.getActivations().get(layerIndex);
        assertEquals(network.getNumNeurons(layerIndex), activation.getRows());
        assertEquals(1, activation.getColumns());

        if (layerIndex > 0){
            Matrix weight = network.getWeights().get(layerIndex - 1);
            assertEquals(network.getNumNeurons(layerIndex), weight.getRows());
            assertEquals(network.getNumNeurons(layerIndex - 1), weight.getColumns());

            Matrix bias = network.getBiases().get(layerIndex - 1);
            assertEquals(network.getNumNeurons(layerIndex), bias.getRows());
            assertEquals(1, bias.getColumns());
        }

        if (layerIndex + 1 < network.getNumLayers()){
            Matrix nextWeight = network.getWeights().get(layerIndex);
            assertEquals(network.getNumNeurons(layerIndex + 1), nextWeight.getRows());
            assertEquals(network.getNumNeurons(layerIndex), nextWeight.getColumns());
        }
    }

    /**
     * Test removing a neuron from the specified layer
     * @param numNeurons the number of neurons to remove
     * @param sizes the sizes of the layers in the network
     */
    static void testAddLayer(int numNeurons, int... sizes) {
        network = new NeuralNetwork(0.001, "relu", "mse", sizes);
        int oldNumLayers = network.getNumLayers();

        network.addLayer(numNeurons);

        int newNumLayers = network.getNumLayers();
        assertEquals(oldNumLayers + 1, newNumLayers);
        assertEquals(numNeurons, network.getNumNeurons(-1));

        Matrix lastWeight = network.getWeights().get(network.getWeights().size() - 1);
        assertEquals(newNumLayers - 1, network.getWeights().size());
        assertEquals(network.getNumNeurons(-1), lastWeight.getRows());
        assertEquals(network.getNumNeurons(-2), lastWeight.getColumns());

        Matrix lastBias = network.getBiases().get(network.getBiases().size() - 1);
        assertEquals(newNumLayers - 1, network.getBiases().size());
        assertEquals(network.getNumNeurons(-1), lastBias.getRows());
        assertEquals(1, lastBias.getColumns());

        Matrix lastActivation = network.getActivations().get(network.getActivations().size() - 1);
        assertEquals(network.getNumLayers(), network.getActivations().size());
        assertEquals(network.getNumNeurons(-1), lastActivation.getRows());
        assertEquals(1, lastActivation.getColumns());
    }

    /**
     * Test removing a layer from the network
     * @param sizes the sizes of the layers in the network
     */
    static void testRemoveLayer(int... sizes) {
        network = new NeuralNetwork(0.001, "relu", "mse", sizes);
        int oldNumLayers = network.getNumLayers();
        if (oldNumLayers <= MIN_LAYERS) return; // Cannot remove input layer or output layer

        network.removeLayer();

        int newNumLayers = network.getNumLayers();
        assertEquals(oldNumLayers - 1, newNumLayers);

        int numActivations = network.getActivations().size();
        Matrix lastActivation = network.getActivations().get(network.getActivations().size() - 1);
        assertEquals(oldNumLayers - 1, numActivations);
        assertEquals(network.getNumNeurons(-1), lastActivation.getRows());


        int numWeights = network.getWeights().size();
        Matrix lastWeight = network.getWeights().get(network.getWeights().size() - 1);
        assertEquals(oldNumLayers - 2, numWeights);
        assertEquals(network.getNumNeurons(-1), lastWeight.getRows());
        assertEquals(network.getNumNeurons(-2), lastWeight.getColumns());

        int numBiases = network.getBiases().size();
        Matrix lastBias = network.getBiases().get(network.getBiases().size() - 1);
        assertEquals(oldNumLayers - 2, numBiases);
        assertEquals(network.getNumNeurons(-1), lastBias.getRows());
        assertEquals(1, lastBias.getColumns());

    }
}
