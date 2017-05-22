package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 * @author James Crawford 9962522
 */
class TCPreceive {
    /**
     * Makes the requests for a specific file from a VideoServer.
     * Delete any file of the same name, and captures incoming data from the VideoServer to be written to the new file.
     *
     * There is currently an issue with file transfer greater than 4MB.
     *
     * @param sock Connection to the VideoServer
     * @param filename Filename of the Requested file
     *
     * URL http://stackoverflow.com/questions/4687615/how-to-achieve-transfer-file-between-client-and-server-using-java-socket
     * @throws Exception When socket connection is broken, or file is not accessible
     */
    TCPreceive(Socket sock, String filename) throws Exception {
        //SEND REQUEST TO LISTENER
        int byteRead;
        byte[] byteBuf = new byte[1];

        DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
        InputStream inFromServer = sock.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        System.out.println("Getting \"" + filename + "\" from " + sock.getInetAddress().getHostAddress());

        outToServer.writeBytes(filename+ "\n");

        BufferedOutputStream bufFileOutput = new BufferedOutputStream(new FileOutputStream(filename));
        inFromServer.read(byteBuf, 0, byteBuf.length);

        do {
            baos.write(byteBuf);
            byteRead = inFromServer.read(byteBuf);
        } while (byteRead != -1);

        bufFileOutput.write(baos.toByteArray());
        bufFileOutput.flush();
        bufFileOutput.close();
        sock.close();

        System.out.println("\"" + filename + "\" has been received");

        sock.close();
        System.out.println("Finished Transfer!");
    }
}
