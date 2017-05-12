package com.jimmyson.assignment_2;

import java.io.Console;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */

public class program {
    private static TCPlistener listen;

    public static void main(String argv[]) throws Exception
    {
        int port = 0;
        int index = 0;
        for (String param : argv) {
            switch (param) {
                case "-p":
                    port = Integer.parseInt(argv[index+1]);
            }
            index++;
        }

        if(port >= 1024) {
            //@TODO: BUILD GUI INTERACTION
            listen = new TCPlistener(port);

            //Check For Port Number
            //Get IP Address

            //Broadcast Socket Set

            //UDP call for active servers

            //TCP connection for send

            //TCP receive for fetch
        }

        if (shutdownCheck()) {
            return;
        }
    }

    private static void fetchFile() throws Exception {
        String srvIP = "Computer";
        int srvPort = 6789;
        String reqFile = "HelloWorld.txt";

        new TCPreceive(new Socket(srvIP, srvPort), reqFile);
    }

    private static boolean shutdownCheck()
    {
        //Check for active connections
        if (listen.CheckConnections()) {
            //Ask to rejects
            return true;
        }

        //If Active, ask
            //IF True, reject incoming connections and wait for transfer to end
            //IF False, Nothing

        return false;
    }
}
