package com.example.progettoprog3.Client.Controller;

import com.example.progettoprog3.ClientApplication;
import com.example.progettoprog3.Model.Email;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.*;
import java.io.*;
import java.util.*;
import java.io.IOException;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class EmailController {

    @FXML
    private Button replyAllButton;
    @FXML
    private Button forwardAllButton;
    @FXML
    private TextField object;
    @FXML
    private TextField subject;
    @FXML
    private TextArea text;
    @FXML
    private TextArea textAreaEmail;
    @FXML
    private Label objectLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label senderLabel;
    @FXML
    private Button sendButton;


    private String email;
    private Email emailObj;
    private boolean homePage;

    /**
     * Initialization function of this class, every other controller must go through this function.
     * It sets the email string and the boolean to return to the home page
     * @param email the email string of the user
     */
    public void initEmailController(String email) {
        this.email = email;
        this.homePage = true;
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            event.consume();
            exitApplication(stage);
        });
    }

    /**
     * Function dedicated to the display of the email, the information on the screen is printed
     * through the email object taken as a parameter.
     * If the email has multiple recipients, the two buttons dedicated to multiple replies and forwards
     * will be displayed, otherwise they will be hidden.
     * @param emailSingle the e-mail objet
     * @param email the email string of the user
     */
    public void passEmail(Object emailSingle, String email) {
        if (emailSingle instanceof Email) {
            this.homePage = true;
            this.email = email;
            this.emailObj = (Email)emailSingle;
            textAreaEmail.setEditable(false);
            Stage stage = (Stage) objectLabel.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                event.consume();
                exitApplication(stage);
            });
            objectLabel.setText("Object: " + this.emailObj.getObject());
            dateLabel.setText("Date: " + this.emailObj.getDate());
            senderLabel.setText("Sender: " + this.emailObj.getSender());
            textAreaEmail.appendText(this.emailObj.getText());
            if (this.emailObj.getReceiver().size() == 1) {
                replyAllButton.setVisible(false);
                forwardAllButton.setVisible(false);
            }
        } else
            System.out.println("-- Error, the object passed as a parameter is not of type Email --");

    }

    /**
     * Function to reply to the message.
     * It changes scene and calls up the method to facilitate the auto-compilation
     * of the response in the subject and receiver fields.
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onReplyButton(ActionEvent event) throws IOException {
        FXMLLoader loader = switchScene(event, "WriteEmail.fxml");
        EmailController scene2Controller = loader.getController();
        scene2Controller.replyEmail(this.emailObj, this.email);
    }

    /**
     * It auto-completes the message fields such as subject and recipient to reply.
     * Set the boolean to return to the message display in case the user wants to.
     * @param emailObj the e-mail object
     * @param email the e-mail string of the user
     */
    private void replyEmail(Email emailObj, String email) {
        this.email = email;
        this.emailObj = emailObj;
        this.homePage = false;
        subject.setText(emailObj.getSender());
        object.setText("Reply: " + emailObj.getObject());
    }

    /**
     * Function to reply all to the message.
     * It changes scene and calls up the method to facilitate the auto-compilation
     * of the response in the subject and receiver fields.
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onReplyAllButton(ActionEvent event) throws IOException {
        FXMLLoader loader = switchScene(event, "WriteEmail.fxml");
        EmailController scene2Controller = loader.getController();
        scene2Controller.replyAllEmail(this.emailObj, this.email);
    }

    /**
     * It auto-completes the message fields such as subject and recipients to reply all.
     * Set the boolean to return to the message display in case the user wants to.
     * @param emailObj the e-mail object
     * @param email the e-mail string of the user
     */
    private void replyAllEmail(Email emailObj, String email) {
        this.email = email;
        this.emailObj = emailObj;
        this.homePage = false;
        object.setText("Reply: " + emailObj.getObject());
        String res = getReceivers(emailObj);
        subject.setText(emailObj.getSender() + " " + res);
    }

    /**
     * Function dedicated to the extrapolation of receivers for self-compilation in case of reply all or forward all
     * @param emailObj the e-mail object
     * @return the receivers String to print in the subject field
     */
    private String getReceivers(Email emailObj) {
        ArrayList<String> receiverList = emailObj.getReceiver();
        String res = "";
        for (String receiver : receiverList) {
            if (!receiver.equals(this.email))
                res += receiver + " ";
        }
        return res;
    }

    /**
     * Function to forward to the message.
     * It changes scene and calls up the method to facilitate the auto-compilation
     * of the response in the subject and receiver fields.
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onForwardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = switchScene(event, "WriteEmail.fxml");
        EmailController scene2Controller = loader.getController();
        scene2Controller.forwardEmail(this.emailObj, this.email);
    }

    /**
     * It auto-completes the message fields such as subject and text to forward.
     * Set the boolean to return to the message display in case the user wants to.
     * @param emailObj the e-mail object
     * @param email the e-mail string of the user
     */
    private void forwardEmail(Email emailObj, String email) {
        this.email = email;
        this.emailObj = emailObj;
        this.homePage = false;
        subject.setText(emailObj.getSender());
        object.setText(emailObj.getObject());
        text.setText("\nForwarded message: \n" + emailObj.getText());
    }

    /**
     * Function to forward all to the message.
     * It changes scene and calls up the method to facilitate the auto-compilation
     * of the response in the subject and receiver fields.
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onForwardAllButton(ActionEvent event) throws IOException {
        FXMLLoader loader = switchScene(event, "WriteEmail.fxml");
        EmailController scene2Controller = loader.getController();
        scene2Controller.forwardAllEmail(this.emailObj, this.email);
    }

    /**
     * It auto-completes the message fields such as subject and text to forward all.
     * Set the boolean to return to the message display in case the user wants to.
     * @param emailObj the e-mail object
     * @param email the e-mail string of the user
     */
    private void forwardAllEmail(Email emailObj, String email) {
        this.email = email;
        this.emailObj = emailObj;
        this.homePage = false;
        object.setText(emailObj.getObject());
        text.setText("\nForwarded message: \n" + emailObj.getText());
        String res = getReceivers(emailObj);
        subject.setText(emailObj.getSender() + " " + res);
    }

    /**
     * Function dedicated to the deletion of an e-mail by the client, the email to be deleted will be sent with the flag
     * dedicated to deletion set to true so that the server can take the desired option.
     * true --> delete e-mail, false --> send e-mail
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onDeleteButton(ActionEvent event) throws IOException {
        this.emailObj.setDelete(true);
        ArrayList<String> receiver = new ArrayList<>();
        receiver.add(this.email);
        this.emailObj.setReceiver(receiver);
        if (sendEmail(this.emailObj)) {
            FXMLLoader loader = switchScene(event, "InterfaceClient.fxml");
            ClientController scene2Controller = loader.getController();
            scene2Controller.initClientController(this.email, false);
        }
    }

    /**
     * Function dedicated to the passage of the scene to the previous screen.
     * If the client opens an e-mail the back button will change the scene in InterfaceClient.fxml
     * If the client was replying or forwarding the message, the back button will bring it back to the display of the specific e-mail
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader;
        if (this.homePage) {
            //return to the home page (Interface.xfml)
            loader = switchScene(event, "InterfaceClient.fxml");
            ClientController scene2Controller = loader.getController();
            scene2Controller.initClientController(this.email, false);
        } else {
            //return to the view email (ViewEmail.xfml)
            loader = switchScene(event, "ViewEmail.fxml");
            EmailController scene2Controller = loader.getController();
            scene2Controller.passEmail(this.emailObj, this.email);
        }

    }

    /**
     * Function dedicated to data extraction, if the receiver (or subject) field is correct,
     * an e-mail object is created which will then be sent to the server, and then moved to InterfaceClient.fxml
     * otherwise, an error alert will be sent to the client while remaining on the same page.
     * @param event the event
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    public void onSendButton(ActionEvent event) throws IOException {
        String obj; String sbj; String textEmail; String date;
        boolean res = true;
        obj = object.getText();
        textEmail = text.getText();
        sbj = subject.getText();

        //several checks in the subject field
        if (sbj.isEmpty())
            res = false;
        else {
            String[] temp = sbj.split(" ");
            if (temp.length == 0)
                res = false;
            else {
                for (int i = 0; i < temp.length && res; i++) {
                    if (!temp[i].contains("@") || (!temp[i].contains(".it") && !temp[i].contains(".com"))) {
                        if (!temp[i].isBlank())
                            res = false;
                    }
                }
            }
        }
        if (res) {
            //email insert correctly
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            date = dtf.format(now);
            Email email = new Email(sbj, this.email, obj, textEmail, date);
            if (sendEmail(email)) {
                FXMLLoader loader = switchScene(event, "InterfaceClient.fxml");
                ClientController scene2Controller = loader.getController();
                scene2Controller.initClientController(this.email, false);
            }
        } else {
            //email insert not correctly
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Wrong email, please write again");
            Optional<ButtonType> result = a.showAndWait();
            if (!result.isPresent() || result.get() == ButtonType.OK) {
                // alert is exited, no button has been pressed or okay button is pressed
                subject.setText("");
            }
        }
    }

    /**
     * Function dedicated to sending the e-mail object to the server (to be sent or deleted from the list).
     * @param email the e-mail object to send to the server
     * @return true if the server replies with an OK message, false if it replies with an ERROR message
     */
    private boolean sendEmail(Email email) {
        boolean res = false;
        Alert a = new Alert(Alert.AlertType.ERROR);
        try {
            String clientIP = InetAddress.getLocalHost().getHostName();
            Socket s = new Socket(clientIP, 8189);
            try {
                //send the email to the server
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(email);
                //receive the response
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                String response = (String)in.readObject();
                if (response.equals("ERROR")) {
                     // set alert type
                    a.setContentText("Email error: user not exists");
                    a.show();
                } else if (response.equals("OK")) {
                    res =  true;
                }
            } catch (ClassNotFoundException e1) {
                System.out.println("-- Error, object obtained not of type String --");
            } finally {
                s.close();
            }
        } catch (IOException e) {
            System.out.println("-- Error, server temporary down --");
            a.setContentText("Server temporary down, please try again");
            a.show();
            return false;
        }
        return res;
    }

    /**
     * Function dedicated to changing the scene
     * @param event the event
     * @param fileXML the file xfml to switch the scene
     * @return the loader
     * @throws IOException when the FXMLLoader loader the controller improperly
     */
    private FXMLLoader switchScene(ActionEvent event, String fileXML) throws IOException {
        Stage stage; Scene scene; Parent root;
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
     * Function that, when the admin clicks the X button, sends an exit confirmation alert,
     * If the server thread is active and the alert is confirmed it will be aborted and the socket will be closed.
     * If the server thread is down, and you confirm the alert the application will be closed.
     * @param stage the stage
     */
    @FXML
    private void exitApplication(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure to exit from application?");
        if (alert.showAndWait().get() == ButtonType.OK){
            System.out.println("-- You successfully logged out --");
            stage.close();
        }
    }
}