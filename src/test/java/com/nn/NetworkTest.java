package com.nn;

import org.junit.jupiter.api.Test;

public class NetworkTest {

    @Test
    public void testConstructor() {
        Network n = new Network(2, 3, 1);
        assert n.getSizes().length == 2;
        assert n.getSizes()[0] == 3;
        assert n.getSizes()[1] == 1;

        // Check Biases
        assert n.getBiases().length == 1;
        assert n.getBiases()[0].getRows() == 1; // 1 because the output layer has 1 neuron
        assert n.getBiases()[0].getCols() == 1; // 1 because Biases are column vectors

        // Check Weights
        assert n.getWeights().length == 1;
        assert n.getWeights()[0].getRows() == 1; // 1 because the output layer has 1 neuron
        assert n.getWeights()[0].getCols() == 3; // 3 because the input layer has 3 neurons

        // Check Activation Function
        assert n.getActivationFunction() == ActivationFunction.SIGMOID;

        // Check Cost Function
        assert n.getCostFunction() == CostFunction.QUADRATIC;

        // Check Learning Rate
        assert n.getLearningRate() == 0.1;
    }

    @Test
    public void testFeedForward() {
        Network n = new Network(2, 3, 10, 10);
        Matrix input = new Matrix(new double[][]{{1}, {2}, {3}});
        input.map(x -> (double) 1); // Set all inputs to 1

        Matrix output = n.feedForward(input);
        assert output.getRows() == 10;
        assert output.getCols() == 1;

        // Check if the output is between 0 and 1
        for (int i = 0; i < output.getRows(); i++) {
            assert output.data[i][0] >= 0;
            assert output.data[i][0] <= 1;
        }
    }


}
