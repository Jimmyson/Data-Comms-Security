package com.jimmyson.tcp_chat;

import java.io.*;
import java.net.*;

/**
 * Created by Jimmyson on 15/03/2017.
 */
public class TCPclient {
    public static void main(String argv[]) throws Exception
    {
        int srvPort = 0;
        String srvAddr = "";
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i<argv.length; i++) {
            if(argv[i].equals("-p") || argv[i].equals("--port")) {
                srvPort = Integer.parseInt(argv[i+1]);
            }
            if(argv[i].equals("-d") || argv[i].equals("--dest")) {
                srvAddr = argv[i+1];
            }
        }

        //SET PORT NUMBER
        if(srvPort == 0) {
            //SET PORT INSIDE APPLICATION
            System.out.print("SPECIFY APPLICATION PORT (6789): ");
            srvPort = Integer.parseInt(inFromUser.readLine());
        }
        if (srvAddr.equals("")) {
            //SPECIFY DESTINATION
            System.out.print("SPECIFY DESTINATION DEVICE: ");
            srvAddr = inFromUser.readLine();
        }

        String sentence, modifiedSentence;

        while(true) {
            Socket clientSocket = new Socket(srvAddr, srvPort);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence + '\n');

            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        }
    }
}
