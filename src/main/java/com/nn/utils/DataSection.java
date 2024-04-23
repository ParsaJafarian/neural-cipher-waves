package com.nn.utils;

import com.nn.algo.Matrix;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.nn.display.NeuralNetworkConfig.*;

public class DataSection {
    private final VBox inputBox, outputBox;

    public DataSection(VBox inputBox, @NotNull HBox inputBtns, VBox outputBox, @NotNull HBox outputBtns) {
        Button inputAdderBtn = (Button) inputBtns.getChildren().get(0);
        Button inputRemoverBtn = (Button) inputBtns.getChildren().get(1);

        Button outputAdderBtn = (Button) outputBtns.getChildren().get(0);
        Button outputRemoverBtn = (Button) outputBtns.getChildren().get(1);

        this.inputBox = inputBox;
        this.outputBox = outputBox;

        inputBox.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            FIRST_LAYER_NEURONS.set(inputBox.getChildren().size());
        });

        outputBox.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            LAST_LAYER_NEURONS.set(outputBox.getChildren().size());
        });

        initializeBox(inputBox, inputAdderBtn, inputRemoverBtn);
        initializeBox(outputBox, outputAdderBtn, outputRemoverBtn);
    }

    private void initializeBox(VBox box, @NotNull Button adderBtn, @NotNull Button removerBtn) {
        //add initial text fields
        for (int i = 0; i < MIN_NEURONS; i++)
            box.getChildren().add(new NumberField());

        adderBtn.setOnAction(e -> {
            if (box.getChildren().size() < MAX_NEURONS)
                box.getChildren().add(new NumberField());
        });

        removerBtn.setOnAction(e -> {
            if (box.getChildren().size() > MIN_NEURONS)
                box.getChildren().remove(box.getChildren().size() - 1);
        });
    }

    public Matrix[][] getData() {
        return new Matrix[][]{new Matrix[]{getInputData(), getOutputData()}};
    }

    @Contract(" -> new")
    private @NotNull Matrix getInputData() {
        return getData(inputBox);
    }

    @Contract(" -> new")
    private @NotNull Matrix getOutputData() {
        return getData(outputBox);
    }

    @Contract("_ -> new")
    private @NotNull Matrix getData(@NotNull VBox box) {
        double[][] data = new double[box.getChildren().size()][1];
        for (int i = 0; i < box.getChildren().size(); i++) {
            NumberField numberField = (NumberField) box.getChildren().get(i);
            data[i][0] = numberField.getNumber();
        }
        return new Matrix(data);
    }
}
