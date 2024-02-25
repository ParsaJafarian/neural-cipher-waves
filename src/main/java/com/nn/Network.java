package com.nn;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Neural network class for creating and training neural networks.
 */
public class Network {
    private double learningRate;
    private static final HashMap<String, CostFunction> costFunctions = new HashMap<>() {{
        put("mse", CostFunction.MSE);
        put("mae", CostFunction.MAE);
    }};
    /**
     * An array of sizes of the layers in the network.
     * Each element represents a layer while the value represents the number of neurons in that layer.
     */
    private final int[] sizes;
    private final int numLayers;

    private ArrayList<Matrix> activations;
    /**
     * A ArrayList of weights matrices for each layer in the network.
     * The weights matrix for each layer is of size (n x m) where n is the number of neurons
     * in the current layer and m is the number of neurons in the previous layer.
     */
    private final ArrayList<Matrix> weights;
    /**
     * A ArrayList of biases vectors for each layer in the network.
     * The biases vector for each layer is of size (n x 1) where n is the number of neurons in that layer.
     */
    private final ArrayList<Matrix> biases;
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
        weights = new ArrayList<>();
        biases = new ArrayList<>();
        activations = new ArrayList<>();

        for (int i = 0; i < sizes.length - 1; i++) {
            weights.add(Matrix.random(sizes[i + 1], sizes[i]));
            biases.add(Matrix.random(sizes[i + 1], 1));
        }

        this.sizes = sizes;
        this.numLayers = sizes.length;
        this.activationFunction = activationFunctions.get(activationFunction);
        this.costFunction = costFunctions.get(costFunction);
        this.learningRate = learningRate;

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
     * @param zs          array to store the z values of the network (passed in as an empty array)
     * @return the output of the network
     */
    private Matrix feedForward(@NotNull Matrix inputs, ArrayList<Matrix> zs) {
        //if input size is not equal to the first layer size and it's not a column vector, throw an exception
        if (inputs.getRows() != sizes[0] || inputs.getCols() != 1)
            throw new IllegalArgumentException("Invalid input size");

        //clear the activations ArrayList and add the inputs to it
        activations.clear();
        activations.add(inputs);

        Matrix outputs = inputs.clone();

        for (int i = 0; i < numLayers - 1; i++) {
            Matrix z = weights.get(i).dot(outputs).add(biases.get(i));
            zs.add(z);
            outputs = activationFunction.f(z);
            activations.add(outputs);
        }

        return outputs;
    }

    Matrix feedForward(@NotNull Matrix inputs) {
        return feedForward(inputs, new ArrayList<>());
    }

