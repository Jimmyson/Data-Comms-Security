package com.jimmyson.assignment_2;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */
public class TCPsend {
    public TCPsend (Socket sock, String filename) throws Exception {
        File file = new File(filename);

        //FIND FILE
        if (file.exists()) {
            BufferedReader inFromFile = new BufferedReader(new FileReader(file));

            while(sock.isConnected())
            {
                DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
                outToClient.writeBytes(inFromFile.readLine());
            }

            sock.close();

        }
    }
}
