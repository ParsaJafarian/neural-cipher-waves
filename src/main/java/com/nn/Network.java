package com.nn;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Neural network class for creating and training neural networks.
 */
public class Network {
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

    private static void shuffleArray(Matrix[] array) {
        int index;
        Matrix temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public void sgd(Matrix[] trainingData, int epochs, int miniBatchSize, double learningRate) {

        //shuffle the training data
        for (int i = 0; i < epochs; i++) {
            shuffleArray(trainingData);
            for (int j = 0; j < trainingData.length; j += miniBatchSize) {
                Matrix[] miniBatch = Arrays.copyOfRange(trainingData, j, j + miniBatchSize);
                updateMiniBatch(miniBatch, learningRate);
            }
        }
    }

    private void updateMiniBatch(Matrix[] miniBatch, double learningRate) {
        Matrix[] nablaB = new Matrix[biases.length];
        Matrix[] nablaW = new Matrix[weights.length];
        for (int i = 0; i < biases.length; i++) {
            nablaB[i] = new Matrix(biases[i].getRows(), biases[i].getCols());
        }
        for (int i = 0; i < weights.length; i++) {
            nablaW[i] = new Matrix(weights[i].getRows(), weights[i].getCols());
        }

        for (Matrix x : miniBatch) {
            Matrix[] deltaNablaB, deltaNablaW;
            deltaNablaB = new Matrix[biases.length];
            deltaNablaW = new Matrix[weights.length];
            for (int i = 0; i < biases.length; i++) {
                deltaNablaB[i] = new Matrix(biases[i].getRows(), biases[i].getCols());
            }
            for (int i = 0; i < weights.length; i++) {
                deltaNablaW[i] = new Matrix(weights[i].getRows(), weights[i].getCols());
            }

//            backprop(x, deltaNablaB, deltaNablaW);

            for (int i = 0; i < biases.length; i++)
                nablaB[i] = nablaB[i].add(deltaNablaB[i]);
            for (int i = 0; i < weights.length; i++)
                nablaW[i] = nablaW[i].add(deltaNablaW[i]);
        }

        for (int i = 0; i < biases.length; i++)
            biases[i] = biases[i].sub(nablaB[i].multiply(learningRate / miniBatch.length));
        for (int i = 0; i < weights.length; i++)
            weights[i] = weights[i].sub(nablaW[i].multiply(learningRate / miniBatch.length));
    }

    public Matrix costDerivative(@NotNull Matrix outputActivations, Matrix y) {
        return outputActivations.sub(y);
    }


    public int getNumLayers() {
        return numLayers;
    }

    public int[] getSizes() {
        return sizes;
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
                ", sizes=" + Arrays.toString(sizes) +
                ", numLayers=" + numLayers +
                ", weights=" + Arrays.toString(weights) +
                ", biases=" + Arrays.toString(biases) +
                ", activationFunction='" + activationFunction + '\'' +
                '}';
    }

}
