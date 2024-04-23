package com.nn.utils;

import com.nn.algo.NeuralNetwork;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PredictionSection {
    private final NeuralNetwork network;
    public PredictionSection(NeuralNetwork network, TextField predInputField, Label predOutputLabel, Button predictBtn){
        this.network = network;
        predInputField = new NumberField();

        predictBtn.setOnAction(e -> {
        });
    }
}
