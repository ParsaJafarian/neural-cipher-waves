package com.nn.display;

import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;

public class WeightLine extends Line {
     WeightLine(@NotNull Neuron prevNeuron, @NotNull Neuron currNeuron) {
        super();
        this.setId("weightLine");
        this.toBack();

        this.startXProperty().bind(prevNeuron.outputXProperty());
        this.startYProperty().bind(prevNeuron.outputYProperty());

        this.endXProperty().bind(currNeuron.inputXProperty());
        this.endYProperty().bind(currNeuron.inputYProperty());
    }
}
