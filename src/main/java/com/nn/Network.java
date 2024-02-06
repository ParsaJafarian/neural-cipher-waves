package com.nn;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Neural network class for creating and training neural networks.
 */
public class Network {
    private final double learningRate;
    private final int[] sizes;
    private final int numLayers;
    private final Matrix[] weights, biases;
    private final String activationFunction;
    private static final HashMap<String, ActivationFunction> activationFunctions = new HashMap<>() {{
        put("sigmoid", ActivationFunction.SIGMOID);
        put("tanh", ActivationFunction.TANH);
        put("relu", ActivationFunction.RELU);
    }};

    /**
     * @param learningRate learning rate of the network
     * @param activationFunction activation function of the network
     * @param sizes sizes of the layers in the network
     */
    public Network(double learningRate, String activationFunction, int @NotNull ... sizes) {
        if (learningRate <= 0.0)
            throw new IllegalArgumentException("Learning rate must be positive");
        if (sizes.length < 2)
            throw new IllegalArgumentException("Network must have at least 2 layers");
        if (Arrays.stream(sizes).anyMatch(x -> x <= 0))
            throw new IllegalArgumentException("Invalid layer sizes");
        if (!activationFunctions.containsKey(activationFunction))
            throw new IllegalArgumentException("Activation function not found");

        //initialize weights and biases
        this.weights = new Matrix[sizes.length - 1];
        this.biases = new Matrix[sizes.length - 1];

        for (int i = 0; i < sizes.length - 1; i++) {
            weights[i] = new Matrix(sizes[i + 1], sizes[i]).map(x -> Math.random() * 2 - 1);
            biases[i] = new Matrix(sizes[i + 1], 1).map(x -> Math.random() * 2 - 1);
        }

        this.learningRate = learningRate;
        this.sizes = sizes;
        this.numLayers = sizes.length;
        this.activationFunction = activationFunction;

//        for (int i = 0; i < sizes.length; i++) {
//            ActivationFunction function = activationFunctions.get(activationFunction);
//            Matrix nextInputs = i == 0 ? new Matrix(sizes[i], 1) : new Matrix(sizes[i - 1], 1);
//            layers[i] = new Layer(sizes[i], nextInputs, function);
//        }
    }

    /**
     * Constructor with default activation function of sigmoid.
     * @param learningRate learning rate of the network
     * @param sizes sizes of the layers in the network
     */
    public Network(double learningRate, int... sizes) {
        this(learningRate, "sigmoid", sizes);
    }

    /**
     * Feed forward the inputs through the network with the current weights and biases and activation function.
     * @param inputs input to the network
     * @return the output of the network
     */
    public Matrix feedForward(@NotNull Matrix inputs) {
        //if input size is not equal to the first layer size and its not a column vector, throw an exception
        if (inputs.getRows() != sizes[0] || inputs.getCols() != 1)
            throw new IllegalArgumentException("Invalid input size");

        Matrix outputs = inputs.clone();

        for (int i = 0; i < numLayers - 1; i++) {
            //o = activationFunction(w * i + b)
            outputs = activationFunctions.get(activationFunction).function(weights[i].dot(inputs).add(biases[i]));
        }

        return outputs;
    }

    public int getNumLayers() {
        return numLayers;
    }

    public int[] getSizes() {
        return sizes;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public Matrix[] getWeights() {
        return weights;
    }

    public Matrix[] getBiases() {
        return biases;
    }

    public String getActivationFunction() {
        return activationFunction;
    }

    @Override
    public String toString() {
        return "Network{" +
                "learningRate=" + learningRate +
                ", sizes=" + Arrays.toString(sizes) +
                ", numLayers=" + numLayers +
                ", weights=" + Arrays.toString(weights) +
                ", biases=" + Arrays.toString(biases) +
                ", activationFunction='" + activationFunction + '\'' +
                '}';
    }

}
