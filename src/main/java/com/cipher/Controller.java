package com.cipher;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import static com.SceneSwitcher.switchToScene;

/**
 * The controller is where the back-end and the front-end of the program are connected
 */

public class Controller {
    public File inputFile;
    public Button savB;
    public Button selB;
    public CheckBox Ensy;
    public Button reset;
    public double startI;
    public double startII;
    public double startO;
    public double startOO;
    public TextArea Input;
    public TextArea Preview;

    public RadioButton decrypt;
    public Circle innerC;
    public Circle outerC;
    @FXML
    private ImageView inner;

    @FXML
    private Button back;

    @FXML
    private ImageView outer;

    /**
     * function to grab mouse coordinates when clicked on the inner circle
     * @param e the mouseAction event
     */
    @FXML
    public void onMousePressedI(MouseEvent e){
        double mouseDeltaX = e.getX();
        double mouseDeltaY = e.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        startI = Math.toDegrees(radAngle);
        startII = inner.getRotate();
    }
    /**
     * function to grab mouse coordinates when clicked on the outer circle
     * @param e the mouseAction event
     */
    @FXML
    public void onMousePressedO(MouseEvent e){
        double mouseDeltaX = e.getX();
        double mouseDeltaY = e.getY();
        double radAngle = Math.atan2(mouseDeltaY, mouseDeltaX);
        startO = Math.toDegrees(radAngle);
        startOO = outer.getRotate();
    }

    /**
     * Function to continuously track the mouse movement and rotates the inner circle accordingly
     * @param t MouseDrag event
     */
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
        Preview.setText(CC_Engine.CC_encrypt(Input.getText(),key,Ensy.isSelected(),decrypt.isSelected()));
    }
    /**
     * Function to continuously track the mouse movement and rotates the outer circle accordingly
     * @param t MouseDrag event
     */
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
        Preview.setText(CC_Engine.CC_encrypt(Input.getText(),key,Ensy.isSelected(),decrypt.isSelected()));
    }

    /**
     * File explorer launch to save decrypted/encrypted into text files
     * @throws FileNotFoundException error handling for non-existing directories
     */
    public void onsavB() throws FileNotFoundException {
        Stage pupop = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data/cipher"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(pupop);
        if (file != null) {
            saveTextToFile(Preview.getText(), file);
        }
    }

    /**
     * this function writes the content of the program's output textfield into a .txt file
     * @param content the content of the programs textfield
     * @param file    the file to be saved
     * @throws FileNotFoundException non-existing directories error handling
     */
    private void saveTextToFile(String content, File file) throws FileNotFoundException {
        PrintWriter writer;
        writer = new PrintWriter(file);
        writer.println(content);
        writer.close();
    }

    /**
     * Function to select text files as input parameters
     * @throws FileNotFoundException error handling of non-existing files
     */
    public void onselB() throws FileNotFoundException {
        Stage pupop = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialDirectory(new File("data/cipher"));
        File selectedFile = fileChooser.showOpenDialog(pupop);
        inputFile = selectedFile;
        Input.clear();
        Scanner scanner = new Scanner(selectedFile);
        while (scanner.hasNext()){
            Input.setText(Input.getText()+scanner.next());
        }
    }

    /**
     * back button to go back to the main menu
     */
    public void onBack(){
        switchToScene(back,"main-menu.fxml");
    }

    /**
     * reset button function to reset the cipher wheel
     */
    @FXML
    public void resetClicked(){
        inner.setRotate(0);
        outer.setRotate(0);
        startI=0;
        startII=0;
        startO=0;
        startOO=0;
        Preview.setText(CC_Engine.CC_encrypt(Input.getText(),0,Ensy.isSelected(),decrypt.isSelected()));
    }
}