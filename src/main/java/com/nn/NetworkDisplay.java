package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class consists of the visible neural display accesible in Simulation
 */
public class NetworkDisplay {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int PANE_PADDING = 20;
    private static final int MIN_LAYERS = 2, MAX_LAYERS = 6;
    private static final int MIN_NEURONS = 2, MAX_NEURONS = 8;
    private final HBox networkContainer;

    private final Network network;

    private final ArrayList<Line> lineWeights = new ArrayList<>();
    private final ArrayList<ArrayList<Circle>> neuronList;
    private final ArrayList<VBox> layerContainers = new ArrayList<>();
    private final ArrayList<VBox> lineContainers = new ArrayList<>();

    public NetworkDisplay(HBox networkContainer) {
        this.networkContainer = networkContainer;
        this.networkContainer.setPrefSize(WIDTH, HEIGHT);

        this.neuronList = new ArrayList<>();

        this.network = new Network(0.01, 2, 4);

        generateLayers();
        generateWeights();
    }

    private void addLayers() {
        for (int i = 0; i < network.getNumberOfLayers(); i++) {
            addLayer();
        }
    }


    private void generateLayers() {
        for (int i = 0; i < network.getNumberOfLayers(); i++) {
            int numNeurons = network.getNumberOfNeurons(i);
            generateLayer(i, numNeurons);
        }
    }

    private void generateLayer(int indexOfLayer, int numberOfNeurons) {
        VBox layerContainer = new VBox();
        layerContainer.setAlignment(Pos.CENTER);
        layerContainer.setSpacing(5);

        ArrayList<Circle> layer = new ArrayList<>();

        for (int indexOfNeuron = 0; indexOfNeuron < numberOfNeurons; indexOfNeuron++) {
            Label value = new Label();
            value.setId("neuronNet");
            double activation = network.getActivations().get(indexOfLayer).get(indexOfNeuron, 0);
            DoubleProperty prop = new SimpleDoubleProperty(activation);

            value.textProperty().bind(prop.asString("%.2f"));

            Circle neuron = new Circle(20);
            neuron.setUserData(prop);

            layerContainer.getChildren().add(neuron);
            layer.add(neuron);
        }

        neuronList.add(layer);
        networkContainer.getChildren().add(layerContainer);
        layerContainers.add(layerContainer);
    }

    private void generateWeights() {
        for (int i = 0; i < network.getWeights().size(); i++) {
            generateLayerWeights(i);
        }
    }

    private void generateLayerWeights(int layerIndex) {
        int currNumNeurons = network.getNumberOfNeurons(layerIndex + 1);
        for (int currNeuron = 0; currNeuron < currNumNeurons; currNeuron++) {
            int prevNumNeurons = network.getActivations().get(layerIndex).getRows();
            for (int prevNeuron = 0; prevNeuron < prevNumNeurons; prevNeuron++) {
                generateWeight(currNeuron, prevNeuron, layerIndex);
            }
        }
    }

    /**
     * Generate weight line between current neuron and a previous neuron
     *
     * @param currNeuronIndex index of the current neuron
     * @param prevNeuronIndex index of the previous neuron
     * @param currLayerIndex  index of the current layer
     */
    private void generateWeight(int currNeuronIndex, int prevNeuronIndex, int currLayerIndex) {
        double weight = Math.abs(network.getWeights().get(currLayerIndex).get(currNeuronIndex, prevNeuronIndex));
        Line line = new Line();
        VBox layer = layerContainers.get(currLayerIndex);

        SimpleDoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);

        Circle currNeuron = neuronList.get(currLayerIndex + 1).get(currNeuronIndex);
        Circle prevNeuron = neuronList.get(currLayerIndex).get(prevNeuronIndex);

        // Calculate the center positions for the current and previous neurons
        double currNeuronCenterX = currNeuron.getBoundsInParent().getCenterX();
        double prevNeuronCenterX = prevNeuron.getBoundsInParent().getCenterX();

        // Bind line coordinates to neuron centers accounting for VBox layout
        line.startXProperty().bind(currNeuron.parentProperty().get().layoutXProperty().add(currNeuronCenterX));
        line.endXProperty().bind(prevNeuron.parentProperty().get().layoutXProperty().add(prevNeuronCenterX));


        // Bind line coordinates to neuron centers accounting for HBox
        line.startYProperty().bind(
                currNeuron.layoutYProperty().add(currNeuron.getTranslateY()).add(currNeuron.getRadius()).add(layer.layoutYProperty())
        );
        line.endYProperty().bind(
                prevNeuron.layoutYProperty().add(prevNeuron.getTranslateY()).add(prevNeuron.getRadius()).add(layer.layoutYProperty())
        );

        // Set line appearance properties
        line.opacityProperty().bind(value.divide(2).add(0.5));
        line.strokeWidthProperty().bind(value.divide(1.5).add(0.5).multiply(3));

        // Add line to the container
        networkContainer.getChildren().add(line);
        line.toBack(); // Ensure the lines are behind the neurons
        line.setManaged(false); // The HBox will now ignore the line when laying out its children
    }


    public void addLayer() {
//        addLayerButtons();

        network.addLayer(MIN_NEURONS);
        generateLayer(network.getNumberOfLayers() - 2, MIN_NEURONS);
        generateLayerWeights(network.getNumberOfLayers() - 2);
    }

    private void addLayerButtons(VBox layerContainer) {
        HBox subBtnContainer = new HBox();
        subBtnContainer.setSpacing(5);

        subBtnContainer.getChildren().add(new Button("+"));
        subBtnContainer.getChildren().add(new Button("-"));
    }

    private void addLayerNeurons() {
        network.addLayer(MIN_NEURONS);
        generateLayer(network.getNumberOfLayers() - 1, MIN_NEURONS);
    }

    private void degenerateWeight(int currNeuronIndex, int prevNeuronIndex, int layerIndex) {
        Line line = lineWeights.get(currNeuronIndex * network.getNumberOfNeurons(layerIndex) + prevNeuronIndex);
        networkContainer.getChildren().remove(line);
    }

    public void clear() {
        network.clear();
        update();
    }

    public void update() {
        for (int i = 0; i < network.getActivations().size(); i++)
            for (int j = 0; j < network.getActivations().get(i).getRows(); j++)
                ((SimpleDoubleProperty) neuronList.get(i).get(j).getUserData()).set(network.getActivations().get(i).get(j, 0));
    }
}

