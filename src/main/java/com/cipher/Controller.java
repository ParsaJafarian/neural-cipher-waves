package com.cipher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;


public class Controller {
    public CheckBox Ensy;
    public final String dict = "abcdefghijklmnopqrstuvwxyz";
    public Button reset;
    public double startI;
    public double startII;
    public double startO;
    public double startOO;
    public TextArea Input;
    public TextArea Preview;

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
        inner.setRotate(startII+deltaAngle);
        double innerang;
        if (inner.getRotate()<0){
            innerang = 360 + inner.getRotate();
        } else{
            innerang = inner.getRotate();
        }
        double outerang;
        if (outer.getRotate()<0){
            outerang = 360 + outer.getRotate();
        } else{
            outerang = outer.getRotate();
        }
        int fromInd = (int) Math.round(innerang/13.84615384615385);
        int toInd = (int) Math.round((outerang)/13.84615384615385);
        int key;
        if ((fromInd-toInd)<0){
            key = 26 + (fromInd-toInd);
        }else
        {
            key = fromInd-toInd;
        }
        Preview.setText(CC_Engine.CC_encrypt(Input.getText(),key,Ensy.isSelected()));
    }
    @FXML
    public void onDraggedO(MouseEvent t){
        double mouseDeltaX = t.getX();
        double mouseDeltaY = t.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        double deltaAngle = Math.toDegrees(radAngle)-startO;
        outer.setRotate(startOO+deltaAngle);
        double innerang;
        if (inner.getRotate()<0){
            innerang = 360 + inner.getRotate();
        } else{
            innerang = inner.getRotate();
        }
        double outerang;
        if (outer.getRotate()<0){
            outerang = 360 + outer.getRotate();
        } else{
            outerang = outer.getRotate();
        }
        int fromInd = (int) Math.round(innerang/13.84615384615385);
        int toInd = (int) Math.round((outerang)/13.84615384615385);
        int key;
        if ((fromInd-toInd)<0){
            key = 26 + (fromInd-toInd);
        }else
        {
            key = fromInd-toInd;
        }
        Preview.setText(CC_Engine.CC_encrypt(Input.getText(),key,Ensy.isSelected()));
    }
    public void onKeySet(MouseEvent r){
        //int fromInd = (int) Math.ceil((inner.getRotate()-13.84615384615385/2)/13.84615384615385);
        //int key = fromInd;
        //Preview.setText(CC_Engine.CC_encrypt(Input.getText(),key));
    }
    @FXML
    public void resetClicked(){
        inner.setRotate(0);
        outer.setRotate(0);
        startI=0;
        startII=0;
        startO=0;
        startOO=0;
        Preview.setText(CC_Engine.CC_encrypt(Input.getText(),0,Ensy.isSelected()));
    }
}