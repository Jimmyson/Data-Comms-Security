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
class TCPreceive {
    /**
     * Makes the requests for a specific file from a server.
     * Delete any file of the same name, and captures incoming data from the server to be written to the new file.
     *
     * There is currently an issue with file transfer greater than 4MB.
     *
     * @param sock Connection to the server
     * @param filename Filename of the Requested file
     * @url http://www.coderpanda.com/java-socket-programming-transferring-large-sized-files-through-socket/
     * @throws Exception When socket connection is broken, or file is not accessible
     */
    TCPreceive(Socket sock, String filename) throws Exception {
        //SEND REQUEST TO LISTENER
        DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(sock.getInputStream());

        outToServer.writeBytes(filename+ "\n");

        File file = new File(filename);

        System.out.println("Getting \"" + filename + "\" from " + sock.getInetAddress().getHostAddress());
        SocketChannel sockChan = sock.getChannel();

        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        FileChannel fileChan = accessFile.getChannel();

        while (sockChan.read(buffer) > 0) {
            buffer.flip();
            fileChan.write(buffer);
            buffer.clear();
        }
        fileChan.close();
        System.out.println("\"" + filename + "\" has been received");
        sockChan.close();

        sock.close();
        System.out.println("Finished Transfer!");
    }
}
