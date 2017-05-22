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
     *
     * URL: http://stackoverflow.com/questions/4687615/how-to-achieve-transfer-file-between-client-and-server-using-java-socket
     */
    TCPsend(Socket sock, String filename) {
        this.Sock = sock;
        File file = new File(".\\"+filename);

        //File[] dirList = new File(".").listFiles();

        try {
            if (file.exists()) {
                try {
                    BufferedOutputStream outToClient = new BufferedOutputStream(sock.getOutputStream());

                    byte[] byteArray = new byte[(int) file.length()];

                    BufferedInputStream bufFileInput = new BufferedInputStream(new FileInputStream(file));

                    System.out.println("Sending \"" + filename + "\" to " + sock.getInetAddress().getHostAddress());

                    try {
                        bufFileInput.read(byteArray, 0, byteArray.length);
                        outToClient.write(byteArray, 0, byteArray.length);
                        outToClient.flush();
                        outToClient.close();
                        sock.close();

                        System.out.println("Finished sending \"" + filename + "\"");
                    } catch (IOException e) {
                        System.out.println("Failed to send \"" + filename + "\"");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {
            try {
                sock.close();
            } catch (IOException f){
                //
            }
        }
    }

    /**
     * Checks to see if the connection is active
     * @return True when connection is active
     */
    boolean isClosed() {
        return (Sock.isClosed());
    }
}
