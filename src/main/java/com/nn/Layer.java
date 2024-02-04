package com.nn;

import java.util.Arrays;

class Layer {
    private final int size;
    private double[] biases;
    private double[][] weights;
    private double[] outputs;
    private double[] inputs;
    private final ActivationFunction activationFunction;


    public Layer(int size, int inputSize, ActivationFunction activationFunction) {
        this.size = size;
        this.biases = new double[size];
        this.weights = new double[size][inputSize];
        this.outputs = new double[size];
        this.inputs = new double[inputSize];
        this.activationFunction = activationFunction;

        // Initialize weights and biases with random values
        for (int i = 0; i < size; i++) {
            biases[i] = Math.random() * 2 - 1;
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = Math.random() * 2 - 1;
            }
        }

    }

    private double dotProduct(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++)
            sum += a[i] * b[i];
        return sum;
    }

    public void feedForward() {
        for (int i = 0; i < size; i++)
            outputs[i] = activationFunction.function(dotProduct(weights[i], inputs) + biases[i]);
    }

    public Layer clone() {
        Layer layer = new Layer(size, inputs.length, activationFunction);
        for (int i = 0; i < size; i++) {
            layer.biases[i] = biases[i];
            System.arraycopy(weights[i], 0, layer.weights[i], 0, inputs.length);
        }
        return layer;
    }

    public void mutate(double learningRate) {
        for (int i = 0; i < size; i++) {
            if (Math.random() < learningRate)
                biases[i] += Math.random() * 2 - 1;
            for (int j = 0; j < inputs.length; j++)
                if (Math.random() < learningRate)
                    weights[i][j] += Math.random() * 2 - 1;
        }
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public double[] getBiases() {
        return biases;
    }

    public double[][] getWeights() {
        return weights;
    }

    public int getSize() {
        return size;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public double[] getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "size=" + size +
                ", biases=" + Arrays.toString(biases) +
                ", weights=" + Arrays.toString(weights) +
                ", outputs=" + Arrays.toString(outputs) +
                ", inputs=" + Arrays.toString(inputs) +
                ", activationFunction=" + activationFunction +
                '}';
    }
}
