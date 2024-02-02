module com.wavesneuralnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.wavesneuralnetwork to javafx.fxml;
    exports com.wavesneuralnetwork;
}