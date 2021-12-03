package com.example.progettoprog3.Client.Controller;

import com.example.progettoprog3.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.*;
import java.io.*;
import java.util.*;

import com.example.progettoprog3.Model.Email;

import java.io.IOException;

public class EmailController {

    @FXML
    private Button backButton;
    @FXML
    private Button sendButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField object;
    @FXML
    private TextField subject;
    @FXML
    private TextArea text;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String obj;
    private String sbj;
    private String textEmail;
    private String date;
    private String email;

    public void passSender(String email) {
        this.email = email;
        System.out.println(this.email);
    }

    public void onBackButton(ActionEvent event) throws IOException {
        /*root = FXMLLoader.load(ClientApplication.class.getResource("InterfaceClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("InterfaceClient.fxml"));
        root = loader.load();
        ClientController scene2Controller = loader.getController();
        scene2Controller.passEmail(this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSendButton(ActionEvent event) {
        obj = object.getText();
        sbj = subject.getText();
        textEmail = text.getText();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        date = dtf.format(now);
        System.out.println(date);
        System.out.println(obj);
        System.out.println(sbj);
        System.out.println(textEmail);
        Email email = new Email(sbj, this.email, obj, textEmail, date);

        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            Socket s = new Socket(clientIP, 8189);
            try {
                /*InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream();
                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                out.println("NEW EMAIL");*/
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                //ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                out.writeObject(email);
            } finally {
                s.close();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}
