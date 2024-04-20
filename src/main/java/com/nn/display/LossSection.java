package com.nn.display;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class LossSection {
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private final SimpleDoubleProperty lastLoss = new SimpleDoubleProperty(0);

    public LossSection(LineChart<Number, Number> chart, Label trainingLossLabel) {
        chart.getData().clear();
        chart.getData().add(series);

        //bind the lastloss to trainingLossLabel with 2 decimal places
        lastLoss.addListener((observable, oldValue, newValue) -> {
            trainingLossLabel.setText(String.format("%.2f", newValue));
        });
    }

    public void addData(int epoch, double loss) {
        series.getData().add(new XYChart.Data<>(epoch, loss));
        lastLoss.set(loss);
    }

    public void clear(){
        series.getData().clear();
        lastLoss.set(0);
    }
}
