package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */
class TCPsend extends Thread {
    private Socket Sock;
    private File File;

    TCPsend(Socket sock, String filename) {
        this.Sock = sock;
        this.File = new File(filename);

        if (File.exists()) {
            try {
                while (Sock.isConnected()) {
                    byte[] size = new byte[(int) File.length()];
                    BufferedInputStream inFromFile = new BufferedInputStream(new FileInputStream(File));
                    inFromFile.read(size, 0, (int) File.length());

                    DataOutputStream outToClient = new DataOutputStream(Sock.getOutputStream());
                    outToClient.write(size, 0, (int) File.length());

                    Sock.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean isConnected() {
        return (Sock.isConnected());
    }
}
