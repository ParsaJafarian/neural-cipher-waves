module com.wavesneuralnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com to javafx.fxml;
    exports com;
    exports com.nn;
    opens com.nn to javafx.fxml;
}