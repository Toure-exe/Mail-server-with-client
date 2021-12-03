package com.example.progettoprog3.Server.Controller;

import com.example.progettoprog3.Server.Application.InitConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import com.example.progettoprog3.Model.EmailList;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerController {

    private ServerSocket s = null;
    private ExecutorService exec = null;
    private Thread t = null;
    private InitConnection init = null;

    @FXML
    private TextArea log;

    @FXML
    private Button startButton;

    @FXML
    private Button endButton;

    @FXML
    private Button resetEmailButton;

    private String email;

    @FXML
    protected void initialize(){
        log.setEditable(false);
    }

    private void setConnection() {
        try {
            this.s = new ServerSocket(8189);
        } catch (IOException e) {
            e.printStackTrace();
        }

        init = new InitConnection(log, s);
        t = new Thread(init);
        t.start();
    }

    public void onStartButton() throws IOException {
        if (t == null) {
            log.appendText("Server on \n");
            setConnection();
        } else
            log.appendText("Server already on \n");

    }

    public void onEndButton() throws IOException {
        if(t == null)
            log.appendText("Server already down \n");
        else {
            t.interrupt();
            s.close();
            t = null;
            log.appendText("Server off \n");
        }
    }

    public void onResetEmailButton() {
        EmailList giulioCesare = new EmailList();
        EmailList luigiBianchi = new EmailList();
        EmailList marioRossi = new EmailList();
        try {
            String path = "src/main/java/com/example/progettoprog3/Server/User/";

            //File f = new File(path + "giulio@cesare.it.txt");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "giulio@cesare.it.txt"));
            oos.writeObject(giulioCesare);
            oos.flush();
            oos.close();

            //f = new File(path + "luigi@bianchi.it.txt");
            oos = new ObjectOutputStream(new FileOutputStream(path + "luigi@bianchi.it.txt"));
            oos.writeObject(luigiBianchi);
            oos.flush();
            oos.close();

            //f = new File(path + "mario@rossi.it.txt");
            oos = new ObjectOutputStream(new FileOutputStream(path + "mario@rossi.it.txt"));
            oos.writeObject(marioRossi);
            oos.flush();
            oos.close();

            log.appendText("The memory is reset\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
