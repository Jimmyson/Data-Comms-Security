package com.jimmyson.assignment_2.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Jimmyson on 3/05/2017.
 */

public class program {
    private static final int UDPport = 4000;
    private static final int TCPport = 4001;
    private static TCPlistener Listen;
    private static UDPcall Call;

    public static void main(String argv[]) throws Exception
    {
        //int port = 0;
        int index = 0;
        InetAddress server = null;
        for (String param : argv) {
            switch (param) {
                //case "-p":
                    //port = Integer.parseInt(argv[index+1]);
                    //break;
                case "-h":
                    server = (validIP(argv[index+1])) ? InetAddress.getByAddress(StringToIP(argv[index+1])) : InetAddress.getByName(argv[index+1]);
            }
            index++;
        }

        while (server == null) {

        }

        if(server != null) {
            Listen = new TCPlistener(TCPport);
            Call = new UDPcall(UDPport, server);
            Call.run();

            PrintToScreen("Username: ");
            String Name = (new InputStreamReader(System.in)).toString();
            PrintToScreen(SendMessage("WELCOME "+Name));

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            while(Listen.Running()) {
                String[] command = CommandSplit(input.readLine());
                switch(command[0].toUpperCase()) {
                    case "WELCOME":
                        break;
                    case "ADD":
                        SendMessage(input.toString());
                        break;
                    case "DELETE":
                    case "DEL":
                        SendMessage(input.toString());
                        break;
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
                        SendMessage(input.toString());
                        break;
                    case "GET":
                    case "REQUEST":
                    case "REQ":
                        //REQUEST "FILENAME" 192.168.0.1:4000
                        fetchFile(command[0],command[1]);
                        break;
                    case "ONLINE":
                        SendMessage(input.toString());
                        break;
                    case "Q":
                    case "QUIT":
                    case "EXIT":
                        SendMessage("BYE");
                        Call.Terminate();
                        if (shutdownCheck()) {
                            Listen.Terminate();
                        }
                        break;
                    default:
                        PrintToScreen(Name+": "+SendMessage(input.readLine()));
                }
            }
        }
    }

    private static boolean validIP(String address) {
        String[] parts = address.split("\\.");
        if(parts.length == 4) {
            for(String part : parts) {
                if(!(Integer.parseInt(part) >= 0 && Integer.parseInt(part) <= 255)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static byte[] StringToIP(String address) {
        String[] parts = address.split("\\.");
        byte[] ip = new byte[4];

        for (int j = 0; j < parts.length; j++) {
            ip[j] = (byte)Integer.parseInt(parts[j]);
        }

        return ip;
    }

    private static String[] CommandSplit(String command) {
        return command.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private static String SendMessage(String message) throws Exception {
        Call.Send(message);
        return message;
    }

    private static void PrintToScreen(String message) {
        System.out.print(message);
    }

    private static void fetchFile(String file, String host) throws Exception {
        if(validIP(host)) {
            new TCPreceive(new Socket(InetAddress.getByAddress(StringToIP(host)), 4001), file);
        } else {
            new TCPreceive(new Socket(InetAddress.getByName(host), 4001), file);
        }
    }

    private static boolean shutdownCheck()
    {
        //Check for active connections
            //Ask to rejects

        //If Active, ask
            //IF True, reject incoming connections and wait for transfer to end
            //IF False, Nothing

        return Listen.CheckConnections();
    }
}
