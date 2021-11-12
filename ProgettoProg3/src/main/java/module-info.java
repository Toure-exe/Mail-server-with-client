module com.example.progettoprog3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.progettoprog3 to javafx.fxml;
    exports com.example.progettoprog3;
    exports com.example.progettoprog3.Controller;
    opens com.example.progettoprog3.Controller to javafx.fxml;
}