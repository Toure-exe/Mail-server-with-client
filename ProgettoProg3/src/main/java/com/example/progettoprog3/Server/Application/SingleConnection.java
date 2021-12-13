package com.example.progettoprog3.Server.Application;

import com.example.progettoprog3.Model.Email;
import com.example.progettoprog3.Model.EmailList;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class SingleConnection implements Runnable{

    @FXML
    private final TextArea log;

    private final Socket incoming;
    private final HashMap<String, Integer> users;
    private final Lock readLock;
    private final Lock writeLock;

    /**
     * Constructor of the class that will later be executed as a thread.
     * @param log log of the interfaceServer.fxml
     * @param incoming socket client-server
     * @param readLock read lock
     * @param writeLock write lock
     */
    public SingleConnection(TextArea log, Socket incoming, Lock readLock, Lock writeLock) {
        this.log = log;
        this.readLock = readLock;
        this.writeLock = writeLock;
        this.incoming = incoming;
        users = new HashMap<>();
        users.put("giulio@cesare.it", 0);
        users.put("mario@rossi.it", 1);
        users.put("luigi@bianchi.it", 2);
    }

    /**
     * Executable method of the thread, it will fulfill the client's request such as downloading, sending,
     * or deleting e-mails.
     * The success or failure of a successful client request will be printed in the server log.
     * If the request from the client is incorrect an error log will be printed.
     */
    @Override
    public void run() {
        String[] temp;
        System.out.println("ci entra");
        try {
            try {
                String path;
                ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                ObjectOutputStream outStream = new ObjectOutputStream(incoming.getOutputStream());
                Object container = in.readObject();
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
                        readLock.lock();
                        EmailList emailList = (EmailList)ois.readObject();
                        readLock.unlock();
                        ois.close();

                        //send the obj EmailList to the client
                        outStream.writeObject(emailList);
                        outStream.flush();
                        outStream.close();
                    } /*else if (cont.contains("@")) {
                        path = "src/main/java/com/example/progettoprog3/Server/User/" + cont + ".txt";
                        log.appendText("User: " + cont + " is logged in \n");
                        //read the obj EmailList from file
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                        readLock.lock();
                        EmailList emailList = (EmailList)ois.readObject();
                        readLock.unlock();
                        ois.close();

                        //send the obj EmailList to the client
                        outStream.writeObject(emailList);
                        outStream.flush();
                        outStream.close();
                    }*/
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
                        readLock.lock();
                        EmailList emailList = (EmailList)ois.readObject();
                        readLock.unlock();
                        ois.close();
                        //delete the email from arraylist
                        emailList.deleteEmail(email);
                        //write the updated obj EmailList into the file
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                        writeLock.lock();
                        oos.writeObject(emailList);
                        writeLock.unlock();
                        oos.flush();
                        oos.close();
                    } else {
                        //section false --> send email
                        String path1 = "src/main/java/com/example/progettoprog3/Server/User/";
                        ArrayList<String> receivers = email.getReceiver();
                        boolean res = true;
                        for (String receiver : receivers) {
                            if (!users.containsKey(receiver)) {
                                res = false;
                                break;
                            }
                        }
                        if (res) {
                            for (String receiver : receivers) {
                                //catch the obj EmailList from file
                                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path1 + receiver + ".txt"));
                                readLock.lock();
                                EmailList emailList = (EmailList)ois.readObject();
                                readLock.unlock();
                                emailList.addEmail(email);
                                ois.close();
                                //write the updated obj EmailList into the file
                                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path1 + receiver + ".txt"));
                                writeLock.lock();
                                oos.writeObject(emailList);
                                writeLock.unlock();
                                oos.flush();
                                oos.close();
                                log.appendText("New email arrived from: " + email.getSender() + " to: " + receiver + "\n");
                            }
                            outStream.writeObject("OK");
                        } else {
                            outStream.writeObject("ERROR");
                            log.appendText("Email error. Wrong receiver \n");
                        }
                    }
                } else
                    log.appendText("Error. Client request could not be fulfilled\n");
            } finally {
                incoming.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
