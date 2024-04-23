package com.nn.utils;

import javafx.scene.control.TextField;

public class NumberField extends TextField {
    public NumberField() {
        super();
        this.setPromptText("Enter number");

        this.makeFieldNumeric();
        this.limitField();
        this.focusWhenEmpty();

        this.setMaxWidth(40);
        this.setStyle("-fx-alignment: center;");
        this.setText("0");
    }

    public double getNumber(){
        return Double.parseDouble(this.getText());
    }

    private void makeFieldNumeric() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[\\d-.]+$")) {
                //removes last character if it doesn't match description above
                if (this.getText().isEmpty()) this.setText("");
                else this.setText(this.getText().substring(0, this.getText().length() - 1));
            }
        });
    }

    private void limitField() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty() && Double.parseDouble(newValue) > 100) this.setText(oldValue);
            } catch (Exception e) {
                this.setText(oldValue);
            }
        });
    }

    private void focusWhenEmpty() {
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && this.getText().isEmpty())
                this.requestFocus();
        });
    }
}
