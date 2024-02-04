package com.wavesneuralnetwork;

import java.util.HashMap;

public class NeuralNetwork {
    private final double learningRate;
    private int[] sizes;
    private Layer[] layers;
    private static final HashMap<String, ActivationFunction> activationFunctions = new HashMap<>() {{
        put("sigmoid", ActivationFunction.SIGMOID);
        put("tanh", ActivationFunction.TANH);
        put("relu", ActivationFunction.RELU);
    }};

    public NeuralNetwork(double learningRate, String activationFunction, int... sizes) {
        this.learningRate = learningRate;
        this.sizes = sizes;
        this.layers = new Layer[sizes.length];

        if (!activationFunctions.containsKey(activationFunction))
            throw new IllegalArgumentException("Activation function not found");

        for (int i = 0; i < sizes.length; i++) {
            int nextSize = i + 1 < sizes.length ? sizes[i + 1] : 0;
            layers[i] = new Layer(sizes[i], nextSize, activationFunctions.get(activationFunction));
        }
    }



}
