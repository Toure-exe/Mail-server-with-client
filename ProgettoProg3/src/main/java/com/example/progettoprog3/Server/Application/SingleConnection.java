package com.example.progettoprog3.Server.Application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import com.example.progettoprog3.Model.Email;

public class SingleConnection implements Runnable{

    private Socket incoming = null;
    private Object container = null;

    @FXML
    private TextArea log;

    public SingleConnection(TextArea log, Socket incoming) {
        this.log = log;
        this.incoming = incoming;
    }

    @Override
    public void run() {
        System.out.println("ci entra");
        try {
            try {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                container = in.nextLine();
                if (container instanceof String) {
                    String cont = (String)container;
                    if (cont.contains("@") && cont.contains("END CONNECTION")) {
                        String[] temp = cont.split("\\-");
                        log.appendText("User: " + temp[1] + " is logged out \n");
                    } else if (cont.contains("@")) {
                        log.appendText("User: " + cont + " is logged in\n");
                    }
                } else if (container instanceof Email) {
                    System.out.println("!!!!!");
                }

            } finally {
                incoming.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
