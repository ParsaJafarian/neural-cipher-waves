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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ropeSet(object);
        System.out.println(path.toString());
        PathTransition pathTransition = new PathTransition(new Duration(800), path, object);
        pathTransition.setAutoReverse(true);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.play();

        Shape1.setOnAction(e -> {
            System.out.println(pathTransition.durationProperty());
            pathTransition.getNode().setVisible(false);
            ropeSet(object2);
            pathTransition.setNode(object2);
            pathTransition.stop();
            pathTransition.play();
        });
        
        
    /*
        rope2.startXProperty().bind(object.translateXProperty().add(object.centerXProperty()));
        rope2.startYProperty().bind(object.translateYProperty().add(object.centerYProperty()));
        rope2.endXProperty().bind(object2.translateXProperty().add(object.getCenterX()));
        rope2.endYProperty().bind(object2.translateYProperty().add(object.getCenterY()));
        PathTransition pathTransition2 = new PathTransition(new Duration(800), path2, object2);
        pathTransition2.setAutoReverse(true);
        pathTransition2.setCycleCount(Animation.INDEFINITE);
        pathTransition2.play();
     */
}
    
    public void ropeSet(Circle obj) {
        rope.startXProperty().bind(pivot.centerXProperty());
        rope.startYProperty().bind(pivot.centerYProperty());
        rope.endXProperty().bind(obj.translateXProperty().add(obj.getCenterX()));
        rope.endYProperty().bind(obj.translateYProperty().add(obj.getCenterY()));
    }

}
