package com.waves;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author david
 */
public class VisualController implements Initializable {

    @FXML
    private Circle path;
    @FXML
    private Circle node;
    @FXML
    private AnchorPane back;
    @FXML
    private Slider freq;
    @FXML
    private Slider amp;
    @FXML
    private Circle border;
    @FXML
    private Label equation;
    @FXML
    private Label angF;
    @FXML
    private Label period;
    @FXML
    public Button exit;
    ArrayList<Circle> dots = new ArrayList<>();
    String angularF;
    boolean cont = true;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                dotCreation();
            }
        };
        timer.start();

        double borderRadius = border.getRadius();
        double pathRadius = path.getRadius();
        amp.setValue(0);

        PathTransition pathTransition = new PathTransition(new Duration(2000), path, node);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.play();

        setAngularVelocityAndPeriod(pathTransition);
        equationCreation();

        freq.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            cont = false;
            pathTransition.setRate((double) newvalue);
            setAngularVelocityAndPeriod(pathTransition);
            freq.setOnMouseReleased(e->{
                equationCreation();
                cont = true;
            });
        });

        amp.setOnMousePressed(e -> {
            cont = false;
            amp.valueProperty().addListener((observable, oldvalue, newvalue) -> {

                pathTransition.stop();
                //angle = (Math.atan2(pathTransition.getNode().getTranslateY() - path.getLayoutY(), pathTransition.getNode().getTranslateX() - path.getLayoutX()) * 180) / Math.PI;
                border.setRadius(borderRadius + (double) newvalue);
                path.setRadius(pathRadius + (double) newvalue);
                //path.setRotate(angle);
                node.setVisible(false);
                equationCreation();
            });
        });

        amp.setOnMouseReleased(e -> {
            cont = true;
            node.setVisible(true);
            pathTransition.play();
        });

        exit.setOnAction(e -> {
            try {
                //changes the root of the scene to direct the user to the slideshow before the race starts
                exit.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml"))));
            } catch (IOException ignored) {
            }
        });

    }

    public void dotCreation() {
        Platform.runLater(() -> {
            if (cont) {
                Circle dot = new Circle(600+amp.getValue(), node.getTranslateY(), 5, Color.WHITE);
                back.getChildren().add(dot);
                TranslateTransition translate = new TranslateTransition(new Duration(10000), dot);
                dots.add(dot);
                translate.setInterpolator(Interpolator.LINEAR);
                translate.setByX(2000);
                translate.play();
                translate.setOnFinished(e -> back.getChildren().remove(translate.getNode()));
            }
            else{
                back.getChildren().removeAll(dots);
            }
        });
    }

    public void setAngularVelocityAndPeriod(PathTransition pathTransition) {
        if (pathTransition.getRate() == 0) {
            period.setText("no movement");
            angF.setText("no movement");
        } else {
            double dur = Math.round((pathTransition.getDuration().toSeconds() / pathTransition.getRate()) * 100.0) / 100.0;
            period.setText(dur + " seconds");
            double angularFValue = Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0;
            angularF = String.valueOf(angularFValue);
            angF.setText(angularFValue + " rad/s");
        }
    }

    public void equationCreation() {
        String equ = Math.round((border.getRadius()) * 100.0) / 100.0 + "sin" + "(" + angularF + "t" + " + π/2)";
        equation.setText(equ);
    }

}
