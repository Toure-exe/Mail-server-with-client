package com.example.progettoprog3.Server.Controller;

import com.example.progettoprog3.Server.Application.InitConnection;
import com.example.progettoprog3.Model.EmailList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class ServerController {

    @FXML
    private TextArea log;

    private ServerSocket s = null;
    private Thread t = null;

    @FXML
    protected void initialize() { log.setEditable(false); }

    /**
     * Class initialization function, sets a handler for the management of button X.
     */
    public void initServerController() {
        Stage stage = (Stage) log.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            event.consume();
            try {
                exitApplication(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * It creates the thread server dedicated to receiving requests from clients.
     */
    private void setConnection() {
        try {
            this.s = new ServerSocket(8189);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InitConnection init = new InitConnection(this.log, this.s);
        this.t = new Thread(init);
        this.t.start();
    }

    /**
     * Turn on the server.
     * If the server was already on, it prints an error message in the interface log.
     * Otherwise, it prints a message confirming the activation of the server in the interface log.
     */
    public void onStartButton() {
        if (this.t == null) {
            this.log.appendText("Server on \n");
            setConnection();
        } else
            this.log.appendText("Server already on \n");
    }

    /**
     * Turn off the server.
     * If the server was already off, it prints an error message in the interface log.
     * Otherwise, it prints a message confirming the shutdown of the server,
     * that is, interrupting the thread dedicated to receiving request from clients and closing the socket
     * @throws IOException when the socket closes improperly
     */
    public void onEndButton() throws IOException {
        if (this.t == null)
            this.log.appendText("Server already down \n");
        else {
            this.t.interrupt();
            this.s.close();
            this.t = null;
            this.log.appendText("Server off \n");
        }
    }

    /**
     * Function that, when the user clicks the X button, sends an exit confirmation alert,
     * if confirmed, the application will be closed, otherwise will proceed.
     * @param stage the stage
     */
    @FXML
    private void exitApplication(Stage stage) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        if (this.t != null) {
            //thread server is active
            alert.setContentText("Are you sure to exit from application? \nThis will cause the turn off of the server");
            if (alert.showAndWait().get() == ButtonType.OK){
                System.out.println("-- You successfully logged out --");
                this.t.interrupt();
                this.s.close();
                this.t = null;
                stage.close();
            }
        } else {
            //thread server is not active
            alert.setContentText("Are you sure to exit from application?");
            if (alert.showAndWait().get() == ButtonType.OK){
                System.out.println("-- You successfully logged out --");
                stage.close();
            }
        }
    }

    /**
     * Reset the memory of the server.
     * It'll set the memory of all users empty.
     */
    @FXML
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

            log.appendText("The memory is been reset\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}