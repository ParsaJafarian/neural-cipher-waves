package com.nn.display;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

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

    void setAddNeuronAction(Runnable action) {
        ((Button) this.getChildren().get(0)).setOnAction(e -> action.run());
    }

    void setRemoveNeuronAction(Runnable action) {
        ((Button) this.getChildren().get(1)).setOnAction(e -> action.run());
    }
}
