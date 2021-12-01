module com.example.progettoprog3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.progettoprog3 to javafx.fxml;
    exports com.example.progettoprog3;
    exports com.example.progettoprog3.Client.Controller;
    opens com.example.progettoprog3.Client.Controller to javafx.fxml;

    exports com.example.progettoprog3.Server.Controller;
    opens com.example.progettoprog3.Server.Controller to javafx.fxml;
}