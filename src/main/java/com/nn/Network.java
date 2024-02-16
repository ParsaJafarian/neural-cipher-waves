package com.nn;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Neural network class for creating and training neural networks.
 */
public class Network {
    private double learningRate;
    private static final HashMap<String, CostFunction> costFunctions = new HashMap<>() {{
        put("quadratic", CostFunction.QUADRATIC);
    }};
    /**
     * An array of sizes of the layers in the network.
     * Each element represents a layer while the value represents the number of neurons in that layer.
     */
    private final int[] sizes;
    private final int numLayers;
    /**
     * An array of weights matrices for each layer in the network.
     * The weights matrix for each layer is of size (n x m) where n is the number of neurons
     * in the next layer and m is the number of neurons in the current layer.
     */
    private final Matrix[] weights;
    /**
     * An array of biases vectors for each layer in the network.
     * The biases vector for each layer is of size (n x 1) where n is the number of neurons in that layer.
     */
    private final Matrix[] biases;
    private final ActivationFunction activationFunction;
    private final CostFunction costFunction;
    private static final HashMap<String, ActivationFunction> activationFunctions = new HashMap<>() {{
        put("sigmoid", ActivationFunction.SIGMOID);
        put("tanh", ActivationFunction.TANH);
        put("relu", ActivationFunction.RELU);
    }};

    /**
     * Constructor for the network.
     *
     * @param learningRate       learning rate of the network
     * @param activationFunction activation function of the network
     * @param costFunction       cost function of the network
     * @param sizes              sizes of the layers in the network.
     */
    public Network(double learningRate, String activationFunction, String costFunction, int @NotNull ... sizes) {
        if (learningRate <= 0.0)
            throw new IllegalArgumentException("Learning rate must be positive");
        if (sizes.length < 2)
            throw new IllegalArgumentException("Network must have at least 2 layers");
        if (Arrays.stream(sizes).anyMatch(x -> x <= 0))
            throw new IllegalArgumentException("Invalid layer sizes");
        if (!activationFunctions.containsKey(activationFunction))
            throw new IllegalArgumentException("Activation function not found");
        if (!costFunctions.containsKey(costFunction))
            throw new IllegalArgumentException("Cost function not found");

        //initialize weights and biases
        this.weights = new Matrix[sizes.length - 1];
        this.biases = new Matrix[sizes.length - 1];

        for (int i = 0; i < sizes.length - 1; i++) {
            weights[i] = new Matrix(sizes[i + 1], sizes[i]).map(x -> Math.random() * 2 - 1);
            biases[i] = new Matrix(sizes[i + 1], 1).map(x -> Math.random() * 2 - 1);
        }

        this.sizes = sizes;
        this.numLayers = sizes.length;
        this.activationFunction = activationFunctions.get(activationFunction);
        this.costFunction = costFunctions.get(costFunction);

    }

    /**
     * Constructor with default activation function of sigmoid.
     *
     * @param learningRate learning rate of the network
     * @param sizes        sizes of the layers in the network
     */
    public Network(double learningRate, int... sizes) {
        this(learningRate, "sigmoid", "quadratic", sizes);
    }

    /**
     * Feed forward the inputs through the network with the current weights and biases and activation function.
     *
     * @param inputs      input to the network
     * @param activations array to store the activations of the network (passed in as an empty array)
     * @param zs          array to store the z values of the network (passed in as an empty array)
     * @return the output of the network
     */
    private Matrix feedForward(@NotNull Matrix inputs, Matrix[] activations, Matrix[] zs) {
        //if input size is not equal to the first layer size and it's not a column vector, throw an exception
        if (inputs.getRows() != sizes[0] || inputs.getCols() != 1)
            throw new IllegalArgumentException("Invalid input size");

        Matrix outputs = inputs.clone();

        for (int i = 0; i < numLayers - 1; i++) {
            Matrix z = weights[i].dot(outputs).add(biases[i]);
            zs[i] = z;
            outputs = activationFunction.function(z);
            activations[i] = outputs;
        }

        return outputs;
    }

    Matrix feedForward(@NotNull Matrix inputs) {
        return feedForward(inputs, new Matrix[numLayers - 1], new Matrix[numLayers - 1]);
    }

