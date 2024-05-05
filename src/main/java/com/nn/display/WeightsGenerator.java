package com.nn.display;

import com.nn.algo.NeuralNetwork;

import java.util.ArrayList;

/**
 * This class is responsible for generating weights between neurons in the neural network.
 */
public class WeightsGenerator {
    private final NeuralNetwork network;
    private final ArrayList<ArrayList<Neuron>> layers;

    /**
     * Constructor for the WeightsGenerator class.
     * @param network The neural network to generate weights for.
     * @param layers The display layers of the neural network.
     */
    public WeightsGenerator( NeuralNetwork network, ArrayList<ArrayList<Neuron>> layers) {
        this.network = network;
        this.layers = layers;
    }

    /**
     * Generate weights between
     */
    void generateLayerWeights(int currLayerIndex) {
        if (currLayerIndex < 1) return;

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

    /**
     * Generate weights between the current neuron and the neurons in the other layer.
     * If input is true, input weight lines are generated, otherwise output weight lines are generated.
     *
     * @param currNeuronIndex The index of the current neuron.
     * @param currLayerIndex The index of the current layer.
     * @param isInput True if the current neuron is in the input layer, false otherwise.
     */
    private void generateWeights(int currNeuronIndex, int currLayerIndex, boolean isInput) {
        if (isInput && currLayerIndex <= 0) return;
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
