package com.example.progettoprog3;

import com.example.progettoprog3.Server.Controller.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class ServerApplication extends Application {

    public static void main(String[] args) { launch(); }

    /**
     * Start the application server.
     * @param stage the stage
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ServerApplication.class.getResource("InterfaceServer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(ServerApplication.class.getResource("layout.css").toExternalForm());
        stage.setTitle("Server Log");
        stage.setResizable(false);
        stage.show();
        ServerController sc = loader.getController();
        sc.initServerController();
    }
}