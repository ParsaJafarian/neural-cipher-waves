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


    double angleVal;
    double length;
    double period;
    double angularF;
    double mass= 1;;
    LineChart<Number, Number> chart;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ropeSetCircle(object);
        angleVal = angleSlider.getValue();
        length = lengthSlider.getValue();
        period = (2 * Math.PI) * (Math.sqrt((length) / 9.81));
        angularF = (2 * Math.PI) / period;
        PathTransition pathTransition = new PathTransition(Duration.seconds(period / 2), path, object);
        pathTransition.setAutoReverse(true);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        arcPathCreation(length * 500, angleSlider.getValue(), pathTransition);
        equationCreation();

        //detects and uses the changes made to the length slider by the user
        lengthSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            length = (double) newvalue;
            period = (2 * Math.PI) * (Math.sqrt((length) / 9.81));
            angularF = (2 * Math.PI) / period;
            //since one cycle of transition is half of a period of the motion, we make the duration of the animation period/2
            pathTransition.setDuration(Duration.seconds(period / 2));
            arcPathCreation(length * 500, angleVal, pathTransition);

            lengthSlider.setOnMouseReleased(e -> equationCreation());
        });

        //detects and uses the changes made to the angle slider by the user
        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            angleVal = (double) newvalue;
            arcPathCreation(length * 500, angleVal, pathTransition);
            angleSlider.setOnMouseReleased(e -> equationCreation());
        });

        //changes the object attached to the pendulum string when clicking on the specified buttons
        Shape1.setOnAction(e -> {
            setNewObjectCircle(pathTransition, object);
            mass =1;
            equationCreation();
        });
        Shape2.setOnAction(e -> {
            setNewObjectCircle(pathTransition, object2);
            mass =10;
            equationCreation();
        });
        Shape3.setOnAction(e -> {
            setNewObjectCircle(pathTransition, object3);
            mass =50;
            equationCreation();
        });
        Shape4.setOnAction(e -> {
            setNewObjectCircle(pathTransition, object4);
            mass =100;
            equationCreation();
        });

        //exits the physics simulation
        exit.setOnAction(e -> {
            try {
                exit.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml"))));
            } catch (IOException ignored) {
            }
        });
    }

    /**
     * changes the object that is attrached to the string of the pendulum
     * @param pathTransition the path transition used to simulate the motion of the pendulum
     * @param shape the shape that will be used as the object of the pendulum/node of the transition.
     */
    public void setNewObjectCircle(PathTransition pathTransition, Circle shape) {
        if (!pathTransition.getNode().equals(shape)) {
            pathTransition.getNode().setVisible(false);
        }
        ropeSetCircle(shape);
        pathTransition.setNode(shape);
        pathTransition.stop();
        pathTransition.play();
    }

    /**
     * binds the rope of the pendulum to the new object
     * @param obj object that is used for the pendulum
     */
    public void ropeSetCircle(Circle obj) {
        rope.startXProperty().bind(pivot.centerXProperty());
        rope.startYProperty().bind(pivot.centerYProperty());
        rope.endXProperty().bind(obj.translateXProperty().add(obj.getCenterX()));
        rope.endYProperty().bind(obj.translateYProperty().add(obj.getCenterY()));
        obj.setVisible(true);
    }

    /**
     * creates the arc that is used as the path of the path transition.
     * @param length the length of the string of the pendulum
     * @param angle the starting/maximum angle that is chosen by the user using the angle slider
     * @param pathTransition the path transition that is used for the motion of the pendulum
     */
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

    /**
     *  Uses the static methods that are instantiated whenever the sliders are changed by the user in order to create the equation for the simple harmonic motion
     */
    public void equationCreation() {
        angularVelocityText.setText(Math.round(angularF * 100.0) / 100.0 + " rad/s");
        periodText.setText(Math.round(period * 100.0) / 100.0 + " seconds");
        lengthText.setText(Math.round(length * 100.0) / 100.0 + " meters");
        String equ = Math.round((angleVal * (Math.PI / 180)) * 100.0) / 100.0 + "sin" + "(" + Math.round(angularF * 100.0) / 100.0 + "t" + ")";
        equationText.setText(equ);
        graphCreation();
    }

    /**
     * creates the graph that shows the changes in potential and kinetic energy in a single cycle
     */
    public void graphCreation() {
        //removes the previous data that is displayed on the chart
        back.getChildren().remove(chart);

        //stores the time it takes for the pendulum to make one cycle as a double with 2 decimal places
        double dur = Math.round(period * 100.0) / 100.0;

        //calculates the total energy of the system using a mathematical equation
        double totalE = (length - (length * Math.cos((angleVal * (Math.PI / 180))))) * 9.81 * mass;

        //creates the array lists needed to store all the data of the graph
        ArrayList<Double> potential = new ArrayList<>();
        ArrayList<Double> kinetic = new ArrayList<>();
        ArrayList<Double> time = new ArrayList<>();

        //creates the axis of the graph and sets their values + names
        NumberAxis x = new NumberAxis(0, dur, 1);
        x.setLabel("Time (period)");
        NumberAxis y = new NumberAxis(0, totalE, 1);
        y.setLabel("energy");

        //instantiates the LineChart and establishes their dimensions
        chart = new LineChart<>(x, y);
        chart.setMaxHeight(350);
        chart.setMaxWidth(400);

        //using an empty line chart to set its position in the right place
        chart.setLayoutX(EChart.getLayoutX() - 20);
        chart.setLayoutY(EChart.getLayoutY() - 20);

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
        }

        //creates the line that represents the potential energy changes and adds the data.
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

        //creates the line that represents the kinetic energy changes and adds the data.
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Kinetic Energy");
        for (int i = 0; i < time.size(); i++) {
            series2.getData().add(new XYChart.Data<>(time.get(i), kinetic.get(i)));
        }

        //adds the lines to the chart.
        chart.getData().add(series);
        chart.getData().add(series2);
        chart.setCreateSymbols(false);
        back.getChildren().add(chart);
    }



    //GETTERS AND SETTERS

    public Line getRope() {
        return rope;
    }

    public void setRope(Line rope) {
        this.rope = rope;
    }

    public Arc getPath() {
        return path;
    }

    public void setPath(Arc path) {
        this.path = path;
    }

    public Circle getPivot() {
        return pivot;
    }

    public void setPivot(Circle pivot) {
        this.pivot = pivot;
    }

    public Circle getObject2() {
        return object2;
    }

    public void setObject2(Circle object2) {
        this.object2 = object2;
    }

    public Button getShape1() {
        return Shape1;
    }

    public void setShape1(Button shape1) {
        Shape1 = shape1;
    }

    public Slider getLengthSlider() {
        return lengthSlider;
    }

    public void setLengthSlider(Slider lengthSlider) {
        this.lengthSlider = lengthSlider;
    }

    public Slider getAngleSlider() {
        return angleSlider;
    }

    public void setAngleSlider(Slider angleSlider) {
        this.angleSlider = angleSlider;
    }

    public Line getLineReference() {
        return lineReference;
    }

    public void setLineReference(Line lineReference) {
        this.lineReference = lineReference;
    }

    public AnchorPane getBack() {
        return back;
    }

    public void setBack(AnchorPane back) {
        this.back = back;
    }

    public Label getAngularVelocityText() {
        return angularVelocityText;
    }

    public void setAngularVelocityText(Label angularVelocityText) {
        this.angularVelocityText = angularVelocityText;
    }

    public Label getPeriodText() {
        return periodText;
    }

    public void setPeriodText(Label periodText) {
        this.periodText = periodText;
    }

    public Label getLengthText() {
        return lengthText;
    }

    public void setLengthText(Label lengthText) {
        this.lengthText = lengthText;
    }

    public Label getEquationText() {
        return equationText;
    }

    public void setEquationText(Label equationText) {
        this.equationText = equationText;
    }

    public LineChart<?, ?> getEChart() {
        return EChart;
    }

    public void setEChart(LineChart<?, ?> EChart) {
        this.EChart = EChart;
    }

    public Button getExit() {
        return exit;
    }

    public void setExit(Button exit) {
        this.exit = exit;
    }

    public Button getShape2() {
        return Shape2;
    }

    public void setShape2(Button shape2) {
        Shape2 = shape2;
    }

    public Button getShape3() {
        return Shape3;
    }

    public void setShape3(Button shape3) {
        Shape3 = shape3;
    }

    public Button getShape4() {
        return Shape4;
    }

    public void setShape4(Button shape4) {
        Shape4 = shape4;
    }

    public Circle getObject3() {
        return object3;
    }

    public void setObject3(Circle object3) {
        this.object3 = object3;
    }

    public Circle getObject4() {
        return object4;
    }

    public void setObject4(Circle object4) {
        this.object4 = object4;
    }

    public double getAngleVal() {
        return angleVal;
    }

    public void setAngleVal(double angleVal) {
        this.angleVal = angleVal;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public double getAngularF() {
        return angularF;
    }

    public void setAngularF(double angularF) {
        this.angularF = angularF;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public LineChart<Number, Number> getChart() {
        return chart;
    }

    public void setChart(LineChart<Number, Number> chart) {
        this.chart = chart;
    }
}
