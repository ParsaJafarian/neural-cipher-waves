package com.nn.display;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Neuron extends StackPane {
    public static final int RADIUS = 10;
    private final ArrayList<WeightLine> inputLines = new ArrayList<>();
    private final ArrayList<WeightLine> outputLines = new ArrayList<>();

    /**
     * StackPane representing a neuron that holds the activation value and has connections to other neurons
     * @param activation the activation value of the neuron
     */
    public Neuron(double activation) {
        super();

        setPrefSize(2 * RADIUS, 2 * RADIUS);
        getStyleClass().add("neuron");

        DoubleProperty prop = new SimpleDoubleProperty(activation);
        Circle circle = new Circle(2 * RADIUS);
        circle.setFill(Paint.valueOf("#6f5327"));

        Label value = new Label();
        value.setStyle("-fx-text-fill: white;");
        value.textProperty().bind(prop.asString("%.2f"));
        value.toFront();

        this.setUserData(prop);
        this.toFront();
        this.getChildren().addAll(circle, value);
    }

    /**
     * Remove this neuron from the parent pane
     */
    public void remove(){
        Pane parent = (Pane) this.getParent();
        parent.getChildren().remove(this);

        for (WeightLine inputLine : inputLines)
            parent.getChildren().remove(inputLine);

        for (WeightLine outputLine : outputLines)
            parent.getChildren().remove(outputLine);
    }

    public void connectToPrevNeuron(Neuron prevNeuron) {
        if (this.getParent() == null) return;

        WeightLine weightLine = new WeightLine(prevNeuron, this);
        inputLines.add(weightLine);
        prevNeuron.outputLines.add(weightLine);

        Pane parent = (Pane) this.getParent();
        parent.getChildren().add(weightLine);
    }

    //custom properties so that lines don't go through the center of the neuron

    private DoubleProperty inputXProperty() {
        return this.translateXProperty();
    }

    private DoubleBinding inputYProperty() {
        return this.translateYProperty().add(RADIUS);
    }

    private DoubleBinding outputXProperty() {
        return this.translateXProperty().add(RADIUS).add(RADIUS);
    }

    private DoubleBinding outputYProperty() {
        return this.translateYProperty().add(RADIUS);
    }

    /**
     * Line representing the weight between two neurons
     */
    private static class WeightLine extends Line {
        WeightLine(@NotNull Neuron prevNeuron, @NotNull Neuron currNeuron) {
            super();
            this.toBack();
            this.setStroke(Paint.valueOf("#6f5327"));

            this.startXProperty().bind(prevNeuron.outputXProperty());
            this.startYProperty().bind(prevNeuron.outputYProperty());

            this.endXProperty().bind(currNeuron.inputXProperty());
            this.endYProperty().bind(currNeuron.inputYProperty());
        }
    }
}
