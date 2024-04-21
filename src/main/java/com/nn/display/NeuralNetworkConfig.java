package com.nn.display;

public class NeuralNetworkConfig {
    public static final int MIN_LAYERS = 2, MAX_LAYERS = 5;
    public static final int MIN_NEURONS = 2, MAX_NEURONS = 10;
    public static final int FIRST_LAYER_NEURONS = 5,  LAST_LAYER_NEURONS = 5;

    public static int getLayerSpacing(int layerIndex) {
        return 150 * layerIndex;
    }

    public static int getNeuronSpacing(int neuronIndex) {
        return 50 * neuronIndex;
    }
}