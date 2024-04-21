package com.nn.display;

import java.util.concurrent.atomic.AtomicInteger;

public class NeuralNetworkConfig {
    public static final int MIN_LAYERS = 2, MAX_LAYERS = 5;
    public static final int MIN_NEURONS = 2, MAX_NEURONS = 10;
    public static final AtomicInteger FIRST_LAYER_NEURONS = new AtomicInteger(MIN_NEURONS);
    public static final AtomicInteger LAST_LAYER_NEURONS = new AtomicInteger(MIN_NEURONS);

    public static int getLayerSpacing(int layerIndex) {
        return 150 * layerIndex;
    }

    public static int getNeuronSpacing(int neuronIndex) {
        return 50 * neuronIndex;
    }
}
