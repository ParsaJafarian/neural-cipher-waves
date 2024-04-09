package com.nn;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkTester {

    private static Network network;

    static void testAddNeuron(int layerIndex, int... sizes) {
        network = new Network(0.001, "relu", "mse", sizes);
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

    static void testAddLayer(int numNeurons, int... sizes) {
        network = new Network(0.001, "relu", "mse", sizes);
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
}
