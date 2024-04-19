package com.cipher;

import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;


public class Controller {
    public double startI;
    public double startII;
    public double startO;
    public double startOO;

    public Circle innerC;
    public Circle outerC;
    @FXML
    private ImageView inner;

    @FXML
    private ImageView outer;

    @FXML
    public void onMousePressedI(MouseEvent e){
        double mouseDeltaX = e.getX();
        double mouseDeltaY = e.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        startI = Math.toDegrees(radAngle);
        startII = inner.getRotate();
    }
    @FXML
    public void onMousePressedO(MouseEvent e){
        double mouseDeltaX = e.getX();
        double mouseDeltaY = e.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        startO = Math.toDegrees(radAngle);
        startOO = outer.getRotate();
    }
    @FXML
    public void onDraggedI(MouseEvent t){
        double mouseDeltaX = t.getX();
        double mouseDeltaY = t.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        double deltaAngle = Math.toDegrees(radAngle)-startI;
        System.out.println(deltaAngle);
        inner.setRotate(startII+deltaAngle);
    }
    @FXML
    public void onDraggedO(MouseEvent t){
        double mouseDeltaX = t.getX();
        double mouseDeltaY = t.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        double deltaAngle = Math.toDegrees(radAngle)-startO;
        System.out.println(deltaAngle);
        outer.setRotate(startOO+deltaAngle);
    }
}