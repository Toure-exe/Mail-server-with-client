package com.example.progettoprog3.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.*;
import java.io.*;
import java.util.*;
import com.example.progettoprog3.Model.Email;

public class ClientController {
    private String email;

    @FXML
    Label benvenutoLabel;

    /*@FXML
    protected void initialize(){

    }*/

    public void displayName(String email) {
        benvenutoLabel.setText("Hello: " + email);
        setConnection(email);
    }

    public void setConnection(String email) {
        System.out.println(email);
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            System.out.println(clientIP);
            Socket s = new Socket(clientIP, 8189);
            System.out.println("Ho aperto il socket verso il server.\n");
            try {
                InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream();
                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                out.println(email);
            } finally {
                s.close();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}
