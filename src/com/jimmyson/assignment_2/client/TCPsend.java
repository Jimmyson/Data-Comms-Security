package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

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
     * @url http://www.coderpanda.com/java-socket-programming-transferring-large-sized-files-through-socket/
     */
    TCPsend(Socket sock, String filename) {
        this.Sock = sock;
        this.File = new File(filename);

        if (File.exists()) {
            try {
                System.out.println("Sending \""+filename+"\" to "+sock.getInetAddress().getHostAddress());
                SocketChannel sockChan = SocketChannel.open(sock.getRemoteSocketAddress());
                RandomAccessFile accessFile = new RandomAccessFile(File, "r");
                FileChannel inFromFile = accessFile.getChannel();

                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (inFromFile.read(buffer) > 0) {
                    buffer.flip();
                    sockChan.write(buffer);
                }

                System.out.println("Finished sending \""+filename + "\"");
                sockChan.close();
                accessFile.close();
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
