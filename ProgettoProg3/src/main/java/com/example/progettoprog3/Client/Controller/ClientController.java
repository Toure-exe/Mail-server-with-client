package com.example.progettoprog3.Client.Controller;
import com.example.progettoprog3.ClientApplication;
import com.example.progettoprog3.Model.EmailList;
import com.example.progettoprog3.Model.Email;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientController {

    @FXML
    private Label benvenutoLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button writeButton;

    @FXML
    private ListView listView;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String email;

    public boolean displayName(String email) {
        benvenutoLabel.setText("Hello: " + email);
        return setConnection(email);
    }

    public void passEmail(String email) {
        benvenutoLabel.setText("You are logged with: " + email);
    }

    public void onExitButton(ActionEvent event) {
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            System.out.println(clientIP);
            Socket s = new Socket(clientIP, 8189);
            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                out.writeObject("END CONNECTION-" + email);
                /*InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream();
                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                out.println("END CONNECTION-" + email);*/

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

    public void onWriteButton(ActionEvent event) throws IOException {
        /*root = FXMLLoader.load(ClientApplication.class.getResource("WriteEmail.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("WriteEmail.fxml"));
        root = loader.load();
        EmailController scene2Controller = loader.getController();
        scene2Controller.passSender(this.email);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(email);

                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                EmailList emailobj = (EmailList)in.readObject();
                ArrayList<Email> emailList = emailobj.getEmailList();
                System.out.println(emailList.size());
                for (Email tempEmail : emailList) {
                    //listView.getItems().add("From: " + tempEmail.getSender() + " Object: " + tempEmail.getObject() + " Date: " +tempEmail.getDate());
                    listView.getItems().add(tempEmail);
                }
                

            } finally {
                s.close();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error");
            return false;
        }
    }

    public void handleMouseClick(MouseEvent mouseEvent) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
        Email email = (Email)listView.getSelectionModel().getSelectedItem();
        System.out.println(email.getText());
    }

    /*public void handleMouseClick(MouseEvent arg0) {

        System.out.println("clicked on " + email.getObject());
    }*/
}
