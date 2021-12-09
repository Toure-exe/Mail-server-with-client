package com.example.progettoprog3.Client.Controller;
import com.example.progettoprog3.Client.Application.ClientConnection;
import com.example.progettoprog3.ClientApplication;
import com.example.progettoprog3.Model.EmailList;
import com.example.progettoprog3.Model.Email;
import javafx.application.Platform;
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
import javafx.stage.StageStyle;

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
    private Button refreshButton;

    @FXML
    private ListView listView;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String email;
    private ArrayList<Email> emailList = null;
    private Thread t = null;
    private ClientConnection cc = null;

    public boolean displayName(String email) {
        benvenutoLabel.setText("Hello: " + email);
        return setConnection(email);
    }

    public void passEmail(String email) {
        this.email = email;
        System.out.println("passEmail method: " + email);
        benvenutoLabel.setText("You are logged with: " + email);
        //this.emailList = downloadEmailList();
        //viewEmailList();
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.ERROR); // set alert type
        a.initStyle(StageStyle.TRANSPARENT);
        this.cc = new ClientConnection(this.email, this.listView, this.emailList, a);
        t = new Thread(cc);
        t.start();
    }

    public void onRefreshButton(ActionEvent event) {
        listView.getItems().clear();
        downloadEmailList();
        viewEmailList();
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
                t.interrupt();
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
        t.interrupt();
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("WriteEmail.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        EmailController scene2Controller = loader.getController();
        scene2Controller.passSender(this.email);
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
                ArrayList<Email> emailListDownload = emailobj.getEmailList();
                this.emailList = reverseEmailList(emailListDownload);
                viewEmailList();
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.ERROR); // set alert type
                a.initStyle(StageStyle.UNDECORATED);
                this.cc = new ClientConnection(this.email, this.listView, this.emailList, a);
                //Platform.runLater(cc);
                t = new Thread(cc);
                t.start();

            } finally {
                s.close();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error");
            return false;
        }
    }

    public void handleMouseClick(MouseEvent mouseEvent) throws IOException {
        t.interrupt();
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
        int index = listView.getSelectionModel().getSelectedIndex();
        System.out.println(index);
        ArrayList<Email> emailListUpdate = this.cc.getEmailListUP();
        this.emailList = reverseEmailList(emailListUpdate);
        System.out.println(this.emailList.size());
        Email emailSingle = this.emailList.get(index);

        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("ViewEmail.fxml"));
        root = loader.load();
        EmailController scene2Controller = loader.getController();
        scene2Controller.passEmail(emailSingle, this.email);
        stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private ArrayList<Email> downloadEmailList() {
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            Socket s = new Socket(clientIP, 8189);
            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject("UPDATE-" + this.email);

                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                EmailList emailobj = (EmailList)in.readObject();
                this.emailList = emailobj.getEmailList();

            } finally {
                s.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error");
        } finally {
            return this.emailList;
        }
    }

    private void viewEmailList() {
        if (this.emailList != null && this.emailList.size() > 0) {
            for (Email tempEmail : this.emailList) {
                listView.getItems().add("From: " + tempEmail.getSender() + " Object: " + tempEmail.getObject() + " Date: " + tempEmail.getDate());
                //listView.getItems().add(tempEmail);
            }
        }
    }

    private ArrayList<Email> reverseEmailList(ArrayList<Email> emailList) {
        ArrayList<Email> revArrayList = new ArrayList<>();
        for (int i = emailList.size() - 1; i >= 0; i--) {
            revArrayList.add(emailList.get(i));
        }
        return revArrayList;
    }

}
