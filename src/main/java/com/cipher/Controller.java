package com.cipher;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;


public class Controller {

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
        innerC.setRotate(Math.toDegrees(radAngle));
    }
    @FXML
    public void onMousePressedO(MouseEvent e){
        System.out.println(e.getX());
    }
    @FXML
    public void onDraggedI(MouseEvent t){
        double mouseDeltaX = t.getX();
        double mouseDeltaY = t.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        System.out.println(radAngle);
        System.out.println(innerC.getRotate());
        double deltaAngle = Math.toDegrees(radAngle)-innerC.getRotate();
        //inner.setRotate(deltaAngle);
        //System.out.println(deltaAngle);
    }
    @FXML
    public void onDraggedO(MouseEvent t){
        System.out.println("Mouse Dragged on the Outer");
    }
}