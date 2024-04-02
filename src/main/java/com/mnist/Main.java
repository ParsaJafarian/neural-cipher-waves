package com.mnist;

import com.nn.Matrix;
import com.nn.Network;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        Matrix[][] trainData = new MnistLoader().load("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte");
        Matrix[][] testData = new MnistLoader().load("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte");

        Network network = new Network(0.001, "relu", "mse", 784, 100, 10);

        network.sgd(trainData, testData, 10,100);

    }
}
