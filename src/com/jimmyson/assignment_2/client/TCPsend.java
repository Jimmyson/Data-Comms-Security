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
                DataOutputStream outToClient = new DataOutputStream(Sock.getOutputStream());
                DataInputStream input = new DataInputStream(Sock.getInputStream());

                while (Sock.isConnected()) {
                    long pos = 0;
                    while(pos < File.length()) {
                        byte[] size = new byte[(int) File.length()];
                        BufferedInputStream inFromFile = new BufferedInputStream(new FileInputStream(File));
                        inFromFile.read(size, 0, size.length);

                        outToClient.writeLong(File.length());
                        outToClient.write(size, 0, size.length);
                        pos += size.length;
                    }
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
