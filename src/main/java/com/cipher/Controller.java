package com.cipher;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;


public class Controller {

    private double IstartAngle;
    private double OstartAngle;
    @FXML
    private ImageView inner;

    @FXML
    private ImageView outer;

    @FXML
    public void onMousePressedI(MouseEvent e){
        IstartAngle = aTanAngle(e.getX(), e.getY());
    }
    @FXML
    public void onMousePressedO(MouseEvent e){
        OstartAngle = aTanAngle(e.getX(), e.getY());
    }
    @FXML
    public void onDraggedI(MouseDragEvent t){
        double deltaAngle = aTanAngle(t.getX(), t.getY()) - IstartAngle;
        Rotate rotate = new Rotate(deltaAngle, Rotate.Z_AXIS);
        inner.getTransforms().add(rotate);
    }
    @FXML
    public void onDraggedO(MouseDragEvent t){
        double deltaAngle = aTanAngle(t.getX(), t.getY()) - OstartAngle;
        Rotate rotate = new Rotate(deltaAngle, Rotate.Z_AXIS);
        outer.getTransforms().add(rotate);
    }
    private double aTanAngle(double x, double y){
        return Math.toDegrees(Math.atan(y/x));
    }
}