package com.nn.display;

import com.nn.NeuralNetwork;

import java.util.ArrayList;

public class WeightsGenerator {
    private final NeuralNetwork network;
    private final ArrayList<ArrayList<Neuron>> layers;

    public WeightsGenerator( NeuralNetwork network, ArrayList<ArrayList<Neuron>> layers) {
        this.network = network;
        this.layers = layers;
    }

    /**
     * Generate weights between last layer and the before last layer
     */
    void generateLayerWeights(int currLayerIndex) {
        if (currLayerIndex < 2) return;

        int currNumNeurons = network.getNumNeurons(currLayerIndex);
        for (int currNeuronIndex = 0; currNeuronIndex < currNumNeurons; currNeuronIndex++) {
            generateWeights(currNeuronIndex, currLayerIndex);
        }
    }

    void generateWeights(int currNeuronIndex, int currLayerIndex) {
        generateInputWeights(currNeuronIndex, currLayerIndex);
        generateOutputWeights(currNeuronIndex, currLayerIndex);
    }

    private void generateInputWeights(int currNeuronIndex, int currLayerIndex) {
        generateWeights(currNeuronIndex, currLayerIndex, true);
    }

    private void generateOutputWeights(int currNeuronIndex, int currLayerIndex) {
        generateWeights(currNeuronIndex, currLayerIndex, false);
    }

    private void generateWeights(int currNeuronIndex, int currLayerIndex, boolean isInput) {
        if (isInput && currLayerIndex <= 1) return;
        if (!isInput && currLayerIndex == network.getNumLayers() - 1) return;

        int otherLayerIndex = isInput ? currLayerIndex - 1 : currLayerIndex + 1;
        int otherNumNeurons = network.getNumNeurons(otherLayerIndex);

        for (int otherNeuronIndex = 0; otherNeuronIndex < otherNumNeurons; otherNeuronIndex++) {
            Neuron currNeuron = layers.get(currLayerIndex).get(currNeuronIndex);
            Neuron otherNeuron = layers.get(otherLayerIndex).get(otherNeuronIndex);

            if (isInput) currNeuron.connectToPrevNeuron(otherNeuron);
            else otherNeuron.connectToPrevNeuron(currNeuron);
        }
    }
}
