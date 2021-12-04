package com.example.progettoprog3.Server.Application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File.*;
import com.example.progettoprog3.Model.Email;
import com.example.progettoprog3.Model.EmailList;

public class SingleConnection implements Runnable{

    private Socket incoming = null;
    private Object container = null;

    @FXML
    private TextArea log;

    public SingleConnection(TextArea log, Socket incoming) {
        this.log = log;
        this.incoming = incoming;
    }

    @Override
    public void run() {
        String[] temp;
        System.out.println("ci entra");
        try {
            try {
                String path;
                ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                ObjectOutputStream outStream = new ObjectOutputStream(incoming.getOutputStream());
                //Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);
                container = in.readObject();
                if (container instanceof String) {
                    String cont = (String)container;

                    if (cont.contains("@") && cont.contains("END CONNECTION")) {
                        temp = cont.split("\\-");
                        log.appendText("User: " + temp[1] + " is logged out \n");
                    } else if (cont.contains("@") && cont.contains("UPDATE")) {
                        temp = cont.split("\\-");
                        System.out.println(temp[1]);
                        path = "src/main/java/com/example/progettoprog3/Server/User/" + temp[1] + ".txt";
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                        EmailList emailList = (EmailList)ois.readObject();
                        ois.close();

                        //send the obj EmailList to the client
                        outStream.writeObject(emailList);
                        outStream.flush();
                        outStream.close();
                    } else if (cont.contains("@")) {
                        path = "src/main/java/com/example/progettoprog3/Server/User/" + cont + ".txt";
                        log.appendText("User: " + cont + " is logged in \n");
                        //read the obj EmailList from file
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                        EmailList emailList = (EmailList)ois.readObject();
                        ois.close();

                        //send the obj EmailList to the client
                        outStream.writeObject(emailList);
                        outStream.flush();
                        outStream.close();
                    }
                } else if (container instanceof Email) {
                    System.out.println("!!!!!");
                    Email email = (Email)container;
                    String[] user = email.toStringReceiver().split(" ");
                    path = "src/main/java/com/example/progettoprog3/Server/User/" + user[0] + ".txt";
                    if (email.isDelete()) {
                        //section true --> delete email
                        log.appendText("User: " + email.toStringReceiver() + " has deleted: " + email.getObject() + " email \n");
                        //catch the obj EmailList from file
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                        EmailList emailList = (EmailList)ois.readObject();
                        ois.close();
                        //delete the email from arraylist
                        emailList.deleteEmail(email);
                        //write the updated obj EmailList into the file
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                        oos.writeObject(emailList);
                        oos.flush();
                        oos.close();
                    } else {
                        String path1 = "src/main/java/com/example/progettoprog3/Server/User/";
                        ArrayList<String> receivers = email.getReceiver();
                        for (String receiver : receivers) {
                            log.appendText("New email arrived from: " + email.getSender() + " to: " + receiver + "\n");
                            //catch the obj EmailList from file
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path1 + receiver + ".txt"));
                            EmailList emailList = (EmailList)ois.readObject();
                            emailList.addEmail(email);
                            ois.close();
                            //write the updated obj EmailList into the file
                            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path1 + receiver + ".txt"));
                            oos.writeObject(emailList);
                            oos.flush();
                            oos.close();
                        }
                        /*//section false --> send email
                        log.appendText("New email arrived from: " + email.getSender() + " to: " + email.toStringReceiver() + "\n");
                        System.out.println(path);

                        //catch the obj EmailList from file
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                        EmailList emailList = (EmailList)ois.readObject();
                        emailList.addEmail(email);
                        ois.close();
                        //write the updated obj EmailList into the file
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                        oos.writeObject(emailList);
                        oos.flush();
                        oos.close();*/
                    }

                }

            } finally {
                incoming.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
