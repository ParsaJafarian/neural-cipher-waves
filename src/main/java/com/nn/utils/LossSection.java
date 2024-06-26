package com.nn.utils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

/**
 * A section to display the loss of the neural network during training
 */
public class LossSection {
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private final SimpleDoubleProperty lastLoss = new SimpleDoubleProperty(0);
    private final SimpleIntegerProperty epoch = new SimpleIntegerProperty(0);

    /**
     * Initialize the loss section with a line chart and labels to display the loss and epoch
     *
     * @param chart             The line chart to display the loss
     * @param trainingLossLabel The label to display the training loss
     * @param epochLabel        The label to display the epoch
     */
    public LossSection(@NotNull LineChart<Number, Number> chart, Label trainingLossLabel, Label epochLabel) {
        chart.getData().clear();
        chart.getData().add(series);
        chart.setCreateSymbols(false);

        series.getNode().setStyle("-fx-stroke: black; -fx-stroke-width: 1;");

        lastLoss.addListener((observable, oldValue, newValue) -> {
            trainingLossLabel.setText(String.format("%.2f", newValue));
        });

        epoch.addListener((observable, oldValue, newValue) -> {
            epochLabel.setText(String.valueOf(newValue));
        });
    }

    /**
     * Update epoch and loss data
     *
     * @param loss The loss value to add
     */
    public void addData(double loss) {
        epoch.set(epoch.get() + 1);
        series.getData().add(new XYChart.Data<>(epoch.get(), loss));
        lastLoss.set(loss);
    }

    /**
     * Clear the loss data
     */
    public void clear() {
        series.getData().clear();
        lastLoss.set(0);
        epoch.set(0);
    }
}
