package com.example.progettoprog3.Client.Application;

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

import com.example.progettoprog3.Model.EmailList;
import com.example.progettoprog3.Model.Email;

public class ClientConnection implements Runnable {

    @FXML
    private ListView listView;

    private String email;
    volatile ArrayList<Email> currentEmailList = null;
    private ArrayList<Email> updatedEmailList = null;
    private String clientIP = null;
    private Socket s = null;
    private UpdateVisualization uv;
    private ConncectionError ce;
    private Alert alert;
    private String show;

    public ClientConnection(String email, ListView listView, ArrayList<Email> currentEmailList, Alert alert) {
        this.email = email;
        this.listView = listView;
        this.currentEmailList = currentEmailList;
        this.alert = alert;
        this.show = null;
    }

    @Override
    public void run() {
        try {
            clientIP = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                s = new Socket(clientIP, 8189);
                ce = new ConncectionError(this.alert, true, this.show);
                Platform.runLater(ce);
                this.show = null;
                try {
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    out.writeObject("UPDATE-" + this.email);

                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    EmailList emailobj = (EmailList)in.readObject();

                    if (this.currentEmailList == null) {
                        this.currentEmailList = emailobj.getEmailList();
                        uv = new UpdateVisualization(this.listView, currentEmailList, false);
                        Platform.runLater(uv);
                    } else {
                        this.updatedEmailList = emailobj.getEmailList();
                        if (currentEmailList.size() < updatedEmailList.size()) {
                            System.out.println("invio alert nuova email");
                            currentEmailList = updatedEmailList;
                            //launch the alert and reload the email box
                            uv = new UpdateVisualization(this.listView, currentEmailList, true);
                            Platform.runLater(uv);
                        /*Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });*/

                        } else {
                            System.out.println("non fare nulla");
                        }
                    }
                } finally {
                    s.close();
                    Thread.sleep(5000);
                }
            } catch (IOException e) {
                //e.printStackTrace();
                //break;
                if (this.show == null) {
                    this.show = "NOT NULL";
                    ce = new ConncectionError(this.alert, false, this.show);
                    Platform.runLater(ce);
                }
            } catch (InterruptedException | ClassNotFoundException e1) {
                System.out.println("Killed process.");
                break;
            }
        }
    }

    public ArrayList<Email> getEmailListUP() {
        return currentEmailList;
    }

}
