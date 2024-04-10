package com.nn.display;

public class NeuralNetworkConfig {
    public static final int MIN_LAYERS = 2, MAX_LAYERS = 5;
    public static final int MIN_NEURONS = 2, MAX_NEURONS = 8;

    public static int getLayerSpacing(int layerIndex) {
        return 150 * (layerIndex - 1);
    }

    public static int getNeuronSpacing(int neuronIndex) {
        return 50 * neuronIndex;
    }
}
