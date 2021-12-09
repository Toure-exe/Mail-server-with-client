package com.example.progettoprog3.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
//import java.util.Date;

public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID ID;
    private ArrayList<String> receiver;
    private String sender;
    private String object;
    private String text;
    private String date;
    private boolean delete;

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

    public UUID getID() {
        return ID;
    }

    public ArrayList<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(ArrayList<String> receiver) {
        this.receiver = receiver;
    }

    public String toStringReceiver() {
        String res = "";
        for (String temp : this.receiver) {
            res += temp + " ";
        }
        return res;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
