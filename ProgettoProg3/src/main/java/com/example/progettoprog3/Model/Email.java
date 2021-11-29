package com.example.progettoprog3.Model;

import java.util.Date;

public class Email {
    private String ID;
    private String receiver;
    private String sender;
    private String object;
    private String text;
    private Date date;

    public Email(String ID, String receiver, String sender, String object, String text, Date date) {
        this.ID = ID;
        this.receiver = receiver;
        this.sender = sender;
        this.object = object;
        this.text = text;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
