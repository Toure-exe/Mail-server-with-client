package com.example.progettoprog3.Controller;

import com.example.progettoprog3.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        String currentEmail = email.getText();
        if(list.search(currentEmail)){
           // root = FXMLLoader.load(getClass().getResource("InterfaceClient.fxml"));

            FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("InterfaceClient.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root.load());
            stage.setScene(scene);
            stage.show();
            stage.setResizable(true);
            stage.setMinHeight(450.0);
            stage.setMinWidth(600.0);

            System.out.println("login");
        }else{
            System.out.println("login non riuscito");
        }
    }
}
