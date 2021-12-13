package com.example.progettoprog3.Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID ID;
    private final String sender;
    private final String object;
    private final String text;
    private final String date;
    private ArrayList<String> receiver;
    private boolean delete;

    /**
     * Constructor of the e-mail class, creates the e-mail object by checking the receiver field in order to
     * eliminate the separators (blank) of the e-mails.
     * @param receiver receiver (o receivers) of the e-mail
     * @param sender sender of the e-mail
     * @param object object of the e-mail
     * @param text text of the e-mail
     * @param date date of the email at the time of sending
     */
    public Email(String receiver, String sender, String object, String text, String date) {
        this.ID = UUID.randomUUID();
        this.receiver = new ArrayList<>();
        String[] temp = receiver.split(" ");
        for (int i = 0; i < temp.length; i++) {
            if (!temp[i].isBlank())
                this.receiver.add(temp[i]);
        }
        this.sender = sender;
        this.object = object;
        this.text = text;
        this.date = date;
        this.delete = false;
    }

    /**
     * Get function of the parameter UUID ID.
     * @return the ID of the e-mail
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Get function of the parameter ArrayList<String> receiver.
     * @return the ArrayList of string receiver of the e-mail
     */
    public ArrayList<String> getReceiver() {
        return receiver;
    }

    /**
     * Set a new ArrayList of string receiver.
     * @param receiver the arrayList of string receiver
     */
    public void setReceiver(ArrayList<String> receiver) {
        this.receiver = receiver;
    }

    /**
     * ToString method that generates a String of receiver separated by a blank.
     * @return a string of receiver
     */
    public String toStringReceiver() {
        String res = "";
        for (String temp : this.receiver)
            res += temp + " ";
        return res;
    }

    /**
     * Get function of the parameter String sender.
     * @return the sender of the e-mail
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get function of the parameter String object.
     * @return the object of the e-mail
     */
    public String getObject() {
        return object;
    }

    /**
     * Get function of the parameter String text.
     * @return the text of the e-mail
     */
    public String getText() {
        return text;
    }

    /**
     * Get function of the parameter String date.
     * @return the date of the e-mail
     */
    public String getDate() {
        return date;
    }

    /**
     * Is function of the parameter boolean delete.
     * @return the boolean to check if is an email to send or delete
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Set function to set the parameter boolean delete
     * @param delete the boolean to check if is an email to send or delete
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
