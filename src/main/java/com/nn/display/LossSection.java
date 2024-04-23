package com.nn.display;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;


public class LossSection {
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private final SimpleDoubleProperty lastLoss = new SimpleDoubleProperty(0);
    private final SimpleIntegerProperty epoch = new SimpleIntegerProperty(0);

    public LossSection(@NotNull LineChart<Number, Number> chart, Label trainingLossLabel, Label epochLabel) {
        chart.getData().clear();
        chart.getData().add(series);

        //bind the lastloss to trainingLossLabel with 2 decimal places
        lastLoss.addListener((observable, oldValue, newValue) -> {
            trainingLossLabel.setText(String.format("%.2f", newValue));
        });

        //bind the epoch to epochLabel
        epoch.addListener((observable, oldValue, newValue) -> {
            epochLabel.setText(String.valueOf(newValue));
        });
    }

    public void addData(double loss) {
        epoch.set(epoch.get() + 1);
        series.getData().add(new XYChart.Data<>(epoch.get(), loss));
        lastLoss.set(loss);
    }

    public void clear(){
        series.getData().clear();
        lastLoss.set(0);
        epoch.set(0);
    }
}
