package com.example.progettoprog3.Model;

import java.io.Serializable;
import java.util.UUID;
//import java.util.Date;

public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID ID;
    private String receiver;
    private String sender;
    private String object;
    private String text;
    private String date;

    public Email(String receiver, String sender, String object, String text, String date) {
        this.ID = UUID.randomUUID();
        this.receiver = receiver;
        this.sender = sender;
        this.object = object;
        this.text = text;
        this.date = date;
    }

    public UUID getID() {
        return ID;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
}
