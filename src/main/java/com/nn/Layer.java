//package com.nn;
//
//import javafx.scene.control.Button;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.shape.Circle;
//
//import java.util.ArrayList;
//
//public class Layer {
//    private final HBox btnContainer;
//    private final ArrayList<Circle> neurons;
//
//    public Layer() {
//        this.btnContainer = initializeBtnContainer();
//        this.neurons = new ArrayList<>();
//    }
//
//    private ArrayList<Circle> initializeNeurons(int numberOfNeurons) {
//        ArrayList<Circle> neurons = new ArrayList<>();
//        for (int i = 0; i < numberOfNeurons; i++) {
//            Circle neuron = new Circle(10);
//            neuron.getStyleClass().add("neuron");
//            neurons.add(neuron);
//        }
//        return neurons;
//    }
//
//    private void setTranslateX(double x) {
//        btnContainer.setTranslateX(x);
//        for (Circle neuron : neurons) {
//            neuron.setTranslateX(x);
//        }
//    }
//}
