package com.jimmyson.assignment_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */
public class TCPreceive {
    public TCPreceive(Socket sock, String filename) throws Exception {
        File file = new File(filename);
        if(!file.exists()) {
            FileOutputStream fileWriter = new FileOutputStream(file);

            while(inFromServer.ready()) {
                //Socket sock = new Socket(srvAddr, srvPort);

                //DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                fileWriter.write(inFromServer.read());

                //sentence = inFromUser.readLine();
                //outToServer.writeBytes(sentence + '\n');

                //modifiedSentence = inFromServer.readLine();
                //System.out.println("FROM SERVER: " + modifiedSentence);
            }

            sock.close();
        }


    }
}
