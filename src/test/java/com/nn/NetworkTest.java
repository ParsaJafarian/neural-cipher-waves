package com.nn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;

import static com.nn.NetworkTester.testAddLayer;
import static com.nn.NetworkTester.testAddNeuron;
import static org.junit.jupiter.api.Assertions.*;

public class NetworkTest {

    @Test
    public void testConstructor() {
        Network n = new Network(2, 3, 1);
        assert n.getNumLayers() == 2;
        assert n.getNumNeurons(0) == 3;
        assert n.getNumNeurons(1) == 1;

        // Check Biases
        assert n.getBiases().size() == 1;
        assert n.getBiases().get(0).getRows() == 1;
        assert n.getBiases().get(0).getColumns() == 1;

        // Check Weights
        assert n.getWeights().size() == 1;
        assert n.getBiases().get(0).getRows() == 1;
        assert n.getWeights().get(0).getColumns() == 3;

        // Check Activation Function
        assert n.getActivation() == Activation.SIGMOID;

        // Check Cost Function
        assert n.getLoss() == Loss.MSE;

        // Check Learning Rate
        assert n.getLearningRate() == 2.0;
    }

    @Test
    public void testFeedForward() {
        Network n = new Network(2, 3, 10, 10);
        Matrix input = Matrix.ones(3, 1); // 3 input neurons

        Matrix output = n.feedForward(input);
        assert output.getRows() == 10;
        assert output.getColumns() == 1;

        // Check if the output is between 0 and 1
        for (int i = 0; i < output.getRows(); i++) {
            assert output.get(i, 0) >= 0;
            assert output.get(i, 0) <= 1;
        }

        System.out.println(Arrays.deepToString(n.getWeights().get(0).getData()));
        System.out.println(Arrays.deepToString(n.getBiases().get(0).getData()));
    }

    @Test
    public void testSGD() {
        Network n = new Network(0.001, 1, 1); //2 input neurons, 1 output neurons
        Matrix[][] trainingData = new Matrix[10][2];
        Matrix[][] testData = new Matrix[10][2];

        //Problem: classify x < 0 as 0 and x >= 0 as 1

        double[] xTrain = new double[]{-1, -2, -3, -4, -5, 1, 2, 3, 4, 5};
        double[] yTrain = new double[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1};

        double[] xTest = new double[]{-500, -400, -300, -200, -100, 100, 200, 300, 400, 500};
        double[] yTest = new double[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1};

        for (int i = 0; i < 10; i++) {
            trainingData[i][0] = new Matrix(new double[][]{{xTrain[i]}});
            trainingData[i][1] = new Matrix(new double[][]{{yTrain[i]}});
        }

        //initialize test data
        for (int i = 0; i < 10; i++) {
            testData[i][0] = new Matrix(new double[][]{{xTest[i]}});
            testData[i][1] = new Matrix(new double[][]{{yTest[i]}});
        }

        //Save initial weights and biases
        ArrayList<Matrix> initialWeights = n.getWeights();
        ArrayList<Matrix> initialBiases = n.getBiases();

        n.sgd(trainingData, testData, 1, 2);

        //check if weights and biases have been updated
        assert initialWeights.size() == n.getWeights().size();
        assert initialBiases.size() == n.getBiases().size();

        //For each weights and biases, check if their data are not the same
        for (int i = 0; i < initialWeights.size(); i++)
            assertNotEquals(initialWeights.get(i).getData(), n.getWeights().get(i).getData());

        for (int i = 0; i < initialBiases.size(); i++)
            assertNotEquals(initialBiases.get(i).getData(), n.getBiases().get(i).getData());

    }

    @Test
    public void testAddLayer1() {
        testAddLayer(2, 1, 1);
    }

    @Test
    public void testAddLayer2(){
        testAddLayer(3, 1, 1, 1);
    }

    @Test
    public void testAddNeuron1() {
        testAddNeuron(0, 4, 6, 3);
    }

    @Test
    public void testAddNeuron2() {
        testAddNeuron(1, 4, 6, 3);
    }

    @Test
    public void testAddNeuron3() {
        testAddNeuron(2, 4, 6, 3);
    }
}
