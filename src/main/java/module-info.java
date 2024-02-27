module com.wavesneuralnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    // parsa M
    requires commons.math3;

    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;

    opens com to javafx.fxml;
    exports com;
    exports com.nn;
    opens com.nn to javafx.fxml;
}