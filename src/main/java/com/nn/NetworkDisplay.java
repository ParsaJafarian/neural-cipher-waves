package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


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
    private final Pane networkPane;

    private final Network network;

    private final ArrayList<Line> lineWeights = new ArrayList<>();
    private final ArrayList<ArrayList<Circle>> neuronList;
    private final HBox btnContainer;

    public NetworkDisplay(Pane networkPane, HBox btnContainer) {
        this.networkPane = networkPane;
        this.btnContainer = btnContainer;
        this.networkPane.setPrefSize(WIDTH, HEIGHT);

        this.neuronList = new ArrayList<>();
        neuronList.add(new ArrayList<>());

        this.network = new Network(0.01, 1);

        generateLayers();
    }

    private void addLayers(){
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
        int numLayers = network.getNumberOfLayers();
        int disposableWidth = WIDTH - 2 * PANE_PADDING;
        int disposableHeight = HEIGHT - 2 * PANE_PADDING;
        int layerGap = disposableWidth / (numLayers + 1);
        int heightGap = disposableHeight / (numberOfNeurons + 1);

        neuronList.add(indexOfLayer, new ArrayList<>());

        for (int indexOfNeuron = 0; indexOfNeuron < numberOfNeurons; indexOfNeuron++) {
            Label value = new Label();
            value.setId("neuronNet");
            double activation = network.getActivations().get(indexOfLayer).get(indexOfNeuron, 0);
            DoubleProperty prop = new SimpleDoubleProperty(activation);

            value.textProperty().bind(prop.asString("%.2f"));

            Circle neuron = new Circle(20);
            neuron.setUserData(prop);

            neuron.setCenterX(layerGap * (indexOfLayer + 0.5) - (networkPane.getWidth()));
            neuron.setCenterY(heightGap * (indexOfNeuron + 0.5) - (networkPane.getHeight()));
            value.setTranslateX(layerGap * (indexOfLayer + 0.5) - (10));
            value.setTranslateY(heightGap * (indexOfNeuron + 0.5) - (5));

            networkPane.getChildren().addAll(neuron, value);
            neuronList.get(indexOfLayer).add(neuron);
        }
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
        double weight = network.getWeights().get(currLayerIndex).get(currNeuronIndex, prevNeuronIndex);
        weight = Math.abs(weight);
        Line line = new Line();

        DoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);
        line.startXProperty().bind(neuronList.get(currLayerIndex + 1).get(currNeuronIndex).centerXProperty());
        line.startYProperty().bind(neuronList.get(currLayerIndex + 1).get(currNeuronIndex).centerYProperty());
        line.endXProperty().bind(neuronList.get(currLayerIndex).get(prevNeuronIndex).centerXProperty());
        line.endYProperty().bind(neuronList.get(currLayerIndex).get(prevNeuronIndex).centerYProperty());

        line.opacityProperty().bind((value.divide(2)).add(0.5));
        line.strokeWidthProperty().bind(((value.divide(1.5)).add(0.5)).multiply(3));
        //line.translateXProperty().bind();

        line.setOnMouseClicked((e) -> {
            System.out.println(value.get());
        });
        networkPane.getChildren().add(line);
        line.toBack();
    }

    public void addLayer() {
        int size = btnContainer.getChildren().size();
        if (size == MAX_LAYERS) return;

        addLayerButtons();

        network.addLayer(MIN_NEURONS);
        generateLayer(network.getNumberOfLayers() - 2, MIN_NEURONS);
        generateLayerWeights(network.getNumberOfLayers() - 2);
    }

    private void addLayerButtons() {
        HBox subBtnContainer = new HBox();
        subBtnContainer.setSpacing(5);

        subBtnContainer.getChildren().add(new Button("+"));
        subBtnContainer.getChildren().add(new Button("-"));

        btnContainer.getChildren().add(subBtnContainer);
    }

    private void addLayerNeurons() {
        network.addLayer(MIN_NEURONS);
        generateLayer(network.getNumberOfLayers() - 1, MIN_NEURONS);
    }

    private void degenerateWeight(int currNeuronIndex, int prevNeuronIndex, int layerIndex) {
        Line line = lineWeights.get(currNeuronIndex * network.getNumberOfNeurons(layerIndex) + prevNeuronIndex);
        networkPane.getChildren().remove(line);
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

