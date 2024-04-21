package com.nn.display;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class LossSection {
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private final SimpleDoubleProperty lastLoss = new SimpleDoubleProperty(0);
    private final AtomicInteger epoch;

    public LossSection(@NotNull LineChart<Number, Number> chart, Label trainingLossLabel, AtomicInteger epoch) {
        chart.getData().clear();
        chart.getData().add(series);

        //bind the lastloss to trainingLossLabel with 2 decimal places
        lastLoss.addListener((observable, oldValue, newValue) -> {
            trainingLossLabel.setText(String.format("%.2f", newValue));
        });

        this.epoch = epoch;
    }

    public void addData(double loss) {
        series.getData().add(new XYChart.Data<>(epoch.getAndIncrement(), loss));
        lastLoss.set(loss);
    }

    public void clear(){
        series.getData().clear();
        lastLoss.set(0);
    }
}
