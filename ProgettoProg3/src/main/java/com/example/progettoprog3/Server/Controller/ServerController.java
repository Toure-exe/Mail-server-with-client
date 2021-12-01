package com.example.progettoprog3.Server.Controller;

import com.example.progettoprog3.Server.Application.InitConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

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


}
