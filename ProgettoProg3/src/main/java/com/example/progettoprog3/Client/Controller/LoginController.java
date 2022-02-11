package com.example.progettoprog3.Client.Controller;

import com.example.progettoprog3.ClientApplication;
import com.example.progettoprog3.Model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class LoginController {

    @FXML
    private TextField email;

    private final UsersList list = new UsersList();

    /**
     * Check that the email entered is in the e-mail list and if it is present we change the scene in interfaceClient.xfml,
     * otherwise an alert is sent to warn the user that the email is wrong.
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onLoginButtonPushed(ActionEvent event) throws IOException {
        Stage stage; Scene scene; Parent root;
        Alert a = new Alert(Alert.AlertType.ERROR);
        String currentEmail = email.getText();
        if (list.search(currentEmail)) {
            String emailLoader = email.getText();
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("InterfaceClient.fxml"));
            root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(true);
            stage.setMinHeight(450.0);
            stage.setMinWidth(600.0);
            System.out.println("-- Login successfully --");
            ClientController scene2Controller = loader.getController();
            scene2Controller.initClientController(emailLoader, true);
        } else {
            System.out.println("-- Login failed --");
            a.setContentText("Error, wrong login");
            a.show(); //show the dialog
        }
    }
}