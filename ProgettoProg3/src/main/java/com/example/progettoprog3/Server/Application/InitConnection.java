package com.example.progettoprog3.Server.Application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InitConnection implements Runnable{

    private ServerSocket s = null;
    private ExecutorService exec = null;
    private SingleConnection sc = null;
    private ReadWriteLock rwl = null;
    private Lock readLock = null;
    private Lock writeLock = null;


    @FXML
    private TextArea log;

    public InitConnection(TextArea log, ServerSocket s) {
        // store parameter for later user
        this.log = log;
        this.s = s;
        this.rwl = new ReentrantReadWriteLock();
        this.readLock = rwl.readLock();
        this.writeLock = rwl.writeLock();
    }

    @Override
    public void run() {
        exec = Executors.newFixedThreadPool(5);
        while(true) {
            System.out.println(Thread.currentThread().getId());
            System.out.println(s);
            try {
                try {
                    Socket incoming = s.accept();
                    sc = new SingleConnection(log, incoming, this.readLock, this.writeLock);
                    exec.execute(sc);
                } catch (SocketException sckt) {
                    exec.shutdown();
                    s.close();
                    System.out.println("listen here, you little shit");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
