/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ropeSet(object);
        angleVal = angleSlider.getValue();
        length = lengthSlider.getValue();
        period = (2*Math.PI)*(Math.sqrt((length)/9.81));
        angularF= (2*Math.PI)/period;
        PathTransition pathTransition = new PathTransition(Duration.seconds(period/2), path, object);
        pathTransition.setAutoReverse(true);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        arcPathCreation(length*500, angleSlider.getValue(), pathTransition);
        

        lengthSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            length = (double) newvalue;
            period = (2*Math.PI)*(Math.sqrt((length)/9.81));
            angularF= (2*Math.PI)/period;
            pathTransition.setDuration(Duration.seconds(period/2));
            arcPathCreation(length*500, angleVal,pathTransition);
        });
        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            angleVal = (double) newvalue;
            arcPathCreation(length*500, angleVal,pathTransition);
        });

        Shape1.setOnAction(e -> {
            if (!pathTransition.getNode().equals(object2)) {
                pathTransition.getNode().setVisible(false);
            }
            ropeSet(object2);
            pathTransition.setNode(object2);
            pathTransition.stop();
            pathTransition.play();
        });

    }

    public void arcPathCreation(double length, double angle, PathTransition pathTransition) {
        double degrees = angle * (Math.PI / 180);
        double yDifference = length - (length * Math.cos(degrees));
        double xDifference = length* Math.sin(degrees);
        equationCreation();
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
        periodText.setText(String.valueOf(Math.round(period* 100.0) / 100.0) + " seconds");
        lengthText.setText(String.valueOf(Math.round(length * 100.0) / 100.0) + " meters");
        String equ = String.valueOf(Math.round((angleVal * (Math.PI / 180)) * 100.0) / 100.0) + "sin" + "(" + Math.round(angularF * 100.0) / 100.0 + "t" + ")";
        equationText.setText(equ);
    }
}
