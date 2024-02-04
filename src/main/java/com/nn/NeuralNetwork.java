package com.nn;

import java.util.HashMap;

public class NeuralNetwork {
    private final double learningRate;
    private final int[] sizes;
    private final Layer[] layers;
    private final String activationFunction;
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

        this.activationFunction = activationFunction;

        for (int i = 0; i < sizes.length; i++) {
            int nextSize = i + 1 < sizes.length ? sizes[i + 1] : 0;
            layers[i] = new Layer(sizes[i], nextSize, activationFunctions.get(activationFunction));
        }
    }

    public NeuralNetwork(double learningRate, int... sizes) {
        this(learningRate, "sigmoid", sizes);
    }

    public NeuralNetwork clone() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(learningRate, activationFunction, sizes);
        for (int i = 0; i < layers.length; i++)
            neuralNetwork.layers[i] = layers[i].clone();
        return neuralNetwork;
    }

    public void mutate() {
        for (Layer layer : layers) layer.mutate(learningRate);
    }

}
