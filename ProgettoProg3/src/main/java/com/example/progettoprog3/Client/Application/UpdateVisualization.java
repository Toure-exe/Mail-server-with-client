package com.example.progettoprog3.Client.Application;

import com.example.progettoprog3.Model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Optional;

public class UpdateVisualization implements Runnable {

    @FXML
    private ListView listView;

    private ArrayList<Email> emailList;
    private boolean alert;

    public UpdateVisualization(ListView listView, ArrayList<Email> emailList, boolean alert) {
        this.listView = listView;
        this.emailList = emailList;
        this.alert = alert;
    }

    @Override
    public void run() {
        if (alert) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION); // set alert type
            a.setTitle("New Email");
            a.setContentText("You have new email!");
            a.show();
        }

        System.out.println(listView);
        ArrayList<Email> emailListrReverse;
        emailListrReverse = reverseEmailList(emailList);
        this.listView.getItems().clear();
        if (emailListrReverse != null && emailListrReverse.size() > 0) {
            for (Email tempEmail : emailListrReverse) {
                System.out.println("ci entri");
                this.listView.getItems().add("From: " + tempEmail.getSender() + " Object: " + tempEmail.getObject() + " Date: " + tempEmail.getDate());
                //listView.getItems().add(tempEmail);
            }
        }
        this.listView = null;

    }

    private ArrayList<Email> reverseEmailList(ArrayList<Email> emailList) {
        ArrayList<Email> revArrayList = new ArrayList<>();
        for (int i = emailList.size() - 1; i >= 0; i--) {
            revArrayList.add(emailList.get(i));
        }
        return revArrayList;
    }
}
