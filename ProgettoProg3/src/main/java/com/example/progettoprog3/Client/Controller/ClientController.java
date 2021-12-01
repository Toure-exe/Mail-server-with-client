package com.example.progettoprog3.Client.Controller;
import com.example.progettoprog3.ClientApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientController {
    private String email;

    @FXML
    private Label benvenutoLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public boolean displayName(String email) {
        benvenutoLabel.setText("Hello: " + email);
        return setConnection(email);
    }

    public void onExitButton(ActionEvent event) {
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            System.out.println(clientIP);
            Socket s = new Socket(clientIP, 8189);
            try {
                InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream();
                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                out.println("END CONNECTION-" + email);

            } finally {
                s.close();
                root = FXMLLoader.load(ClientApplication.class.getResource("Login.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }
        } catch (IOException e) {
            System.out.println("Error");
        }

    }

    private boolean setConnection(String email) {
        System.out.println(email);
        try {
            this.email = email;
            String clientIP = InetAddress.getLocalHost().getHostName();
            System.out.println(clientIP);
            Socket s = new Socket(clientIP, 8189);
            System.out.println("Ho aperto il socket verso il server.\n");
            try {
                InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream();
                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                out.println(email);
            } finally {
                s.close();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error");
            return false;
        }
    }
}
