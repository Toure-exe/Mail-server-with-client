package com.example.progettoprog3.Client.Application;

import com.example.progettoprog3.ClientApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class ConnectionError implements Runnable {

    private final boolean hide;
    private final Alert alert;
    private final String show;
    private DialogPane dialog;

    /**
     * Constructor of the class that will later be executed as a thread.
     * @param alert alert error
     * @param hide used to launch the alert in case of failed connection or hide the alert in case of reestablished connection
     * @param show used to not send a new alert if one is already present
     */
    public ConnectionError(Alert alert, boolean hide, String show){
        this.alert = alert;
        this.hide = hide;
        this.show = show;
    }

    /**
     * Disposable Executable method of the thread client, it has the task of reporting the loss of connection
     * from the server by launching an alert or hiding the alert in case of connection re-established.
     */
    @Override
    public void run() {
        if (this.show != null) {
            if (hide) {
                //hide the alert --> connection to server established
                this.alert.hide();
            } else {
                //show the alert --> connection to server lost
                this.alert.setAlertType(Alert.AlertType.ERROR); // set alert type
                this.alert.setContentText("Server temporary down, please wait...");
                this.alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                dialog = this.alert.getDialogPane();
                dialog.getStylesheets().add(ClientApplication.class.getResource("layout.css").toExternalForm());
                dialog.getStyleClass().add("dialog");
                this.alert.show();// show the dialog
            }
        }
    }
}