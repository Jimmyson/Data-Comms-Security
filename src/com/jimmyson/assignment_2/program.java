package com.jimmyson.assignment_2;

import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */

public class program {
    private static TCPlistener Listen;
    private static UDPcall Call;

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
            Listen = new TCPlistener(port);
            Call = new UDPcall(port+1);

            PrintToScreen("Username: ");
            String Name = (new InputStreamReader(System.in)).toString();
            PrintToScreen(SendMessage(Name+" has connected!"));

            //Check For Port Number
            //Get IP Address

            //Broadcast Socket Set

            //UDP call for active servers

            //TCP connection for send

            //TCP receive for fetch

            InputStreamReader input = new InputStreamReader(System.in);

            while(Listen.Running()) {
                switch(input.toString()) {
                    case "FILES":
                        break;
                    case "FETCH":
                        break;
                    case "EXIT":
                        if (shutdownCheck()) {
                            Listen.Terminate();
                        }
                        break;
                    default:
                        PrintToScreen(Name+": "+SendMessage(input.toString()));
                }
            }
        }
    }

    private static String SendMessage(String message) throws Exception {
        Call.AllSend(message);
        return message;
    }

    private static void PrintToScreen(String message) {
        System.out.print(message);
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
        if (Listen.CheckConnections()) {
            //Ask to rejects
            return true;
        }

        //If Active, ask
            //IF True, reject incoming connections and wait for transfer to end
            //IF False, Nothing

        return false;
    }
}
