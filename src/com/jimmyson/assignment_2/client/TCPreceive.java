package com.jimmyson.assignment_2.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */
class TCPreceive {
    TCPreceive(Socket sock, String filename) throws Exception {
        //SEND REQUEST TO LISTENER
        DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
        InputStream inFromServer = sock.getInputStream();

        outToServer.writeBytes(filename+ "\n");

        File file = new File(filename);
        if(file.delete()) {
            byte[] buffer = new byte[1024];
            FileOutputStream toFile = new FileOutputStream(file);
            BufferedOutputStream output = new BufferedOutputStream(toFile);
            int read = inFromServer.read(buffer, 0 , buffer.length);
            output.write(buffer, 0, buffer.length);
            output.close();
            sock.close();
        }
        System.out.println("Finished Transfer!");
    }
}
