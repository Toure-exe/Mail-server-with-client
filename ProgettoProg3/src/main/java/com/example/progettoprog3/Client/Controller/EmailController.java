package com.example.progettoprog3.Client.Controller;

import com.example.progettoprog3.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Button goBackButton;
    @FXML
    private Button sendButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button replyButton;
    @FXML
    private Button replyAllButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button forwardAllButton;
    @FXML
    private TextField object;
    @FXML
    private TextField subject;
    @FXML
    private TextArea text;
    @FXML
    private TextArea textAreaEmail;
    @FXML
    private Label objectLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label senderLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String obj;
    private String sbj;
    private String textEmail;
    private String date;
    private String email;
    private Email emailObj;

    public void passSender(String email) {
        this.email = email;
        System.out.println(this.email);
    }

    public void replyEmail(Email emailObj, String email) {
        this.email = email;
        subject.setText(emailObj.getSender());
        object.setText("Reply: " + emailObj.getObject());
    }

    public void replyAllEmail(Email emailObj, String email) {
        this.email = email;
        object.setText("Reply: " + emailObj.getObject());
        ArrayList<String> receiverList = emailObj.getReceiver();
        String res = "";
        int i = 0;
        for (String receiver : receiverList) {
            if (!receiver.equals(this.email))
                res += receiver + " ";
        }
        subject.setText(emailObj.getSender() + " " + res);
    }

    public void forwardEmail(Email emailObj, String email) {
        this.email = email;
        subject.setText(emailObj.getSender());
        object.setText(emailObj.getObject());
        text.setText("\nForwarded message: \n" + emailObj.getText());
    }

    public void forwardAllEmail(Email emailObj, String email) {
        this.email = email;
        object.setText(emailObj.getObject());
        text.setText("\nForwarded message: \n" + emailObj.getText());
        ArrayList<String> receiverList = emailObj.getReceiver();
        String res = "";
        int i = 0;
        for (String receiver : receiverList) {
            if (!receiver.equals(this.email))
                res += receiver + " ";
        }
        subject.setText(emailObj.getSender() + " " + res);
    }

    public void passEmail(Email emailSingle, String email) {
        textAreaEmail.setEditable(false);
        this.email = email;
        this.emailObj = emailSingle;
        System.out.println("E TU CI ENTRIIIIII");
        objectLabel.setText("Object: " + this.emailObj.getObject());
        dateLabel.setText("Date: " + this.emailObj.getDate());
        senderLabel.setText("Sender: " + this.emailObj.getSender());
        textAreaEmail.appendText(this.emailObj.getText());
        if(emailSingle.getReceiver().size() == 1) {
            replyAllButton.setVisible(false);
            forwardAllButton.setVisible(false);
        }
    }

    public void onReplyButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("WriteEmail.fxml"));
        root = loader.load();
        EmailController scene2Controller = loader.getController();
        scene2Controller.replyEmail(this.emailObj, this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onReplyAllButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("WriteEmail.fxml"));
        root = loader.load();
        EmailController scene2Controller = loader.getController();
        scene2Controller.replyAllEmail(this.emailObj, this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onForwardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("WriteEmail.fxml"));
        root = loader.load();
        EmailController scene2Controller = loader.getController();
        scene2Controller.forwardEmail(this.emailObj, this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onForwardAllButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("WriteEmail.fxml"));
        root = loader.load();
        EmailController scene2Controller = loader.getController();
        scene2Controller.forwardAllEmail(this.emailObj, this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteButton(ActionEvent event) throws IOException {
        this.emailObj.setDelete(true);
        ArrayList<String> receiver = new ArrayList<>();
        receiver.add(this.email);
        this.emailObj.setReceiver(receiver);
        if (sendEmail(this.emailObj)) {
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("InterfaceClient.fxml"));
            root = loader.load();
            ClientController scene2Controller = loader.getController();
            System.out.println("onBackButton method: " + this.email);
            scene2Controller.passEmail(this.email);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onBackButton(ActionEvent event) throws IOException {
        /*root = FXMLLoader.load(ClientApplication.class.getResource("InterfaceClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("InterfaceClient.fxml"));
        root = loader.load();
        //ClientController scene2Controller = loader.getController();
        ClientController scene2Controller = loader.getController();
        System.out.println("onBackButton method: " + this.email);
        scene2Controller.passEmail(this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSendButton(ActionEvent event) throws IOException {
        obj = object.getText();
        textEmail = text.getText();
        sbj = subject.getText();
        String[] temp = sbj.split(" ");
        boolean res = true;
        if (temp.length == 0){
            res = false;
            System.out.println("ciao");
        }
        if (temp[0].isBlank())
            System.out.println("ASDRUBALE");
        System.out.println(temp[0]);
        for (int i = 0; i < temp.length && res; i++) {
            if (!temp[i].contains("@") || (!temp[i].contains(".it") && !temp[i].contains(".com"))) {
                if (!temp[i].isBlank())
                    res = false;
            }
        }
        if (res) {
            //email insert correctly
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            date = dtf.format(now);
            System.out.println(date);
            System.out.println(obj);
            System.out.println(sbj);
            System.out.println(textEmail);
            Email email = new Email(sbj, this.email, obj, textEmail, date);
            if (sendEmail(email)) {
                FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("InterfaceClient.fxml"));
                root = loader.load();
                ClientController scene2Controller = loader.getController();
                System.out.println("onBackButton method: " + this.email);
                scene2Controller.passEmail(this.email);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

        } else {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR); // set alert type
            a.setContentText("Wrong email, please write again");
            Optional<ButtonType> result = a.showAndWait();
            if(!result.isPresent() || result.get() == ButtonType.OK) {
                // alert is exited, no button has been pressed.
                // or okay button is pressed
                subject.setText("");
            }
        }
    }

    private boolean sendEmail(Email email) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.ERROR);
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            Socket s = new Socket(clientIP, 8189);
            try {
                //send the email to the server
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(email);
                //receive the response
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                String response = (String)in.readObject();
                if (response.equals("ERROR")) {
                     // set alert type
                    a.setContentText("Email error: user not exists");
                    a.show();
                }
            } catch (ClassNotFoundException e1) {
                System.out.println("Error");
            } finally {
                s.close();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error");
            a.setContentText("Server temporary down, please try again");
            a.show();
            return false;
        }
    }

}
