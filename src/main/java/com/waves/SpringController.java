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
    @FXML
    private LineChart<?, ?> kineticGraph;
    @FXML
    private Label angF;
    @FXML
    private Button exit;

    String angularF;
    boolean cont = true;
    ArrayList<Circle> dots = new ArrayList<>();
    double endSpring;
    double totalE;
    double amplitude;
    double springConstant;
    double dur;
    LineChart<Number, Number> r;
    XYChart.Series<Number, Number> series;
    XYChart.Series<Number, Number> series2;
    ArrayList<Double> time;
    int t = 0;
    PathTransition pathTransition;
    @FXML
    Line springPartA;
    @FXML
    Line springPartB;
    @FXML
    Line springPartC;
    @FXML
    Line springPartD;
    @FXML
    Line springPartE;
    @FXML
    Line springPartF;
    @FXML
    Line springPartG;
    @FXML
    Rectangle stand;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                dotCreation();
                addEnergy(time, series, series2);
            }
        };
        timer.start();

        block.translateXProperty().addListener((observableValue, oldValue, newValue) -> {
            //number of spring segments
            int springSegments =3;
            //this gives me the space that the spring has to compress or stretch
            double displacement = (Double.parseDouble(newValue.toString())-160+62.8);
            //5 segments on my spring
            double segmentspace = displacement/springSegments;
            double positionXOfEndPoints = segmentspace/2;
            //find the back of the spring system, that does not move
            double backCoordinate = stand.getLayoutX()+stand.getWidth();
            springPartA.setStartX(backCoordinate);
            springPartA.setEndX(springPartA.getStartX()+positionXOfEndPoints);
            springPartB.setStartX(springPartA.getEndX());
            springPartB.setEndX(springPartA.getEndX()+positionXOfEndPoints);
            springPartC.setStartX(springPartB.getEndX());
            springPartC.setEndX(springPartB.getEndX()+positionXOfEndPoints);
            springPartD.setStartX(springPartC.getEndX());
            springPartD.setEndX(springPartC.getEndX()+positionXOfEndPoints);
            springPartE.setStartX(springPartD.getEndX());
            springPartE.setEndX(springPartD.getEndX()+positionXOfEndPoints);
            springPartF.setStartX(springPartE.getEndX());
            springPartF.setEndX(springPartE.getEndX()+positionXOfEndPoints);
            springPartG.setStartX(springPartF.getEndX());
            springPartG.setEndX(Double.parseDouble(newValue.toString()));
        });
        path.setStartX(path.getStartX() + (block.getWidth() / 2));
        endSpring = path.getEndX();
        pathTransition = new PathTransition(new Duration(800), path, block);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.EASE_BOTH);
        pathTransition.setAutoReverse(true);
        pathTransition.play();


        setAngularVelocityAndPeriod(pathTransition);
        equationCreation();

        freq.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            cont = false;
            pathTransition.setRate((double) newvalue);
            setAngularVelocityAndPeriod(pathTransition);

            freq.setOnMouseReleased(e -> {
                cont = true;
                equationCreation();
            });
        });

        amp.setOnMousePressed(e -> {
            cont = false;
            amp.valueProperty().addListener((observable, oldvalue, newvalue) -> {
                pathTransition.stop();
                path.setEndX(path.getStartX() + (double) newvalue);
            });
        });

        amp.setOnMouseReleased(e -> {
            cont = true;
            pathTransition.play();
            equationCreation();
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
                Circle dot = new Circle(block.getTranslateX(), (block.getTranslateY() + 200), 5, Color.BURLYWOOD);
                back.getChildren().add(dot);
                TranslateTransition translate = new TranslateTransition(new Duration(10000), dot);
                dots.add(dot);
                translate.setInterpolator(Interpolator.LINEAR);
                translate.setByY(2000);
                translate.play();
                translate.play();
                translate.setOnFinished(e -> {
                    back.getChildren().remove(translate.getNode());
                    dots.remove((Circle) translate.getNode());
                });
            } else {
                back.getChildren().removeAll(dots);
            }

        });
    }

    public void setAngularVelocityAndPeriod(PathTransition pathTransition) {
        if (pathTransition.getRate() == 0) {
            period.setText("no movement");
            angF.setText("no movement");
        } else {
            dur = Math.round((pathTransition.getDuration().toSeconds() * 2 / pathTransition.getRate()) * 100.0) / 100.0;
            period.setText(dur + " seconds");
            angularF = String.valueOf(Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0);
            springConstant = Math.round(Math.pow(((2 * Math.PI) / dur) * 1, 2) * 100.0) / 100.0;
            constant.setText(springConstant + " N/m");
            angF.setText(Math.round(((2 * Math.PI) / dur) * 100.0) / 100.0 + " rad/s");
        }
    }

    public void equationCreation() {
        pathTransition.stop();
        pathTransition.play();
        amplitude = Math.round((path.getEndX() - path.getStartX()) / 100 * 100.0) / 100.0;
        String equ = amplitude + "sin" + "(" + angularF + "t" + " + π/2)";
        equation.setText(equ);
        totalE = 0.5 * springConstant * Math.pow(amplitude, 2);
        graphCreation();
    }

    public void graphCreation() {
        t = 0;
        back.getChildren().remove(r);

        NumberAxis x = new NumberAxis(0, dur, 1);
        x.setLabel("Time (period)");
        NumberAxis y = new NumberAxis(0, totalE, 1);
        y.setLabel("energy");

        r = new LineChart<>(x, y);
        r.setMaxHeight(350);
        r.setLayoutX(kineticGraph.getLayoutX());
        r.setLayoutY(kineticGraph.getLayoutY());
        //creating arrayList for time values for creating energy graphs
        time = new ArrayList<>();
        for (double i = 0; i < dur; i += 0.017) {
            time.add(i);
        }

        //Potential energy values according to the time values created above
        series = new XYChart.Series<>();
        series.setName("Potential Energy");

        //Total energy value: TotalE
        //Kinetic energy values according to the time values created above
        series2 = new XYChart.Series<>();
        series2.setName("Kinetic Energy");

        r.getData().add(series);
        r.getData().add(series2);
        r.setCreateSymbols(false);
        back.getChildren().add(r);
    }

    public void addEnergy(ArrayList<Double> time, XYChart.Series<Number, Number> series, XYChart.Series<Number, Number> series2) {
        if (t < time.size()) {
            double displacement = amplitude * Math.sin(((2 * Math.PI) / dur) * time.get(t));
            double val = 0.5 * springConstant * Math.pow(displacement, 2);
            series.getData().add(new XYChart.Data<>(time.get(t), val));
            double val2 = totalE - val;
            series2.getData().add(new XYChart.Data<>(time.get(t), val2));
            t++;
        }

    }
}
