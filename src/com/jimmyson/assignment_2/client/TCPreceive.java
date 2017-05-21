package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;

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
     * @throws Exception When socket connection is broken, or file is not accessible
     */
    TCPreceive(Socket sock, String filename) throws Exception {
        //SEND REQUEST TO LISTENER
        DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(sock.getInputStream());

        outToServer.writeBytes(filename+ "\n");

        File file = new File(filename);
        if(file.delete()) {
            long size = inFromServer.readLong();
            long pos = 0;

            FileOutputStream toFile = new FileOutputStream(file);
            BufferedOutputStream output = new BufferedOutputStream(toFile);

            while (pos <= size) {
                byte[] localBuf = new byte[inFromServer.readInt()];
                inFromServer.read(localBuf, 0, localBuf.length);
                toFile.write(localBuf);
                pos += localBuf.length;
                //pos += inFromServer.
            }
            output.close();
            sock.close();
        }
        System.out.println("Finished Transfer!");
    }
}
