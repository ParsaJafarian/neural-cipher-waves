package com.nn.display;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Neuron extends StackPane {
    public static final int RADIUS = 10;

    public Neuron(double activation) {
        super();

        setPrefSize(2 * RADIUS, 2 * RADIUS);
        getStyleClass().add("neuron");

        DoubleProperty prop = new SimpleDoubleProperty(activation);

        Circle circle = new Circle(2 * RADIUS);

        Label value = new Label();
        value.setId("activationValue");
        value.textProperty().bind(prop.asString("%.2f"));
        value.toFront();

        this.setUserData(prop);
        this.toFront();
        this.getChildren().addAll(circle, value);
    }

    public DoubleProperty inputXProperty() {
        return this.translateXProperty();
    }

    public DoubleBinding inputYProperty() {
        return this.translateYProperty().add(RADIUS);
    }

    public DoubleBinding outputXProperty() {
        return this.translateXProperty().add(RADIUS).add(RADIUS);
    }

    public DoubleBinding outputYProperty() {
        return this.translateYProperty().add(RADIUS);
    }
}
