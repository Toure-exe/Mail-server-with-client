package com.example.progettoprog3.Model;

import java.util.ArrayList;
import java.io.Serializable;

public class EmailList implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Email> emailList = null;

    public EmailList() {
        this.emailList = new ArrayList<>();
    }

    public void addEmail(Email email) {
        this.emailList.add(email);
    }

    public void searchEamil(Email email) {

    }

    public void deleteEmail(Email email) {
        int i = 0;
        for (Email temp : this.emailList) {
            if (temp.getID().equals(email.getID())){
                break;
            }
            i++;
        }
        this.emailList.remove(i);
    }

    public ArrayList<Email> getEmailList() {
        return this.emailList;
    }

    public Email getEmail(int index) {
        if (index < emailList.size())
            return this.emailList.get(index);
        else
            return null;
    }

}
