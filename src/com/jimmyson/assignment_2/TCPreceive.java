package com.jimmyson.assignment_2;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */
class TCPreceive {
    TCPreceive(Socket sock, String filename) throws Exception {
        //SEND REQUEST TO LISTENER
        DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
        outToServer.writeBytes(filename);

        File file = new File(filename);
        if(!file.exists()) {
            FileOutputStream fileWriter = new FileOutputStream(file);

            while(sock.isConnected()) {
                //Socket sock = new Socket(srvAddr, srvPort);

                //DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                int chr;
                while((chr = inFromServer.read()) > -2 && sock.isConnected()) {
                    if(chr == -1) {
                        sock.close();
                    } else {
                        fileWriter.write(chr);
                    }
                }

                //sentence = inFromUser.readLine();
                //outToServer.writeBytes(sentence + '\n');

                //modifiedSentence = inFromServer.readLine();
                //System.out.println("FROM SERVER: " + modifiedSentence);
            }
            fileWriter.close();
        }
    }
}
