package com.nn.utils;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * A container for buttons to add and remove neurons
 */
public class ButtonContainer extends HBox {
    public ButtonContainer() {
        super();
        this.setSpacing(5);
        this.toFront();

        Button addNeuronBtn = new Button("+");
        Button removeNeuronBtn = new Button("-");

        this.getChildren().add(addNeuronBtn);
        this.getChildren().add(removeNeuronBtn);
    }

    /**
     * Set the action to be performed when the add neuron button is clicked
     * @param action the action to be performed
     */
    public void setAddNeuronAction(Runnable action) {
        ((Button) this.getChildren().get(0)).setOnAction(e -> action.run());
    }

    /**
     * Set the action to be performed when the remove neuron button is clicked
     * @param action the action to be performed
     */
    public void setRemoveNeuronAction(Runnable action) {
        ((Button) this.getChildren().get(1)).setOnAction(e -> action.run());
    }
}
