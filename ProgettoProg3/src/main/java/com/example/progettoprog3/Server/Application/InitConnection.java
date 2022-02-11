package com.example.progettoprog3.Server.Application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class InitConnection implements Runnable {

    @FXML
    private final TextArea log;

    private final ServerSocket s;
    private final Lock readLock;
    private final Lock writeLock;

    /**
     * Constructor of the class that will later be executed as a thread.
     * @param log log of the interfaceServer.fxml
     * @param s server socket to accept the client's request
     */
    public InitConnection(TextArea log, ServerSocket s) {
        ReadWriteLock rwl = new ReentrantReadWriteLock();
        this.log = log;
        this.s = s;
        this.readLock = rwl.readLock();
        this.writeLock = rwl.writeLock();
    }

    /**
     * Executable method of the thread, it waits for a connection from a client and, when one arrives,
     * it creates a thread dedicated to that client that will satisfy its specific request.
     * For management reasons no more than 5 threads can run at the same time, if this happens (continuare...)
     * This executable remains alive until the server will be shut down.
     */
    @Override
    public void run() {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        SingleConnection sc;
        while (true) {
            try {
                try {
                    Socket incoming = s.accept();
                    sc = new SingleConnection(log, incoming, this.readLock, this.writeLock);
                    exec.execute(sc);
                } catch (SocketException sckt) {
                    exec.shutdown();
                    s.close();
                    System.out.println("-- Server, turned off --");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}