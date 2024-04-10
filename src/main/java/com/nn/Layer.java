//package com.nn;
//
//import javafx.scene.control.Button;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.shape.Circle;
//
//import java.util.ArrayList;
//
//import static com.nn.NeuralNetworkConfig.getLayerSpacing;
//
//public class Layer {
//    private final HBox btnContainer;
//    private final ArrayList<Neuron> neurons;
//
//    public Layer() {
//        this.btnContainer = initializeBtnContainer();
//        this.neurons = new ArrayList<>();
//    }
//
//    public Layer(boolean withBtnContainer){
//        this.btnContainer = withBtnContainer ? initializeBtnContainer() : null;
//        this.neurons = new ArrayList<>();
//    }
//
//    private void addLayerButtons() {
//        int lastLayerIndex = network.getNumLayers() - 1;
//
//        HBox btnContainer = new HBox();
//        btnContainer.setSpacing(5);
//        btnContainer.toFront();
//        btnContainer.setTranslateX(getLayerSpacing(lastLayerIndex));
//
//        Button addNeuronBtn = new Button("+");
//        Button removeNeuronBtn = new Button("-");
//
//        addNeuronBtn.setOnAction(e -> addNeuronThroughBtn(lastLayerIndex));
//        removeNeuronBtn.setOnAction(e -> removeNeuron(lastLayerIndex));
//
//        btnContainer.getChildren().add(addNeuronBtn);
//        btnContainer.getChildren().add(removeNeuronBtn);
//
//        networkContainer.getChildren().add(btnContainer);
//        btnContainers.add(btnContainer);
//    }
//
//
//}