    private static void shuffleData(Matrix @NotNull [] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Matrix a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void sgd(Matrix[] trainingData, Matrix[] testData, int epochs, int miniBatchSize) {
        for (int i = 0; i < epochs; i++) {
            shuffleData(trainingData);

            for (int j = 0; j < trainingData.length; j += miniBatchSize) {
                Matrix[] miniBatch = Arrays.copyOfRange(trainingData, j, j + miniBatchSize);
                updateMiniBatch(miniBatch);
            }

            System.out.println("Epoch " + i + " complete");
            if (testData != null) {
                System.out.println("Accuracy: " + evaluate(testData) + " / " + testData.length);
            }
        }
    }

    private double evaluate(Matrix @NotNull [] testData) {
        if(testData.length == 0)
            throw new IllegalArgumentException("Test data is empty");

        int correct = 0;
        for (Matrix data : testData) {
            Matrix output = feedForward(data);
            if (output.maxIndex() == data.maxIndex())
                correct++;
        }
        return (double) correct / testData.length;
    }

    private void updateMiniBatch(Matrix[] miniBatch) {
        Matrix[] nablaB = new Matrix[biases.length];
        Matrix[] nablaW = new Matrix[weights.length];

        for (int i = 0; i < nablaB.length; i++)
            nablaB[i] = new Matrix(biases[i].getRows(), biases[i].getCols());

        for (int i = 0; i < nablaW.length; i++)
            nablaW[i] = new Matrix(weights[i].getRows(), weights[i].getCols());

        for (Matrix inputs : miniBatch) {
            Matrix[] deltaNablaB, deltaNablaW;
            deltaNablaB = new Matrix[biases.length];
            deltaNablaW = new Matrix[weights.length];

            for (int i = 0; i < deltaNablaB.length; i++)
                deltaNablaB[i] = new Matrix(biases[i].getRows(), biases[i].getCols());

            for (int i = 0; i < deltaNablaW.length; i++)
                deltaNablaW[i] = new Matrix(weights[i].getRows(), weights[i].getCols());

            backpropagation(inputs, deltaNablaB, deltaNablaW);

            for (int i = 0; i < nablaB.length; i++)
                nablaB[i] = nablaB[i].add(deltaNablaB[i]);

            for (int i = 0; i < nablaW.length; i++)
                nablaW[i] = nablaW[i].add(deltaNablaW[i]);
        }

        for (int i = 0; i < biases.length; i++)
            biases[i] = biases[i].subtract(nablaB[i].multiply(learningRate / miniBatch.length));

        for (int i = 0; i < weights.length; i++)
            weights[i] = weights[i].subtract(nablaW[i].multiply(learningRate / miniBatch.length));

    }

     void backpropagation(Matrix inputs, Matrix @NotNull [] deltaNablaB, Matrix @NotNull [] deltaNablaW) {
        //feedforward
        Matrix[] activations = new Matrix[numLayers - 1];
        Matrix[] zs = new Matrix[numLayers - 1];

        //feedforward (store the activations and zs)
        feedForward(inputs, activations, zs);

        //backward pass
        //delta = (a - y) (+) f'(z)
        Matrix delta = costFunction.derivative(inputs, activations[activations.length - 1]).multiply(activationFunction.derivative(zs[zs.length - 1]));

        deltaNablaB[deltaNablaB.length - 1] = delta;
        deltaNablaW[deltaNablaW.length - 1] = delta.dot(activations[activations.length - 2].transpose());

        for (int i = 2; i < numLayers; i++) {
            Matrix z = zs[zs.length - i];
            Matrix sp = activationFunction.derivative(z);
            //delta = (w^T * delta) (+) f'(z)
            delta = weights[weights.length - i + 1].transpose().dot(delta).multiply(sp);
            deltaNablaB[deltaNablaB.length - i] = delta;
            //deltaNablaW = delta * a^(l-1)
            deltaNablaW[deltaNablaW.length - i] = delta.dot(activations[activations.length - i - 1].transpose());
        }
    }

    public int getNumLayers() {
        return numLayers;
    }

    public int[] getSizes() {
        return sizes;
    }

    public Matrix[] getWeights() {
        return weights.clone();
    }

    public Matrix[] getBiases() {
        return biases.clone();
    }

    public ActivationFunction getActivationFunction() {
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

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public CostFunction getCostFunction() {
        return costFunction;
    }
}
