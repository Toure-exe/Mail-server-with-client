package com.example.progettoprog3.Client.Application;

import com.example.progettoprog3.Model.Email;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import java.util.ArrayList;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class UpdateVisualization implements Runnable {

    @FXML
    private ListView<String> listView;

    private final ArrayList<Email> emailList;
    private final boolean alert;

    /**
     * Constructor of the class that will later be executed as a thread.
     * @param listView list view of e-mail of the client
     * @param emailList list of e-mail of the client
     * @param alert used to not send a new alert if one is already present. Update the listview anyway
     */
    public UpdateVisualization(ListView<String> listView, ArrayList<Email> emailList, boolean alert) {
        this.listView = listView;
        this.emailList = emailList;
        this.alert = alert;
    }

    /**
     * Disposable Executable method of the thread client, It sends an alert to the client if new emails have arrived
     * and updates the listview sorted by date from the most recent to the oldest.
     * If the alert is already present it will not be relaunched but only the listview will be updated.
     */
    @Override
    public void run() {
        if (this.alert) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("New Email");
            a.setContentText("You have new email!");
            a.show();
        }
        System.out.println(listView);
        ArrayList<Email> emailListrReverse = reverseEmailList(this.emailList);
        this.listView.getItems().clear();
        if (emailListrReverse != null && emailListrReverse.size() > 0) {
            for (Email tempEmail : emailListrReverse) {
                System.out.println("ci entri");
                this.listView.getItems().add("From: " + tempEmail.getSender() + " Object: " + tempEmail.getObject() + " Date: " + tempEmail.getDate());
            }
        }
        this.listView = null;
    }

    /**
     * Algorithm that inverts the email list so that it is sorted from the most recent to the oldest.
     * @param emailList the email list of the user
     * @return the sorted email list by date (from newest to oldest)
     */
    private ArrayList<Email> reverseEmailList(ArrayList<Email> emailList) {
        ArrayList<Email> revArrayList = null;
        if (emailList != null) {
            revArrayList = new ArrayList<>();
            for (int i = emailList.size() - 1; i >= 0; i--)
                revArrayList.add(emailList.get(i));
        }
        return revArrayList;
    }
}
