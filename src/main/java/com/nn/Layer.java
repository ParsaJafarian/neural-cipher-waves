package com.wavesneuralnetwork;

public class Layer {
    private final int size;
    private double[] biases;
    private double[][] weights;
    private double[] outputs;
    private double[] inputs;
    private ActivationFunction activationFunction;


    public Layer(int size, int inputSize, ActivationFunction activationFunction) {
        this.size = size;
        this.biases = new double[size];
        this.weights = new double[size][inputSize];
        this.outputs = new double[size];
        this.inputs = new double[inputSize];
        this.activationFunction = activationFunction;
    }

    private double dotProduct(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++)
            sum += a[i] * b[i];
        return sum;
    }

    public void feedForward() {
        for (int i = 0; i < size; i++) {
            outputs[i] = activationFunction.function(dotProduct(weights[i], inputs) + biases[i]);
        }
    }
}
