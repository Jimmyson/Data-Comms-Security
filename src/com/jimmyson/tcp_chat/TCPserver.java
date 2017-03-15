//package com.jimmyson.tcp_chat;

import java.net.*;
import java.io.*;

/**
 * Created by Jimmyson on 15/03/2017.
 */
public class TCPserver {
    public static void main(String argv[]) throws Exception
    {
        int lPort = 0;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i<argv.length; i++) {
            if(argv[i].equals("-p") || argv[i].equals("--port")) {
                lPort = Integer.parseInt(argv[i+1]);
            }
        }

        if(lPort == 0) {
            System.out.println("SPECIFY APPLICATION PORT: ");
            lPort = Integer.parseInt(inFromUser.readLine());
        }

        String clientSentence, capitalizedSentence;

        if(lPort > 1023) {
            ServerSocket welcomeSocket = new ServerSocket(lPort);

            while(true)
            {
                Socket connectionSocket = welcomeSocket.accept();

                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                capitalizedSentence = clientSentence.toUpperCase() + '\n';
                outToClient.writeBytes(capitalizedSentence);
            }

        } else {
            System.out.println("INVALID PORT");
        }
    }
}
