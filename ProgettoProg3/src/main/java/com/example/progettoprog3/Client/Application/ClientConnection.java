package com.example.progettoprog3.Client.Application;

import com.example.progettoprog3.Model.Email;

import com.example.progettoprog3.Model.EmailList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientConnection implements Runnable {

    @FXML
    private final ListView<String> listView;

    private final String email;
    private final Alert alert;
    private boolean firstTime;
    private String clientIP = null;
    private String show;
    private ArrayList<Email> currentEmailList;

    /**
     * Constructor of the class that will later be executed as a thread.
     * @param email e-mail of the client
     * @param listView list view of e-mail of the client
     * @param currentEmailList current list of e-mail of the client
     * @param alert alert error
     */
    public ClientConnection(String email, ListView<String> listView, ArrayList<Email> currentEmailList, Alert alert, boolean fistTime) {
        this.email = email;
        this.listView = listView;
        this.currentEmailList = currentEmailList;
        this.alert = alert;
        this.show = null;
        this.firstTime = fistTime;
    }

    /**
     * Executable method of the thread client, every 5 seconds it downloads the e-mail and checks if there have been
     * updated, if there are it notifies the user through an alert, otherwise it does nothing.
     *
     * To perform updates on the xfml file such as sending alerts or updating the list view, disposable executables
     * will be created using the static method Platform.runLater.
     *
     * If the connection with the server fails, it sends a permanent alert to the client that will prevent them
     * from using the connection, while the thread will try to reconnect with the server, if the connection is
     * re-established, the thread will make the alert disappear.
     *
     * The thread will be executed only when the user is in the InterfaceClient.fxml interface and will be interrupted
     * when the user changes scene, and then recreates when he returns to the interface.
     */
    @Override
    public void run() {
        ArrayList<Email> updatedEmailList;
        Socket s;
        UpdateVisualization uv;
        ConnectionError ce;
        try {
            clientIP = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                s = new Socket(clientIP, 8189);
                if (this.show != null) {
                    ce = new ConnectionError(this.alert, true, this.show);
                    Platform.runLater(ce);
                    this.show = null;
                }
                try {
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    //send email user if this is the firs time from the server
                    if (this.firstTime) {
                        out.writeObject(this.email);
                        this.firstTime = false;
                    }
                    //notify the server an emailList update
                    out.writeObject("UPDATE-" + this.email);

                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    EmailList emailObj = (EmailList)in.readObject();

                    if (this.currentEmailList == null) {
                        this.currentEmailList = emailObj.getEmailList();
                        uv = new UpdateVisualization(this.listView, currentEmailList, false);
                        Platform.runLater(uv);
                    } else {
                        updatedEmailList = emailObj.getEmailList();
                        if (currentEmailList.size() < updatedEmailList.size()) {
                            currentEmailList = updatedEmailList;
                            //launch the alert and reload the email box
                            uv = new UpdateVisualization(this.listView, currentEmailList, true);
                            Platform.runLater(uv);
                        }
                    }
                } finally {
                    s.close();
                    Thread.sleep(5000);
                }
            } catch (IOException e) {
                if (this.show == null) {
                    this.show = "NOT NULL";
                    ce = new ConnectionError(this.alert, false, this.show);
                    Platform.runLater(ce);
                }
            } catch (InterruptedException | ClassNotFoundException e1) {
                System.out.println("-- Killed process --");
                break;
            }
        }
    }

    /**
     * Function to get the current e-mail list
     * @return the current list of e-mail
     */
    public ArrayList<Email> getEmailListUP() {
        return currentEmailList;
    }

    /**
     * Function to set the emailList updated when the user push the refresh button
     * if the length of this emailList is different from that downloaded in ClientConnection.
     * @param emailListUP the emailList to be updated
     */
    public void setEmailListUP(ArrayList<Email> emailListUP) {
        this.currentEmailList = emailListUP;
    }
}