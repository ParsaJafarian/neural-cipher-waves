package com.nn;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;

/**
 * This class consists of the visible neural display accesible in Simulation
 */
public class NetworkDisplay {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    public static final int MIN_LAYERS = 2, MAX_LAYERS = 6;
    public static final int MIN_NEURONS = 2, MAX_NEURONS = 8;
    private final HBox networkContainer;

    private final Network network;

    private final ArrayList<Line> lineWeights = new ArrayList<>();
    private final ArrayList<ArrayList<Circle>> layers;
    private final ArrayList<Matrix> activations;

    public NetworkDisplay(HBox networkContainer) {
        this.networkContainer = networkContainer;
        this.networkContainer.setPrefSize(WIDTH, HEIGHT);

        this.layers = new ArrayList<>();

        // Initialize the network with an invisible input layer
        this.network = new Network(0.01, 10);
        this.activations = network.getActivations();

        addInitialHiddenLayers();
    }

    private void addInitialHiddenLayers() {
        for (int i = 0; i < MIN_LAYERS; i++)
            addLayer(MIN_NEURONS);
    }

    public void addLayer(int numberOfNeurons) {
        if (network.getNumLayers() > MAX_LAYERS) return;

        network.addLayer(MIN_NEURONS);

        VBox layerContainer = new VBox();
        layerContainer.setAlignment(Pos.CENTER);
        layerContainer.setSpacing(5);

        networkContainer.getChildren().add(layerContainer);

        addLayerButtons(layerContainer);

        Matrix lastActivation = activations.get(activations.size() - 1);

        for (int neuronIndex = 0; neuronIndex < numberOfNeurons; neuronIndex++) {
            double activation = lastActivation.get(neuronIndex, 0);
            addNeuron(layerContainer, activation);
        }
    }

    public void addNeuron(@NotNull VBox layerContainer, double activation){
        if (layerContainer.getChildren().size() > MAX_NEURONS) return;

        //Pane that contains the neuron and its value
        StackPane neuronPane = new StackPane();

        Label value = new Label();
        value.toFront();
        value.setId("activationValue");

        DoubleProperty prop = new SimpleDoubleProperty(activation);
        value.textProperty().bind(prop.asString("%.2f"));

        Circle neuron = new Circle(20);
        neuron.setUserData(prop);

        neuronPane.getChildren().add(neuron);
        neuronPane.getChildren().add(value);

        layerContainer.getChildren().add(neuronPane);
    }

    /**
     * Add buttons to add or remove neurons from the layer
     * @param layerContainer container to add the buttons to
     */
    private void addLayerButtons(@NotNull VBox layerContainer) {
        HBox btnContainer = new HBox();
        btnContainer.setSpacing(5);

        Button addNeuronBtn = new Button("+");
        Button removeNeuronBtn = new Button("-");

//        addNeuronBtn.setOnAction(e -> {
//            int layerIndex = getLayerIndex(layerContainer);
//            network.addNeuron(layerIndex);
//            addNeuron(layerContainer, 0);
//        });

        btnContainer.getChildren().add(addNeuronBtn);
        btnContainer.getChildren().add(removeNeuronBtn);

        layerContainer.getChildren().add(btnContainer);
    }

    private void degenerateWeight(int currNeuronIndex, int prevNeuronIndex, int layerIndex) {
        Line line = lineWeights.get(currNeuronIndex * network.getNumNeurons(layerIndex) + prevNeuronIndex);
        networkContainer.getChildren().remove(line);
    }

    private void generateWeights() {
        for (int i = 0; i < network.getWeights().size(); i++) {
            generateLayerWeights(i);
        }
    }

    private void generateLayerWeights(int layerIndex) {
        int currNumNeurons = network.getNumNeurons(layerIndex + 1);
        for (int currNeuron = 0; currNeuron < currNumNeurons; currNeuron++) {
            int prevNumNeurons = activations.get(layerIndex).getRows();
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
        VBox layer = (VBox) networkContainer.getChildren().get(currLayerIndex);

        SimpleDoubleProperty value = new SimpleDoubleProperty(weight);
        line.setUserData(value);

        Circle currNeuron = layers.get(currLayerIndex + 1).get(currNeuronIndex);
        Circle prevNeuron = layers.get(currLayerIndex).get(prevNeuronIndex);

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

    public void clear() {
        network.clear();
        update();
    }

    public void update() {
        for (int i = 0; i < activations.size(); i++)
            for (int j = 0; j < activations.get(i).getRows(); j++)
                ((SimpleDoubleProperty) layers.get(i).get(j).getUserData()).set(activations.get(i).get(j, 0));
    }
}

