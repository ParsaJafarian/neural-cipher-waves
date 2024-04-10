package com.nn;

import javafx.scene.shape.Line;

public class WeightLine extends Line {
    public WeightLine(Neuron prevNeuron, Neuron currNeuron) {
        super();
        this.setId("weightLine");
        this.toBack();

        this.startXProperty().bind(prevNeuron.outputXProperty());
        this.startYProperty().bind(prevNeuron.outputYProperty());

        this.endXProperty().bind(currNeuron.inputXProperty());
        this.endYProperty().bind(currNeuron.inputYProperty());
    }
}
