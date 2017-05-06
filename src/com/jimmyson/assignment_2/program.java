package com.jimmyson.assignment_2;

import java.io.Console;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */

public class program {
    private static int port;

    public static void main(String argv[]) throws Exception
    {
        int index = 0;
        for (String param : argv) {
            switch (param) {
                case "-p":
                    port = Integer.parseInt(argv[index+1]);
            }
            index++;
        }

        //@TODO: BUILD GUI INTERACTION

        ServerSocket listener = new ServerSocket(port);

        while (!listener.isClosed()) { //FIX THIS LOOP
            new TCPsend(listener.accept(), "HellowWorld.txt");
        }

        //Check For Port Number
        //Get IP Address

        //Broadcast Socket Set

        //UDP call for active servers

        //TCP connection for send

        //TCP receive for fetch

        //@TODO: SEPARATE INTO OWN FUNCTION
        String srvIP = "Computer";
        int srvPort = 6789;
        String reqFile = "HelloWorld.txt";

        new TCPreceive(new Socket(srvIP, srvPort), reqFile);
    }

    private static boolean shutdownCheck()
    {
        //Check for active connections

        //If Active, ask
            //IF True, reject incoming connections and wait for transfer to end
            //IF False, Nothing

        return false;
    }
}
