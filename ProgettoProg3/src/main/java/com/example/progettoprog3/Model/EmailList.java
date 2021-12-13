package com.example.progettoprog3.Model;

import java.io.Serial;
import java.util.ArrayList;
import java.io.Serializable;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class EmailList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<Email> emailList;

    /**
     * Constructor of the EmailList, it creates an empty ArrayList of e-mail for the user.
     */
    public EmailList() {
        this.emailList = new ArrayList<>();
    }

    /**
     * Function to add an e-mail into the ArrayList of e-mail.
     * @param email the e-mail object to add in
     */
    public void addEmail(Email email) {
        this.emailList.add(email);
    }

    /**
     * Function to delete an e-mail from the ArrayList of e-mail.
     * @param email the e-mail to be deleted
     */
    public void deleteEmail(Email email) {
        int i = 0;
        for (Email temp : this.emailList) {
            if (temp.getID().equals(email.getID()))
                break;
            i++;
        }
        this.emailList.remove(i);
    }

    /**
     * Get function of the list of e-mail
     * @return the ArrayList of e-mail
     */
    public ArrayList<Email> getEmailList() {
        return this.emailList;
    }
}
