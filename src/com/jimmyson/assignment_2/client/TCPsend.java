package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 * @author James Crawford 9962522
 */

class TCPsend extends Thread {
    private Socket Sock;
    private File File;

    /**
     * Initiates the Server preparation and send the file to the Client
     *
     * <p>
     *    Checks for the file to see if such one exists.
     *    If so, then opens the Input and Output streams.
     *    It buffers the files into RAM then sends out the data
     * </p>
     *
     * There is currently a problem with sending and reciving data greater than 4MB.
     *
     * @param sock The Active Connection to the Client
     * @param filename The Literal name of the file being sent
     */
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

    /**
     * Checks to see if the connection is active
     * @return True when connection is active
     */
    boolean isConnected() {
        return (Sock.isConnected());
    }
}
