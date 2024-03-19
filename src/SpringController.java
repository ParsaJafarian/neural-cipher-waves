/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import static java.lang.System.currentTimeMillis;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author 2278304
 */
public class SpringController implements Initializable {

    @FXML
    private AnchorPane back;
    @FXML
    private Slider freq;
    @FXML
    private Slider amp;
    @FXML
    private Rectangle block;
    @FXML
    private Line path;
    @FXML
    private Label equation;
    @FXML
    private Label period;
    @FXML
    private Label angVel;
    @FXML
    private Label constant;
    
    String angularV;
    boolean cont = true;
    ArrayList<Circle> dots = new ArrayList<>();
    Timeline alert = new Timeline(new KeyFrame(new Duration(0.1), event -> {
        dotCreation();
    }));
    double endSpring;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        path.setStartX(path.getStartX()+(block.getWidth()/2));
        endSpring= path.getEndX();
        PathTransition pathTransition = new PathTransition(new Duration(800), path, block);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.EASE_BOTH);
        //pathTransition.jumpTo(new Duration(currentTimeMillis()));
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        alert.play();
        
        setAngularVelocityAndPeriod(pathTransition);
        equationCreation();
        
        freq.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            pathTransition.setRate((double) newvalue);
            setAngularVelocityAndPeriod(pathTransition);
            equationCreation();
        });
        
        amp.setOnMousePressed(e -> {
            cont = false;
            amp.valueProperty().addListener((observable, oldvalue, newvalue) -> {

                pathTransition.stop();
                path.setEndX(path.getStartX() + (double) newvalue);
                block.setVisible(false);
                equationCreation();
            });
        });
        
        amp.setOnMouseReleased(e -> {
            cont = true;
            block.setVisible(true);
            pathTransition.play();
        });
    }    
    
    public void dotCreation() {
        Platform.runLater(() -> {
            if (cont) {
                Circle dot = new Circle(block.getTranslateX(), (block.getTranslateY()+200), 5, Color.WHITE);
                back.getChildren().add(dot);
                TranslateTransition translate = new TranslateTransition(new Duration(10000), dot);
                dots.add(dot);
                translate.setInterpolator(Interpolator.LINEAR);
                translate.setByY(2000);
                translate.play();
            }
            alert.play();

        });
    }
    public void setAngularVelocityAndPeriod(PathTransition pathTransition) {
        if (pathTransition.getRate() == 0) {
            period.setText("no movement");
            angVel.setText("no movement");
        } else {
            double dur = Math.round((pathTransition.getDuration().toSeconds()*2 / pathTransition.getRate()) * 100.0) / 100.0;
            period.setText(String.valueOf(dur) + " seconds");
            angularV = String.valueOf(Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0);
            double springConstant = Math.round(Math.pow(((2 * Math.PI)/dur)*1,2) * 100.0)/ 100.0;
            constant.setText(String.valueOf(springConstant) + " N/m");
            angVel.setText(String.valueOf(Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0) + " rad/s");
        }
    }

    public void equationCreation() {
        String equ = String.valueOf(Math.round((path.getEndX()-path.getStartX()) * 100.0) / 100.0) + "sin" + "(" + angularV + "t" + ")";
        equation.setText(equ);
    }
}
