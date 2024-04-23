module com.wavesneuralnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;
    requires java.desktop;

    opens com to javafx.fxml;
    exports com;
    exports com.nn.display;
    opens com.nn.display to javafx.fxml;
    exports com.nn.algo;
    opens com.nn.algo to javafx.fxml;
    exports com.nn.utils;
    opens com.nn.utils to javafx.fxml;
    exports com.nn;
    opens com.nn to javafx.fxml;
}