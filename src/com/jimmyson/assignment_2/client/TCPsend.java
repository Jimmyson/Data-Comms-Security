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
     * //@url http://www.coderpanda.com/java-socket-programming-transferring-large-sized-files-through-socket/
     */
    TCPsend(Socket sock, String filename) {
        this.Sock = sock;

        if (File.exists()) {
            try {
                BufferedOutputStream outToClient = new BufferedOutputStream(sock.getOutputStream());

                File file = new File(filename);
                byte[] byteArray = new byte[filename.length()];

                BufferedInputStream bufFileInput = new BufferedInputStream(new FileInputStream(file));

                System.out.println("Sending \""+filename+"\" to "+sock.getInetAddress().getHostAddress());

                try {
                    bufFileInput.read(byteArray, 0, byteArray.length);
                    outToClient.write(byteArray, 0, byteArray.length);
                    outToClient.flush();
                    outToClient.close();
                    sock.close();

                    System.out.println("Finished sending \""+filename + "\"");
                } catch (IOException e) {
                    System.out.println("Failed to send \""+filename + "\"");
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
