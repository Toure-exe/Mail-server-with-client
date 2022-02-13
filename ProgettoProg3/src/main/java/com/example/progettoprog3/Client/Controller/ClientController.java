package com.example.progettoprog3.Client.Controller;

import com.example.progettoprog3.Client.Application.ClientConnection;
import com.example.progettoprog3.ClientApplication;
import com.example.progettoprog3.Model.EmailList;
import com.example.progettoprog3.Model.Email;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class ClientController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<String> listView;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String email;
    private ArrayList<Email> emailList = null;
    private Thread t = null;
    private ClientConnection cc = null;
    private DialogPane dialog;

    /**
     * Initialization function of this class, every other controller must go through this function.
     * It will set the main information such as the email string and print the string in the label.
     * In this function we set a handler for closing button X.
     * @param email the e-mail string of the client
     * @param firstTime to know if it is the first time or not
     */
    public void initClientController(String email, boolean firstTime) {
        this.email = email;
        stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            event.consume();
            exitApplication(stage);
        });
        if (firstTime)
            welcomeLabel.setText("Hello: " + this.email);
        else
            welcomeLabel.setText("You are logged with: " + this.email);
        setConnection(email, firstTime);
    }

    /**
     * Function that launch execution of the client thread dedicated to downloading and updating e-mails.
     * @param email the e-mail string of the client
     */
    private void setConnection(String email, boolean firstTime) {
        this.email = email;
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.initStyle(StageStyle.UNDECORATED);
        dialog = a.getDialogPane();
        dialog.getStylesheets().add(ClientApplication.class.getResource("layout.css").toExternalForm());
        dialog.getStyleClass().add("dialog");
        this.cc = new ClientConnection(this.email, this.listView, this.emailList, a, firstTime);
        t = new Thread(cc);
        t.start();
    }

    /**
     * Function logout from the main interface, closes the socket and stops the thread and notifies the server
     * @param event the event
     */
    public void onExitButton(ActionEvent event) {
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            Socket s = new Socket(clientIP, 8189);
            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject("END CONNECTION-" + email);
            } finally {
                s.close();
                switchScene(event, "Login.fxml");
                t.interrupt();
            }
        } catch (IOException e) {
            System.out.println("-- Error, server temporary down --");
        }
    }

    /**
     * Change the scene and controller
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onWriteButton(ActionEvent event) throws IOException {
        t.interrupt();
        FXMLLoader loader = switchScene(event, "WriteEmail.fxml");
        EmailController scene2Controller = loader.getController();
        scene2Controller.initEmailController(this.email);
    }

    /**
     * Manages the choice of an element in the listview displays the selected mail, changes scene and controller
     * @param mouseEvent the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void handleMouseClick(MouseEvent mouseEvent) throws IOException {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            t.interrupt();
            ArrayList<Email> emailListUpdate = this.cc.getEmailListUP();
            this.emailList = reverseEmailList(emailListUpdate);
            Email emailSingle = this.emailList.get(index);

            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("ViewEmail.fxml"));
            root = loader.load();
            stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ClientApplication.class.getResource("layout.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMinHeight(450.0);
            stage.setMinWidth(600.0);
            stage.show();
            EmailController scene2Controller = loader.getController();
            scene2Controller.passEmail(emailSingle, this.email);
        }
    }

    /**
     * Function dedicated to updating e-mails for user interaction.
     * If the list of e-mails just downloaded has a different length from that of the thread dedicated
     * to the perpetual update then refresh the listview otherwise do nothing.
     * E-mails will be displayed from newest to oldest.
     */
    public void onRefreshButton() {
        ArrayList<Email> emailListDownloaded = downloadEmailList();
        if (emailListDownloaded != null && emailListDownloaded.size() != this.cc.getEmailListUP().size()) {
            this.listView.getItems().clear();
            this.cc.setEmailListUP(emailListDownloaded);
            this.emailList = reverseEmailList(emailListDownloaded);
            viewEmailList();
        }
    }

    /**
     * Communicates and obtains the updated list of e-mails from the server.
     * @return an ArrayList of e-mail of the user
     */
    private ArrayList<Email> downloadEmailList() {
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            Socket s = new Socket(clientIP, 8189);
            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject("UPDATE-" + this.email);

                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                EmailList emailObj = (EmailList)in.readObject();
                this.emailList = emailObj.getEmailList();

            } finally {
                s.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("-- Error, server temporary down --");
            return this.emailList;
        }
        return this.emailList;

    }

    /**
     * Add every e-mail in the ArrayList on the listview.
     */
    private void viewEmailList() {
        if (this.emailList != null && this.emailList.size() > 0) {
            for (Email tempEmail : this.emailList)
                listView.getItems().add("From: " + tempEmail.getSender() + " Object: " + tempEmail.getObject() + " Date: " + tempEmail.getDate());
        }
    }

    /**
     * Algorithm that inverts the email list so that it is sorted from the most recent to the oldest.
     * @param emailList the email list of the user
     * @return the sorted email list by date (from newest to oldest)
     */
    private ArrayList<Email> reverseEmailList(ArrayList<Email> emailList) {
        ArrayList<Email> revArrayList = new ArrayList<>();
        for (int i = emailList.size() - 1; i >= 0; i--)
            revArrayList.add(emailList.get(i));
        return revArrayList;
    }

    /**
     * Function dedicated to changing the scene
     * @param event the event
     * @param fileXML the file xfml to switch the scene
     * @return the loader
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    private FXMLLoader switchScene(ActionEvent event, String fileXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource(fileXML));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(ClientApplication.class.getResource("layout.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinHeight(450.0);
        stage.setMinWidth(600.0);
        stage.show();
        return loader;
    }

    /**
     * Function that, when the user clicks the X button, sends an exit confirmation alert,
     * if confirmed, the download and e-mail update process will be interrupted and
     * the application will be closed, otherwise will proceed.
     * @param stage the stage
     */
    @FXML
    private void exitApplication(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure to exit from application?");
        dialog = alert.getDialogPane();
        dialog.getStylesheets().add(ClientApplication.class.getResource("layout.css").toExternalForm());
        dialog.getStyleClass().add("dialog");
        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("-- You have successfully logged out --");
            this.t.interrupt();
            stage.close();
        }
    }
}