    private static void shuffleData(Matrix @NotNull [][] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Matrix[] a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /**
     * @param trainingData  the training data. Structure: [[input matrix, output matrix], ...]
     * @param testData      the test data. Structure: [[input matrix, output matrix], ...]
     * @param epochs        number of epochs to train the network
     * @param miniBatchSize size of each mini batch
     */
    public void sgd(Matrix[] @NotNull [] trainingData, Matrix[] @NotNull [] testData, int epochs, int miniBatchSize) {
        if (trainingData.length == 0)
            throw new IllegalArgumentException("Training data is empty");
        if (testData != null && testData.length == 0)
            throw new IllegalArgumentException("Test data is empty");
        if (Arrays.stream(trainingData).anyMatch(x -> x.length != 2))
            throw new IllegalArgumentException("Invalid training data structure");


        for (int i = 1; i <= epochs; i++) {
            System.out.println("Epoch " + i + " started");
//            System.out.println("<---------------------Weights--------------------->");
//            for (Matrix m : weights) {
//                System.out.println(m);
//            }
//            System.out.println("<---------------------Biases--------------------->");
//            for (Matrix m : biases) {
//                System.out.println(m);
//            }


            AtomicBoolean isNaN = new AtomicBoolean(false);

            for (int j = 0; j < trainingData.length; j += miniBatchSize) {
                shuffleData(trainingData);
                Matrix[][] miniBatch = Arrays.copyOfRange(trainingData, j, j + miniBatchSize);
                updateMiniBatch(miniBatch);
                for (Matrix m : weights) {
                    if (m.hasNan()) {
                        isNaN.set(true);
                        break;
                    }
                }
                for (Matrix m : biases) {
                    if (m.hasNan()) {
                        isNaN.set(true);
                        break;
                    }
                }
                if (isNaN.get()) {
                    System.out.println("NaN detected in weights or biases in epoch " + i + " mini batch " + j/miniBatchSize);
                    break;
                }
            }

            System.out.println("Epoch " + i + " complete");
//            System.out.println("<---------------------Weights--------------------->");
//            for (Matrix m : weights) {
//                System.out.println(m);
//            }
//            System.out.println("<---------------------Biases--------------------->");
//            for (Matrix m : biases) {
//                System.out.println(m);
//            }
            if (testData != null) {
                System.out.println("Accuracy: " + evaluate(testData) * 100 + "%");
            }
        }
    }

    /**
     * @param testData the test data. Structure: [[input matrix, output matrix], ...]
     * @return the accuracy of the network on the test data
     */
    private double evaluate(Matrix[][] testData) {
        if (testData.length == 0)
            throw new IllegalArgumentException("Test data is empty");

        int correct = 0;
        //each data consists of the input and the actual output [input, output] where input and output are matrices
        for (Matrix[] data : testData) {
            Matrix x = data[0];
            Matrix y = data[1];
            //data[0] is the input, data[1] is the actual output
            Matrix output = feedForward(x);
            if (output.maxIndex() == y.maxIndex())
                correct++;
        }
        return (double) correct / testData.length;
    }

    public double predict(Matrix input) {
        return feedForward(input).maxIndex();
    }

    /**
     * @return an array of two linked ArrayLists. The first linked ArrayList contains the nablas for the biases and the second linked ArrayList contains the nablas for the weights
     */
    @Contract(" -> new")
    private ArrayList<Matrix> @NotNull [] createNablas() {
        ArrayList<Matrix> nablaB = new ArrayList<>();
        ArrayList<Matrix> nablaW = new ArrayList<>();

        for (Matrix bias : biases) nablaB.add(Matrix.zeros(bias.getRows(), bias.getCols()));
        for (Matrix matrix : weights) nablaW.add(Matrix.zeros(matrix.getRows(), matrix.getCols()));

        return new ArrayList[]{nablaB, nablaW};
    }

    private void updateMiniBatch(Matrix[] @NotNull [] miniBatch) {
        ArrayList<Matrix>[] nablas = createNablas();
        ArrayList<Matrix> nablaB = nablas[0];
        ArrayList<Matrix> nablaW = nablas[1];


        for (Matrix[] inputs : miniBatch) {

            ArrayList<Matrix>[] deltaNablas = backpropagation(inputs);
            ArrayList<Matrix> deltaNablaB = deltaNablas[0];
            ArrayList<Matrix> deltaNablaW = deltaNablas[1];

            for (int i = 0; i < biases.size(); i++)
                nablaB.set(i, nablaB.get(i).add(deltaNablaB.get(i)));

            for (int i = 0; i < weights.size(); i++)
                nablaW.set(i, nablaW.get(i).add(deltaNablaW.get(i)));
        }

        for (int i = 0; i < biases.size(); i++)
            biases.set(i, biases.get(i).subtract(nablaB.get(i).multiply(learningRate / miniBatch.length)));

        for (int i = 0; i < weights.size(); i++)
            weights.set(i, weights.get(i).subtract(nablaW.get(i).multiply(learningRate / miniBatch.length)));
    }

    /**
     * @param inputs inputs to the network. It's an array of two matrices where the first matrix is the input and the second matrix is the expected output
     */
    private ArrayList<Matrix>[] backpropagation(Matrix @NotNull [] inputs) {
        if (inputs.length != 2)
            throw new IllegalArgumentException("Invalid input size");

        ArrayList<Matrix>[] nablas = createNablas();
        ArrayList<Matrix> nablaB = nablas[0];
        ArrayList<Matrix> nablaW = nablas[1];

        Matrix x = inputs[0];
        Matrix y = inputs[1];

        ArrayList<Matrix> zs = new ArrayList<>();

        //feedforward (store the activations and zs)
        feedForward(x, zs);

        //backward pass
        //delta^L = (a^L - y) (+) f'(z^L)
        Matrix a = activations.get(activations.size() - 1); //a^L
        Matrix z = zs.get(zs.size() - 1); //z^L
        Matrix delta = costFunction.der(y, a).multiply(activationFunction.der(z));

        //deltaNablaB^L = delta^L
        nablaB.set(nablaB.size() - 1, delta);
        //deltaNablaW^L = delta^L * a^(L-1)
        nablaW.set(nablaW.size() - 1, delta.dot(activations.get(activations.size() - 2).transpose()));

        for (int l = 2; l < numLayers; l++) {
            z = zs.get(zs.size() - l); //z^(l)
            a = activations.get(activations.size() - l - 1); //a^(l-1)
            Matrix sp = activationFunction.der(z); //f'(z^l)

            //delta^(l)= ((w^(l+1))^T * delta^(l+1)) (+) f'(z^l)
            delta = weights.get(weights.size() - l + 1).transpose().dot(delta).multiply(sp);

            //deltaNablaB^l = delta^l
            nablaB.set(nablaB.size() - l, delta);

            //deltaNablaW^l = delta^l * a^(l-1)
            nablaW.set(nablaW.size() - l, delta.dot(a.transpose()));
        }

        return new ArrayList[]{nablaB, nablaW};
    }

    public int getNumLayers() {
        return numLayers;
    }

    public int[] getSizes() {
        return sizes;
    }

    public ArrayList<Matrix> getWeights() {
        return weights;
    }

    public ArrayList<Matrix> getBiases() {
        return biases;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    @Override
    public String toString() {
        return "Network{" +
                ", sizes=" + Arrays.toString(sizes) +
                ", numLayers=" + numLayers +
                ", weights=" + weights +
                ", biases=" + biases +
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

    /**
     * @return a deep copy of the activations of the network
     */
    public ArrayList<Matrix> getActivations() {
//        ArrayList<Matrix> copy = new ArrayList<>();
//        activations.forEach(x -> copy.add(x.clone()));
//        return copy;
        return activations;
    }
}
