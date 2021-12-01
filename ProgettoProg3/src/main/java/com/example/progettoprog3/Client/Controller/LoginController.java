package com.example.progettoprog3.Client.Controller;

import com.example.progettoprog3.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.example.progettoprog3.Model.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField email;
    private UsersList list = new UsersList();
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void onLoginButtonPushed(ActionEvent event) throws IOException {
        boolean res = false;
        String currentEmail = email.getText();
        if(list.search(currentEmail)){
           // root = FXMLLoader.load(getClass().getResource("InterfaceClient.fxml"));

            String emailLoader = email.getText();

            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("InterfaceClient.fxml"));
            root = loader.load();

            ClientController scene2Controller = loader.getController();
            if(scene2Controller.displayName(emailLoader)) {
                //FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("InterfaceClient.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setResizable(true);
                stage.setMinHeight(450.0);
                stage.setMinWidth(600.0);
            } else {
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.ERROR); // set alert type
                a.setContentText("Server temporary down");
                a.show();// show the dialog
            }



            System.out.println("login");
        }else{
            System.out.println("login non riuscito");
        }
    }
}
