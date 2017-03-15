//package com.jimmyson.tcp_chat;

import java.net.*;
import java.io.*;

/**
 * Created by Jimmyson on 15/03/2017.
 */
public class TCPserver {
    public static void main(String argv[]) throws Exception
    {
        String clientSentence, capitalizedSentence;

        ServerSocket welcomeSocket = new ServerSocket(6789);

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
    }
}
