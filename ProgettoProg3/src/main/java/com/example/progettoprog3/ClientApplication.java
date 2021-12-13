package com.example.progettoprog3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class ClientApplication extends Application {

    public static void main(String[] args) { launch(); }

    /**
     * Start the application client.
     * @param stage the stage
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Email Manager");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}