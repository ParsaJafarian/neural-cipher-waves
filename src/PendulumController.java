/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

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
    private Rectangle border;
    @FXML
    private Circle pivot;
    @FXML
    private Line rope2;
    @FXML
    private Circle object2;
    private Arc path2;
    @FXML
    private Button Shape1;
    @FXML
    private Button button2;
    @FXML
    private Slider lengthSlider;
    @FXML
    private Slider angleSlider;

    double angleVal;
    double length;
    double period;
    double angularF;
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
    private Label equationText1;
    double pendulumStartingHeight;
    double mass;
    @FXML
    private LineChart<?, ?> EChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ropeSet(object);
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
            lengthSlider.setOnMouseReleased(e->{
            equationCreation();
            });
        });
        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            angleVal = (double) newvalue;
            arcPathCreation(length * 500, angleVal, pathTransition);
            angleSlider.setOnMouseReleased(e->{
            equationCreation();
            });
        });

        Shape1.setOnAction(e -> {
            if (!pathTransition.getNode().equals(object2)) {
                pathTransition.getNode().setVisible(false);
            }
            ropeSet(object2);
            mass = 2;
            pathTransition.setNode(object2);
            pathTransition.stop();
            pathTransition.play();
        });

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

    public void ropeSet(Circle obj) {
        rope.startXProperty().bind(pivot.centerXProperty());
        rope.startYProperty().bind(pivot.centerYProperty());
        rope.endXProperty().bind(obj.translateXProperty().add(obj.getCenterX()));
        rope.endYProperty().bind(obj.translateYProperty().add(obj.getCenterY()));
    }

    public void equationCreation() {
        angularVelocityText.setText(String.valueOf(Math.round(angularF * 100.0) / 100.0) + " rad/s");
        periodText.setText(String.valueOf(Math.round(period * 100.0) / 100.0) + " seconds");
        lengthText.setText(String.valueOf(Math.round(length * 100.0) / 100.0) + " meters");
        String equ = String.valueOf(Math.round((angleVal * (Math.PI / 180)) * 100.0) / 100.0) + "sin" + "(" + Math.round(angularF * 100.0) / 100.0 + "t" + ")";
        equationText.setText(equ);
        graphCreation();
    }

    LineChart r;

    public void graphCreation() {
        back.getChildren().remove(r);
        double dur = Math.round(period * 100.0) / 100.0;
        double totalE = (length - (length * Math.cos((angleVal*(Math.PI/180))))) * 9.81 * mass;
        ArrayList potential = new ArrayList<>();
        ArrayList kinetic = new ArrayList<>();
        ArrayList time = new ArrayList<>();
        NumberAxis x = new NumberAxis(0, dur, 1);
        x.setLabel("Time (period)");
        NumberAxis y = new NumberAxis(0, totalE, 1);
        y.setLabel("energy");

        r = new LineChart(x, y);
        r.setMaxHeight(350);
        r.setMaxWidth(400);
        r.setLayoutX(EChart.getLayoutX()-20);
        r.setLayoutY(EChart.getLayoutY()-20);
        //creating arrayList for time values for creating energy graphs
        for (double i = 0; i < dur*2; i += 0.01) {
            time.add(i);
        }

        //Potential enrrgy values according to the time values created above
        for (int i = 0; i < time.size(); i++) {
            double angle = (angleVal*(Math.PI / 180)) * Math.cos((angularF) * (double) time.get(i));
            double heightDifference = length - (length * Math.cos(angle));
            double val = mass*9.81*heightDifference;
            potential.add(val);
            System.out.println(val);
        }
        XYChart.Series series = new XYChart.Series<>();
        series.setName("Potential Energy");
        for (int i = 0; i < time.size(); i++) {
            series.getData().add(new XYChart.Data(time.get(i), potential.get(i)));
        }

        //Total energy value: TotalE
        //Kinetic enrrgy values according to the time values created above
        for (int i = 0; i < time.size(); i++) {
            double val = totalE - (double) potential.get(i);
            kinetic.add(val);
        }
        XYChart.Series series2 = new XYChart.Series<>();
        series2.setName("Kinetic Energy");
        for (int i = 0; i < time.size(); i++) {
            series2.getData().add(new XYChart.Data(time.get(i), kinetic.get(i)));
        }

        r.getData().addAll(series, series2);
        r.setCreateSymbols(false);
        back.getChildren().add(r);
    }
}
