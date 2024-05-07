module com.wavesneuralnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    // parsa M
    requires commons.math3;

    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires java.logging;

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
    exports com.cipher;
    opens com.cipher to javafx.fxml;
    exports com.waves;
    opens com.waves to javafx.fxml;
}