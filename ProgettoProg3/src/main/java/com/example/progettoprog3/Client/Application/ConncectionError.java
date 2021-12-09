package com.example.progettoprog3.Client.Application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class ConncectionError implements Runnable {

    private boolean hide;
    private Alert alert;
    private String show;

    public ConncectionError(Alert alert, boolean hide, String show){
        this.alert = alert;
        this.hide = hide;
        this.show = show;
    }

    @Override
    public void run() {
        if (this.show != null) {
            if (hide) {
                //hide the alert --> conncection to server established
                this.alert.hide();
            } else {
                //show the alert --> connection to server lost
                this.alert.setAlertType(Alert.AlertType.ERROR); // set alert type
                this.alert.setContentText("Server temporary down, please wait...");
                this.alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                this.alert.show();// show the dialog
            }
        }
    }
}
