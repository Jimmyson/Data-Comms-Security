package com.jimmyson.assignment_2;

import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */

public class program {
    private static final int TCPport = 4001;
    private static final int UDPport = 4000;
    private static TCPlistener Listen;
    private static UDPcall Call;

    public static void main(String argv[]) throws Exception
    {
        /*int port = 0;
        int index = 0;
        for (String param : argv) {
            switch (param) {
                case "-p":
                    port = Integer.parseInt(argv[index+1]);
            }
            index++;
        }*/

        //if(port >= 1024) {
            //@TODO: BUILD GUI INTERACTION
            Listen = new TCPlistener(TCPport);
            Call = new UDPcall(UDPport);

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
                String[] command = CommandSplit(input.toString());
                switch(command[0].toUpperCase()) {
                    case "ADD":
                        break;
                    case "DELETE":
                    case "DEL":
                        break;
                    case "GET":
                    case "LIST":
                    case "LS":
                        String formats = "\\.(avi|mp4|mkv|wmv|m4v|webm|mov|flv)";
                        File[] dirList = new File(".").listFiles();
                        if(dirList != null) {
                            for (File file : dirList) {
                                String fileName = file.getName();
                                if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).matches(formats)) {
                                    System.out.print(fileName);
                                }
                            }
                        } else {
                            System.out.print("No files exist");
                        }
                        break;
                    case "WHOHAS":
                        //WHOHAS "FILENAME" 192.168.0.1:4000
                        break;
                    case "REQUEST":
                    case "REQ":
                        //REQUEST "FILENAME" 192.168.0.1:4000
                        fetchFile(command[0],command[1],command[2]);
                        break;
                    case "ONLINE":
                        break;
                    case "Q":
                    case "QUIT":
                    case "EXIT":
                        if (shutdownCheck()) {
                            Listen.Terminate();
                        }
                        break;
                    default:
                        PrintToScreen(Name+": "+SendMessage(input.toString()));
                }
            }
        //}
    }

    private static String[] CommandSplit(String command) {
        return command.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private static String SendMessage(String message) throws Exception {
        Call.AllSend(message);
        return message;
    }

    private static void PrintToScreen(String message) {
        System.out.print(message);
    }

    private static void fetchFile(String file, String host, String port) throws Exception {
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
