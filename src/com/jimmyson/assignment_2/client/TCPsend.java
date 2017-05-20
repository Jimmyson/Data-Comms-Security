package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */
public class TCPsend extends Thread {
    private Socket Sock;
    private File File;

    TCPsend(Socket sock, String filename) {
        this.Sock = sock;
        this.File = new File(filename);
    }

    public void run() {
        //FIND FILE
        if (File.exists()) {
            try {
                BufferedReader inFromFile = new BufferedReader(new FileReader(File));

                while (Sock.isConnected()) {
                    DataOutputStream outToClient = new DataOutputStream(Sock.getOutputStream());
                    outToClient.writeBytes(inFromFile.readLine());
                }

                Sock.close();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean isConnected() {
        return (Sock.isConnected());
    }
}
