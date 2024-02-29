package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


import java.util.ArrayList;

/**
 * This class consists of the visible neural display accesible in Simulation
 */
public class NetworkDisplay {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int PANE_PADDING = 20;
    private final Pane pane;

    private Network network;
    private ArrayList<Matrix> activations;

    private final ArrayList<Line> lineWeights = new ArrayList<>();
    private ArrayList<ArrayList<Circle>> neuronList;

    /**
     * @param network the network to be displayed
     */
    public NetworkDisplay(Pane pane, Network network) {
        this.pane = pane;
        this.pane.setPrefSize(WIDTH, HEIGHT);
        this.neuronList = new ArrayList<>();
        this.setNetwork(network);
    }

    private void generateNeurons() {
        int numberOfLayers = activations.size();
        int disposableWidth = WIDTH - 2 * PANE_PADDING;
        int disposableHeight = HEIGHT - 2 * PANE_PADDING;
        int layerGap = disposableWidth / (numberOfLayers + 1);

        for (int i = 0; i < activations.size(); i++) {
            neuronList.add(new ArrayList<>());
            //number of activations at layer i (
            int n = activations.get(i).getRows();
            int heightGap = disposableHeight / (n + 1);
            for (int j = 0; j < n; j++) {

                Label value = new Label();
                value.setId("neuronNet");
                DoubleProperty prop = new SimpleDoubleProperty(activations.get(i).get(j, 0));

                value.textProperty().bind(prop.asString("%.2f"));

                Circle neuron = new Circle(20);
                neuron.setUserData(prop);

                neuron.setCenterX(layerGap * (i + 0.5) - (pane.getWidth()));
                neuron.setCenterY(heightGap * (j + 0.5) - (pane.getHeight()));
                value.setTranslateX(layerGap * (i + 0.5) - (10));
                value.setTranslateY(heightGap * (j + 0.5) - (5));

                pane.getChildren().addAll(neuron, value);
                neuronList.get(i).add(neuron);

            }
        }

    }

    private void generateWeight() {
        //for each layer that has weights
        for (int l = 0; l < network.getWeights().size(); l++) {
            for (int currNeuron = 0; currNeuron < network.getSizes()[l + 1]; currNeuron++) {
                for (int prevNeuron = 0; prevNeuron < activations.get(l).getRows(); prevNeuron++) {

                    double k = network.getWeights().get(l).get(currNeuron, prevNeuron);
                    k = Math.abs(k);
                    Line line = new Line();

                    DoubleProperty value = new SimpleDoubleProperty(k);
                    line.setUserData(value);
                    line.startXProperty().bind(neuronList.get(l + 1).get(currNeuron).centerXProperty());
                    line.startYProperty().bind(neuronList.get(l + 1).get(currNeuron).centerYProperty());
                    line.endXProperty().bind(neuronList.get(l).get(prevNeuron).centerXProperty());
                    line.endYProperty().bind(neuronList.get(l).get(prevNeuron).centerYProperty());

                    line.opacityProperty().bind((value.divide(2)).add(0.5));
                    line.strokeWidthProperty().bind(((value.divide(1.5)).add(0.5)).multiply(3));
                    //line.translateXProperty().bind();

                    line.setOnMouseClicked((e) -> {
                        System.out.println(value.get());
                    });
                    pane.getChildren().add(line);
                    line.toBack();
                }
            }
        }
    }

    public void update() {
        for (int i = 0; i < activations.size(); i++) {
            for (int j = 0; j < activations.get(i).getRows(); j++) {
                ((SimpleDoubleProperty) neuronList.get(i).get(j).getUserData()).set(activations.get(i).get(j, 0));
            }
        }
    }

    public void clear() {
        pane.getChildren().clear();
        if (activations != null) activations.clear();
        neuronList.clear();
        lineWeights.clear();
    }

    public void setNetwork(Network network) {
        this.clear();
        this.network = network;

        activations = network.getActivations();
        if (this.activations.isEmpty()) {
            for (int i = 0; i < network.getSizes().length; i++) {
                this.activations.add(new Matrix(network.getSizes()[i], 1));
            }
        }

        generateNeurons();
        generateWeight();
    }
}

