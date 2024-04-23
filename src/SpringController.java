/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import static java.lang.System.currentTimeMillis;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
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
    private Label constant;

    String angularF;
    boolean cont = true;
    ArrayList<Circle> dots = new ArrayList<>();
    double endSpring;
    double totalE;
    double amplitude;
    double springConstant;
    double dur;
    @FXML
    private LineChart<?, ?> kineticGraph;
    @FXML
    private Label angF;
    @FXML
    private Button exit;
    LineChart r;
    XYChart.Series series;
    XYChart.Series series2;
    ArrayList time;
    int t =0;
    Timeline alert = new Timeline(new KeyFrame(new Duration(0.1), event -> {
        dotCreation();
        addEnergy(time, series, series2);
    }));
    PathTransition pathTransition;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        path.setStartX(path.getStartX() + (block.getWidth() / 2));
        endSpring = path.getEndX();
        System.out.println(path.getStartX());
        pathTransition = new PathTransition(new Duration(800), path, block);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.EASE_BOTH);
        //pathTransition.jumpTo(new Duration(currentTimeMillis()));
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        

        setAngularVelocityAndPeriod(pathTransition);
        equationCreation();
        alert.play();

        freq.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            pathTransition.setRate((double) newvalue);
            setAngularVelocityAndPeriod(pathTransition);            freq.setOnMouseReleased(e -> {
                equationCreation();
            });
        });

        amp.setOnMousePressed(e -> {
            cont = false;
            amp.valueProperty().addListener((observable, oldvalue, newvalue) -> {
                pathTransition.stop();
                path.setEndX(path.getStartX() + (double) newvalue);
                block.setVisible(false);
            });
        });

        amp.setOnMouseReleased(e -> {
            cont = true;
            block.setVisible(true);
            pathTransition.play();
            equationCreation();
        });
        
        exit.setOnAction(e->{
        try {
                //changes the root of the scene to direct the user to the slideshow before the race starts
                exit.getScene().setRoot(FXMLLoader.load(getClass().getResource("Menu.fxml")));
            } catch (IOException ex) {
            }
        });
    }

    public void dotCreation() {
        Platform.runLater(() -> {
            if (cont) {
                Circle dot = new Circle(block.getTranslateX(), (block.getTranslateY() + 200), 5, Color.WHITE);
                back.getChildren().add(dot);
                TranslateTransition translate = new TranslateTransition(new Duration(10000), dot);
                dots.add(dot);
                translate.setInterpolator(Interpolator.LINEAR);
                translate.setByY(2000);
                translate.play();
                translate.play();
                translate.setOnFinished(e -> {
                    back.getChildren().remove(translate.getNode());
                });
            }
            alert.play();

        });
    }

    public void setAngularVelocityAndPeriod(PathTransition pathTransition) {
        if (pathTransition.getRate() == 0) {
            period.setText("no movement");
            angF.setText("no movement");
        } else {
            dur = Math.round((pathTransition.getDuration().toSeconds() * 2 / pathTransition.getRate()) * 100.0) / 100.0;
            period.setText(String.valueOf(dur) + " seconds");
            angularF = String.valueOf(Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0);
            springConstant = Math.round(Math.pow(((2 * Math.PI) / dur) * 1, 2) * 100.0) / 100.0;
            constant.setText(String.valueOf(springConstant) + " N/m");
            angF.setText(String.valueOf(Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0) + " rad/s");
        }
    }

    public void equationCreation() {
        pathTransition.stop();
        pathTransition.play();
        amplitude = Math.round((path.getEndX() - path.getStartX()) / 100 * 100.0) / 100.0;
        String equ = String.valueOf(amplitude) + "sin" + "(" + angularF + "t" + " + π/2)";
        equation.setText(equ);
        totalE = 0.5 * springConstant * Math.pow(amplitude, 2);
        graphCreation();
    }
    

    public void graphCreation() {
        t=0;
        back.getChildren().remove(r);
        //ArrayList potential = new ArrayList<>();
        //ArrayList kinetic = new ArrayList<>();
        time = new ArrayList<>();
        NumberAxis x = new NumberAxis(0, dur, 1);
        x.setLabel("Time (period)");
        NumberAxis y = new NumberAxis(0, totalE, 1);
        y.setLabel("energy");

        r = new LineChart(x, y);
        r.setMaxHeight(350);
        r.setLayoutX(kineticGraph.getLayoutX());
        r.setLayoutY(kineticGraph.getLayoutY());
        //creating arrayList for time values for creating energy graphs
        for (double i = 0; i < dur; i += 0.01) {
            time.add(i);
        }

        //Potential enrrgy values according to the time values created above
        series = new XYChart.Series<>();
        series.setName("Potential Energy");
//        for (int i = 0; i < time.size(); i++) {
//            double displacement = amplitude * Math.sin(((2 * Math.PI) / dur) * (double) time.get(i));
//            double val = 0.5 * springConstant * Math.pow(displacement, 2);
//            potential.add(val);
//            series.getData().add(new XYChart.Data(time.get(i), potential.get(i)));
//        }
        
        //Total energy value: TotalE
        //Kinetic enrrgy values according to the time values created above
        series2 = new XYChart.Series<>();
        series2.setName("Kinetic Energy");
//        for (int i = 0; i < time.size(); i++) {
//            double val = totalE - (double) potential.get(i);
//            kinetic.add(val);
//            series2.getData().add(new XYChart.Data(time.get(i), kinetic.get(i)));
//        }
        

        r.getData().addAll(series, series2);
        r.setCreateSymbols(false);
        back.getChildren().add(r);
    }
    
    public void findTime(){
        System.out.println(path.getStartX());
    }
    
    
    public void addEnergy(ArrayList time, XYChart.Series series, XYChart.Series series2){
        if (t<time.size()) {
            double displacement = amplitude * Math.sin(((2 * Math.PI) / dur) * (double) time.get(t));
            double val = 0.5 * springConstant * Math.pow(displacement, 2);
            series.getData().add(new XYChart.Data(time.get(t), val));
            double val2 = totalE - (double) val;
            series2.getData().add(new XYChart.Data(time.get(t), val2));
            t++;
        }
    }
}
