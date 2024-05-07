package com.waves;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author david
 */
public class PendulumController implements Initializable {

    @FXML
    private Line rope;
    @FXML
    private Circle object;
    @FXML
    private Arc path;
    @FXML
    private Circle pivot;
    @FXML
    private Circle object2;
    @FXML
    private Button Shape1;
    @FXML
    private Slider lengthSlider;
    @FXML
    private Slider angleSlider;
    @FXML
    private Line lineReference;
    @FXML
    private AnchorPane back;
    @FXML
    private Label angularVelocityText;
    @FXML
    private Label periodText;
    @FXML
    private Label lengthText;
    @FXML
    private Label equationText;
    @FXML
    private LineChart<?, ?> EChart;
    @FXML
    private Button exit;

    double angleVal;
    double length;
    double period;
    double angularF;
    double mass;
    LineChart<Number, Number> r;
    @FXML
    private Button Shape2;
    @FXML
    private Button Shape3;
    @FXML
    private Button Shape4;
    @FXML
    private Circle object3;
    @FXML
    private Circle object4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ropeSetCircle(object);
        mass = 1;
        angleVal = angleSlider.getValue();
        length = lengthSlider.getValue();
        period = (2 * Math.PI) * (Math.sqrt((length) / 9.81));
        angularF = (2 * Math.PI) / period;
        PathTransition pathTransition = new PathTransition(Duration.seconds(period / 2), path, object);
        pathTransition.setAutoReverse(true);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        arcPathCreation(length * 500, angleSlider.getValue(), pathTransition);
        equationCreation();

        lengthSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            length = (double) newvalue;
            period = (2 * Math.PI) * (Math.sqrt((length) / 9.81));
            angularF = (2 * Math.PI) / period;
            pathTransition.setDuration(Duration.seconds(period / 2));
            arcPathCreation(length * 500, angleVal, pathTransition);
            lengthSlider.setOnMouseReleased(e -> equationCreation());
        });
        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            angleVal = (double) newvalue;
            arcPathCreation(length * 500, angleVal, pathTransition);
            angleSlider.setOnMouseReleased(e -> equationCreation());
        });

        Shape1.setOnAction(e -> setNewObjectCircle(pathTransition, object));
        Shape2.setOnAction(e -> setNewObjectCircle(pathTransition, object2));
        Shape3.setOnAction(e -> setNewObjectCircle(pathTransition, object3));
        Shape4.setOnAction(e -> setNewObjectCircle(pathTransition, object4));

        exit.setOnAction(e -> {
            try {
                //changes the root of the scene to direct the user to the slideshow before the race starts
                exit.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml"))));
            } catch (IOException ignored) {
            }
        });
    }

    public void setNewObjectCircle(PathTransition pathTransition, Circle shape) {
        if (!pathTransition.getNode().equals(shape)) {
            pathTransition.getNode().setVisible(false);
        }
        ropeSetCircle(shape);
        mass = 2;
        pathTransition.setNode(shape);
        pathTransition.stop();
        pathTransition.play();
    }

    public void ropeSetCircle(Circle obj) {
        rope.startXProperty().bind(pivot.centerXProperty());
        rope.startYProperty().bind(pivot.centerYProperty());
        rope.endXProperty().bind(obj.translateXProperty().add(obj.getCenterX()));
        rope.endYProperty().bind(obj.translateYProperty().add(obj.getCenterY()));
        obj.setVisible(true);
    }

    public void arcPathCreation(double length, double angle, PathTransition pathTransition) {
        double rad = angle * (Math.PI / 180);
        double yDifference = length - (length * Math.cos(rad));
        double xDifference = length * Math.sin(rad);

        path.setLayoutX(0);
        path.setLayoutY(0);
        path.setCenterX(lineReference.getLayoutX());
        path.setCenterY(length);
        path.setRadiusX(xDifference);
        path.setRadiusY(yDifference);
        pathTransition.stop();
        pathTransition.play();
    }

    public void equationCreation() {
        angularVelocityText.setText(Math.round(angularF * 100.0) / 100.0 + " rad/s");
        periodText.setText(Math.round(period * 100.0) / 100.0 + " seconds");
        lengthText.setText(Math.round(length * 100.0) / 100.0 + " meters");
        String equ = Math.round((angleVal * (Math.PI / 180)) * 100.0) / 100.0 + "sin" + "(" + Math.round(angularF * 100.0) / 100.0 + "t" + ")";
        equationText.setText(equ);
        graphCreation();
    }

    public void graphCreation() {
        back.getChildren().remove(r);
        double dur = Math.round(period * 100.0) / 100.0;
        double totalE = (length - (length * Math.cos((angleVal * (Math.PI / 180))))) * 9.81 * mass;
        ArrayList<Double> potential = new ArrayList<>();
        ArrayList<Double> kinetic = new ArrayList<>();
        ArrayList<Double> time = new ArrayList<>();
        NumberAxis x = new NumberAxis(0, dur, 1);
        x.setLabel("Time (period)");
        NumberAxis y = new NumberAxis(0, totalE, 1);
        y.setLabel("energy");

        r = new LineChart<>(x, y);
        r.setMaxHeight(350);
        r.setMaxWidth(400);
        r.setLayoutX(EChart.getLayoutX() - 20);
        r.setLayoutY(EChart.getLayoutY() - 20);
        //creating arrayList for time values for creating energy graphs
        for (double i = 0; i < dur * 2; i += 0.01) {
            time.add(i);
        }

        //Potential energy values according to the time values created above
        for (Double aDouble : time) {
            double angle = (angleVal * (Math.PI / 180)) * Math.cos((angularF) * aDouble);
            double heightDifference = length - (length * Math.cos(angle));
            double val = mass * 9.81 * heightDifference;
            potential.add(val);
            System.out.println(val);
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Potential Energy");
        for (int i = 0; i < time.size(); i++) {
            series.getData().add(new XYChart.Data<>(time.get(i), potential.get(i)));
        }

        //Total energy value: TotalE
        //Kinetic energy values according to the time values created above
        for (int i = 0; i < time.size(); i++) {
            double val = totalE - potential.get(i);
            kinetic.add(val);
        }
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Kinetic Energy");
        for (int i = 0; i < time.size(); i++) {
            series2.getData().add(new XYChart.Data<>(time.get(i), kinetic.get(i)));
        }

        r.getData().add(series);
        r.getData().add(series2);
        r.setCreateSymbols(false);
        back.getChildren().add(r);
    }
}
